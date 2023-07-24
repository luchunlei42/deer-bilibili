package com.chunlei.bili.video.service;

import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.chunlei.bili.video.entity.SysUploadTask;
import com.chunlei.bili.video.to.InitTaskParam;
import com.chunlei.bili.video.vo.TaskInfoDTO;

import java.util.Map;

public interface SysUploadTaskService {
    /**
     * 根据md5标识获取分片上传任务
     * @param identifier
     * @return
     */
    SysUploadTask getByIdentifier (String identifier);

    /**
     * 初始化一个任务
     */
    TaskInfoDTO initTask (InitTaskParam param);

    /**
     * 获取文件地址
     * @param bucket
     * @param objectKey
     * @return
     */
    String getPath (String bucket, String objectKey);

    /**
     * 获取上传进度
     * @param identifier
     * @return
     */
    TaskInfoDTO getTaskInfo (String identifier);

    /**
     * 生成预签名上传url
     * @param bucket 桶名
     * @param objectKey 对象的key
     * @param params 额外的参数
     * @return
     */
    String genPreSignUploadUrl (String bucket, String objectKey, Map<String, String> params);

    /**
     * 合并分片
     *
     * @param identifier
     * @return
     */
    CompleteMultipartUploadResult merge (String identifier);

    boolean hasObjectExits(String bucketName, String objectKey);
}
