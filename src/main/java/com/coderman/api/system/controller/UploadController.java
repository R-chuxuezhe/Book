package com.coderman.api.system.controller;

import com.coderman.api.common.bean.ResponseBean;

import com.coderman.api.system.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 文件上传
 * @Author
 * @Date 2020/3/18 10:29
 * @Version 1.0
 **/
@Slf4j
@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/upload")
public class UploadController {



    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片文件
     * @param file
     * @return
     */
    @ApiOperation(value = "上传文件")
    @PostMapping("/image")
    public ResponseBean uploadImage(MultipartFile file) throws IOException {
        String realPath=uploadService.getUploadFilePath(file);
        return ResponseBean.success(realPath);
    }



    /**
     * 删除图片
     * @param id
     * @return
     */
    @ApiOperation(value = "删除图片", notes = "删除数据库记录,删除图片服务器上的图片")
    @DeleteMapping("/delete/{id}")
    public ResponseBean delete(@PathVariable Long id){
        uploadService.delete(id);
        return ResponseBean.success();
    }

}
