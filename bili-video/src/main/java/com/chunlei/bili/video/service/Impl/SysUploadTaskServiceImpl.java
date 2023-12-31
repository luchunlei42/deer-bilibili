package com.chunlei.bili.video.service.Impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.chunlei.bili.video.config.MinioConfig;
import com.chunlei.bili.video.constant.MinioConstant;
import com.chunlei.bili.video.entity.SysUploadTask;
import com.chunlei.bili.video.service.SysUploadTaskService;
import com.chunlei.bili.video.to.InitTaskParam;
import com.chunlei.bili.video.vo.TaskInfoDTO;
import com.chunlei.bili.video.vo.TaskRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysUploadTaskServiceImpl implements SysUploadTaskService {
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private MinioConfig minioConfig;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private final String redisIdentifierPrefix = "identifier:";

    @Override
    public SysUploadTask getByIdentifier(String identifier) {
        String json = redisTemplate.opsForValue().get(redisIdentifierPrefix+identifier);
        if (StringUtils.isEmpty(json)){
            return null;
        }else{
            SysUploadTask task = JSON.parseObject(json, SysUploadTask.class);
            return task;
        }
    }

    @Override
    public TaskInfoDTO initTask(InitTaskParam param) {
        Date currentDate = new Date();
        String bucketName = minioConfig.getBucket();
        String fileName = param.getFileName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String key = StrUtil.format("{}/{}.{}", DateUtil.format(currentDate, "YYYY-MM-dd"), IdUtil.randomUUID(), suffix);
        String contentType = MediaTypeFactory.getMediaType(key).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        InitiateMultipartUploadResult initiateMultipartUploadResult = amazonS3.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, key).withObjectMetadata(objectMetadata));
        String uploadId = initiateMultipartUploadResult.getUploadId();

        SysUploadTask task = new SysUploadTask();
        int chunkNum = (int) Math.ceil(param.getTotalSize()*1.0 / param.getChunkSize());
        task.setBucketName(minioConfig.getBucket())
                .setChunkNum(chunkNum)
                .setChunkSize(param.getChunkSize())
                .setTotalSize(param.getTotalSize())
                .setFileIdentifier(param.getIdentifier())
                .setFileName(fileName)
                .setObjectKey(key)
                .setUploadId(uploadId);
        String jsonString = JSONObject.toJSONString(task);
        redisTemplate.opsForValue().set(redisIdentifierPrefix+param.getIdentifier(),jsonString);
        return new TaskInfoDTO().setFinished(false).setTaskRecord(TaskRecordDTO.convertFromEntity(task)).setPath(getPath(bucketName, key));
    }

    @Override
    public String getPath(String bucket, String objectKey) {
        return StrUtil.format("{}/{}/{}", minioConfig.getEndpoint(), bucket, objectKey);
    }

    @Override
    public TaskInfoDTO getTaskInfo(String identifier) {
        SysUploadTask task = getByIdentifier(identifier);
        if (task == null){
            return null;
        }
        TaskInfoDTO result = new TaskInfoDTO().setFinished(true).setTaskRecord(TaskRecordDTO.convertFromEntity(task)).setPath(getPath(task.getBucketName(), task.getObjectKey()));
        boolean doesObjectExist = amazonS3.doesObjectExist(task.getBucketName(), task.getObjectKey());
        if (!doesObjectExist){
            //未上传完，返回已上传的分片
            ListPartsRequest listPartsRequest = new ListPartsRequest(task.getBucketName(), task.getObjectKey(), task.getUploadId());
            PartListing partListing = amazonS3.listParts(listPartsRequest);
            result.setFinished(false).getTaskRecord().setExitPartList(partListing.getParts());
        }
        return result;
    }

    @Override
    public String genPreSignUploadUrl(String bucket, String objectKey, Map<String, String> params) {
        Date currentDate = new Date();
        Date expireDate = DateUtil.offsetMillisecond(currentDate, MinioConstant.PRE_SIGN_URL_EXPIRE.intValue());
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, objectKey).withExpiration(expireDate).withMethod(HttpMethod.PUT);
        if (params != null){
            params.forEach((key,val)->{
                request.addRequestParameter(key,val);
            });
        }
        URL url = amazonS3.generatePresignedUrl(request);
        return url.toString();
    }

    @Override
    public CompleteMultipartUploadResult merge(String identifier) {
        SysUploadTask task = getByIdentifier(identifier);
        if (task == null){
            throw new RuntimeException("分片任务不存在");
        }
        ListPartsRequest listPartsRequest = new ListPartsRequest(task.getBucketName(), task.getObjectKey(), task.getUploadId());
        PartListing partListing = amazonS3.listParts(listPartsRequest);
        List<PartSummary> parts = partListing.getParts();
        if (!task.getChunkNum().equals(parts.size())){
            throw new RuntimeException("分片缺失，请重新上传");
        }
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest()
                .withUploadId(task.getUploadId())
                .withKey(task.getObjectKey())
                .withBucketName(task.getBucketName())
                .withPartETags(parts.stream().map(partSummary -> new PartETag(partSummary.getPartNumber(), partSummary.getETag())).collect(Collectors.toList()));
        CompleteMultipartUploadResult result = amazonS3.completeMultipartUpload(completeMultipartUploadRequest);
        return result;
    }

    @Override
    public boolean hasObjectExits(String bucketName, String objectKey) {
        return amazonS3.doesObjectExist(bucketName, objectKey);
    }


}
