package com.coderman.api.system.vo;

import lombok.Data;

/**
 * @Author
 * @Date 2020/4/25 10:47
 * @Version 1.0
 **/
@Data
public class ImageAttachmentVO {
    //图片类型
    private String mediaType;
    //图片后缀
    private String suffix;
    //图片路径
    private String path;

}
