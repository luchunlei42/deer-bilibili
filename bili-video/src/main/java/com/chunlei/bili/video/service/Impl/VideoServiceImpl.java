package com.chunlei.bili.video.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.chunlei.bili.video.dao.VideoTagRelationDao;
import com.chunlei.bili.video.mapper.TagMapper;
import com.chunlei.bili.video.mapper.VideoDetailMapper;
import com.chunlei.bili.video.mapper.VideoMapper;
import com.chunlei.bili.video.model.*;
import com.chunlei.bili.video.service.SysUploadTaskService;
import com.chunlei.bili.video.service.VideoService;
import com.chunlei.bili.video.vo.SubmissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void uploadVideo(SubmissionDTO dto) throws ExecutionException, InterruptedException {

        if(uploadTaskService.hasObjectExits(dto.getBucketName(),dto.getObjectKey())){
            throw new RuntimeException("文件不存在");
        }

        Video video = new Video();
        CompletableFuture<Void> videoTask = CompletableFuture.runAsync(() -> {
            BeanUtil.copyProperties(dto, video);
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
        AtomicReference<Map<Long, Tag>> map = null;
        CompletableFuture<Void> tagsTask = CompletableFuture.runAsync(() -> {
            TagExample tagExample = new TagExample();
            tagExample.createCriteria().andTagIdIn(dto.getTagIdList());
            List<Tag> tags = tagMapper.selectByExample(tagExample);
            map.set(tags.stream().collect(Collectors.toMap(Tag::getTagId, tag -> tag)));
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

        CompletableFuture.allOf(relationTask,detailTask).get();
    }
}
