package com.coderman.api.system.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @Author
 * @Date 2020/8/19 14:57
 * @Version 1.0
 **/
public interface UploadService {

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    String getUploadFilePath(MultipartFile file)throws IOException;

}
