package com.chunlei.bili.video.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.nacos.shaded.io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.common.dto.UserDTO;
import com.chunlei.bili.common.utils.UserHolder;
import com.chunlei.bili.member.model.Member;
import com.chunlei.bili.video.client.BiliMemberFeignClient;
import com.chunlei.bili.video.client.BiliSearchFeignClient;
import com.chunlei.bili.video.dao.VideoTagRelationDao;
import com.chunlei.bili.video.mapper.*;
import com.chunlei.bili.video.model.*;
import com.chunlei.bili.video.service.SysUploadTaskService;
import com.chunlei.bili.video.service.VideoService;
import com.chunlei.bili.video.service.VideoStatService;
import com.chunlei.bili.video.vo.PublishDTO;
import com.chunlei.bili.video.vo.PublishMemberDTO;
import com.chunlei.bili.video.vo.SubmissionDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideoDetailMapper videoDetailMapper;
    @Autowired
    VideoMapper videoMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    VideoTagRelationDao videoTagRelationDao;
    @Autowired
    Executor uploadThreadPool;
    @Autowired
    SysUploadTaskService uploadTaskService;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    VideoTagRelationMapper videoTagRelationMapper;
    @Autowired
    BiliMemberFeignClient memberFeignClient;
    @Autowired
    BiliSearchFeignClient searchFeignClient;
    @Autowired
    AmazonS3 amazonS3;
    @Autowired
    VideoStatService videoStatService;

    @Override
    @Transactional
    public void uploadVideo(SubmissionDTO dto) throws ExecutionException, InterruptedException {

        if(!uploadTaskService.hasObjectExits(dto.getBucketName(),dto.getObjectKey())){
            throw new RuntimeException("文件不存在");
        }

        Long userId = UserHolder.getUser().getId();
        Video video = new Video();
        CompletableFuture<Void> videoTask = CompletableFuture.runAsync(() -> {
            BeanUtil.copyProperties(dto, video);
            video.setMemberId(userId);
            video.setCreateTime(new Date());
            video.setUpdateTime(new Date());
            video.setPublishStatus((byte) 0);
            videoMapper.insertSelective(video);
        }, uploadThreadPool);


        //视频上传信息
        VideoDetail detail = new VideoDetail();
        CompletableFuture<Void> detailTask = videoTask.thenRunAsync(() -> {
            detail.setBucketName(dto.getBucketName());
            detail.setLocation(dto.getLocation());
            detail.setVideoKey(dto.getObjectKey());
            detail.setLocation(detail.getLocation());
            detail.setVideoId(video.getId());
            videoDetailMapper.insertSelective(detail);
        }, uploadThreadPool);


        //视频标签信息
        AtomicReference<Map<Long, Tag>> map = new AtomicReference<>(new HashMap<>());
        CompletableFuture<Void> tagsTask = CompletableFuture.runAsync(() -> {
            TagExample tagExample = new TagExample();
            if (dto.getTagIdList()!= null && dto.getTagIdList().size() >0){
                tagExample.createCriteria().andTagIdIn(dto.getTagIdList());
                List<Tag> tags = tagMapper.selectByExample(tagExample);
                map.set(tags.stream().collect(Collectors.toMap(Tag::getTagId, tag -> tag)));
            }
        }, uploadThreadPool);

        CompletableFuture<Void> relationTask = CompletableFuture.allOf(videoTask,tagsTask).thenRunAsync(() -> {
            List<Long> tagIdList = dto.getTagIdList();
            if (tagIdList != null && tagIdList.size() > 0) {
                List<VideoTagRelation> tagRelationList = tagIdList.stream().map(tagId -> {
                    VideoTagRelation videoTagRelation = new VideoTagRelation();
                    videoTagRelation.setTagId(tagId);
                    videoTagRelation.setVideoId(video.getId());
                    videoTagRelation.setTagName(map.get().get(tagId).getName());
                    return videoTagRelation;
                }).collect(Collectors.toList());
                videoTagRelationDao.saveBatch(tagRelationList);
            }
        }, uploadThreadPool);

        CompletableFuture.allOf(relationTask,detailTask).join();
    }

    @Override
    public Video findVideoById(Long videoId) {

        Video video = videoMapper.selectByPrimaryKey(videoId);
        return video;
    }

    @Override
    public R publish(Long videoId) {

        if (videoId == null){
            return R.failed("视频Id不能为空");
        }
        Video video = findVideoById(videoId);
        if (video == null){
            return R.failed("视频不见了喵");
        }

        PublishDTO publishDTO = new PublishDTO();
        publishDTO.setVid(videoId);
        publishDTO.setDescription(video.getVideoDescription());
        publishDTO.setTypeId(video.getCatalogId());
        publishDTO.setTitle(video.getVideoTitle());
        publishDTO.setPic(video.getImgUrl());
        publishDTO.setSendDate(video.getCreateTime());
        publishDTO.setPubDate(new Date());

        Long duration = video.getDuration();
        String duration_format = duration/60 + ":" + duration%60;
        publishDTO.setDuration(duration_format);

        //获取category信息
        Category category = categoryMapper.selectByPrimaryKey(video.getCatalogId());
        publishDTO.setTypename(category.getName());

        //获取tags信息
        List<String> tags =  videoTagRelationDao.findTagsByVideoId(videoId);
        publishDTO.setTag(tags);

        //获取用户信息
        R<Member> r = memberFeignClient.findMemberById(video.getMemberId());
        Member member = r.getData();
        PublishMemberDTO memberDTO = new PublishMemberDTO();
        memberDTO.setAuthor(member.getNickname());
        memberDTO.setSign(member.getSign());
        memberDTO.setAvatar(member.getAvatar());
        memberDTO.setMid(member.getId());
        memberDTO.setLevel(member.getGrowth());
        memberDTO.setVideo(1L);
        //TODO:获取粉丝数

        publishDTO.setMember(memberDTO);

        //TODO:获取播放量，弹幕量，收藏等等

        return searchFeignClient.publishVideo(publishDTO);
    }

    @Override
    public void playVideo(Long videoId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        VideoDetailExample videoDetailExample = new VideoDetailExample();
        videoDetailExample.createCriteria().andVideoIdEqualTo(videoId);
        List<VideoDetail> videoDetails = videoDetailMapper.selectByExample(videoDetailExample);
        if (videoDetails == null || videoDetails.size() == 0){
            throw new RuntimeException("视频不存在");
        }
        UserDTO user = UserHolder.getUser();
        if (user != null){
            videoStatService.setVideoView(videoId,user.getId());
        }

        VideoDetail detail = videoDetails.get(0);
        ObjectMetadata metadata = amazonS3.getObjectMetadata(detail.getBucketName(), detail.getVideoKey());
        long contentLength = metadata.getContentLength();
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        while(headerNames.hasMoreElements()){
            String header = headerNames.nextElement();
            headers.put(header, request.getHeader(header));
        }
        String rangeStr = request.getHeader("Range");
        String[] range;
        if(StringUtil.isNullOrEmpty(rangeStr)){
            rangeStr = "bytes=0-" + (contentLength-1);
        }
        range = rangeStr.split("bytes=|-");
        long begin = 0;
        if(range.length >= 2){
            begin = Long.parseLong(range[1]);
        }
        long end = contentLength-1;
        if(range.length >= 3){
            end = Long.parseLong(range[2]);
        }
        long len = (end - begin) + 1;
        String contentRange = "bytes " + begin + "-" + end + "/" + contentLength;
        response.setHeader("Content-Range", contentRange);
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Type", "video/mp4");
        response.setContentLength((int)len);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        GetObjectRequest rangeObjectRequest = new GetObjectRequest(detail.getBucketName(), detail.getVideoKey())
                .withRange(begin,end);
        S3Object amazonS3Object = amazonS3.getObject(rangeObjectRequest);
        S3ObjectInputStream inputStream = amazonS3Object.getObjectContent();
        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
             BufferedInputStream bis = new BufferedInputStream(inputStream);) {
            IOUtils.copy(bis, bos);
        } catch (
                IOException e) {
            if (e instanceof ClientAbortException) {
                // ignore
            } else {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public VideoDetail getVideoDetail(Long videoId) {
        VideoDetailExample example = new VideoDetailExample();
        example.createCriteria().andVideoIdEqualTo(videoId);
        List<VideoDetail> videoDetails = videoDetailMapper.selectByExample(example);
        if (videoDetails != null && videoDetails.size()>0){
            return videoDetails.get(0);
        }
        return null;
    }
}
