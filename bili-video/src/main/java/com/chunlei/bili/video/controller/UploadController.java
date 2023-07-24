package com.chunlei.bili.video.controller;

import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.video.entity.SysUploadTask;
import com.chunlei.bili.video.service.SysUploadTaskService;
import com.chunlei.bili.video.to.InitTaskParam;
import com.chunlei.bili.video.vo.TaskInfoDTO;
import com.chunlei.bili.video.vo.VideoUploadVo;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/minio")
public class UploadController {
    @Autowired
    SysUploadTaskService sysUploadTaskService;


    /**
     * 获取上传进度
     * @param identifier 文件md5
     * @return
     */
    @GetMapping("/tasks/{identifier}")
    public R<TaskInfoDTO> taskInfo (@PathVariable("identifier") String identifier) {
        System.out.println("获取任务信息");
        return R.success(sysUploadTaskService.getTaskInfo(identifier));
    }

    /**
     * 创建一个上传任务
     * @return
     */
    @PostMapping("/tasks")
    public R<TaskInfoDTO> initTask (@Valid @RequestBody InitTaskParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.failed(bindingResult.getFieldError().getDefaultMessage());
        }
        return R.success(sysUploadTaskService.initTask(param));
    }

    /**
     * 获取每个分片的预签名上传地址
     * @param identifier
     * @param partNumber
     * @return
     */
    @GetMapping("/tasks/{identifier}/{partNumber}")
    public R preSignUploadUrl (@PathVariable("identifier") String identifier, @PathVariable("partNumber") Integer partNumber) {
        SysUploadTask task = sysUploadTaskService.getByIdentifier(identifier);
        if (task == null) {
            return R.failed("分片任务不存在");
        }
        Map<String, String> params = new HashMap<>();
        params.put("partNumber", partNumber.toString());
        params.put("uploadId", task.getUploadId());
        return R.success(sysUploadTaskService.genPreSignUploadUrl(task.getBucketName(), task.getObjectKey(), params));
    }

    /**
     * 合并分片
     * @param identifier
     * @return
     */
    @PostMapping("/tasks/merge/{identifier}")
    public R merge (@PathVariable("identifier") String identifier) {
        CompleteMultipartUploadResult result = null;
        try {
            result = sysUploadTaskService.merge(identifier);
        }catch (RuntimeException e){
            R.failed(e.getMessage());
        }
        return R.success(result);
    }


    @PostMapping("/upload")
    public R uploadVideo(@RequestParam("file") MultipartFile file){
        try{
            //获取文件后缀，因此此后端代码可接收一切文件，上传格式前端限定
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
                    .toLowerCase();
            // 重构文件名称
            String pikId = UUID.randomUUID().toString().replaceAll("-", "");
            String newVidoeName = pikId + "." + fileExt;
            System.out.println("重构文件名防止上传同名文件："+newVidoeName);
            //保存视频
            //TODO:把视频保存到 Fastdfs
            String videoUrlFromFastdfs = "default";
//            File fileSave = new File(SavePath, newVidoeName);
//            file.transferTo(fileSave);
            //构造Map将视频信息返回给前端
            //视频名称重构后的名称
            VideoUploadVo vo = new VideoUploadVo();
            vo.setNewVideoName(newVidoeName);
            vo.setVideoUrl(videoUrlFromFastdfs);
            return  R.success(vo);

        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
            //保存视频错误则设置返回码为400
            return  R.failed() ;

        }
    }


}
