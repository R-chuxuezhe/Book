package com.coderman.api.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class BookVo {

    private Long id;

    @NotBlank(message = "图书名称不能为空")
    private String bookName;

    @NotBlank(message = "图书地址不能为空")
    private String bookUrl;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    @NotBlank(message = "作者不能为空")
    private String author;

    @NotBlank(message = "出版社不能为空")
    private String press;

    private Integer upDown;

    private Integer status;

    @NotNull(message = "创建人不能为空")
    private Long createUser;

    @NotNull(message = "图书检索ID不能为空")
    private Long categoryId;

    @NotBlank(message = "备注信息不能为空")
    private String remark;

    private Integer delStatus;
}
