package com.lwt.tavern.controller;


import com.lwt.tavern.common.OSS.AliyunOSSOperator;
import com.lwt.tavern.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class UploadController {

    @Autowired
    private AliyunOSSOperator ossOperator;

    @PostMapping("/upload")
    public Result upload(MultipartFile image) throws Exception {
        log.info("上传文件: {}", image);
        if (!image.isEmpty()) {
            String originalFilename = image.getOriginalFilename();
            String url = ossOperator.upload(image.getBytes(), originalFilename);
            return Result.success(url);
        }
        return Result.fail("上传失败");
    }
}



