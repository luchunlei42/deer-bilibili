package com.chunlei.bili.video.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.common.utils.UserHolder;
import com.chunlei.bili.video.client.BiliMemberFeignClient;
import com.chunlei.bili.video.client.BiliSearchFeignClient;
import com.chunlei.bili.video.dao.VideoTagRelationDao;
import com.chunlei.bili.video.mapper.*;
import com.chunlei.bili.video.model.*;
import com.chunlei.bili.video.service.SysUploadTaskService;
import com.chunlei.bili.video.service.VideoService;
import com.chunlei.bili.video.vo.PublishDTO;
import com.chunlei.bili.video.vo.PublishMemberDTO;
import com.chunlei.bili.video.vo.SubmissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
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
        //TODO:获取粉丝数

        publishDTO.setMember(memberDTO);

        //TODO:获取播放量，弹幕量，收藏等等


        return searchFeignClient.publishVideo(publishDTO);
    }
}
