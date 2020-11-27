package com.coderman.api.common.pojo.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Table(name = "tb_book")
public class Book {

    @Id
    //新增自增主键 返回主键值
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    private String bookName;


    private String bookUrl;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;


    private String author;


    private String press;

    private Integer upDown;

    private Integer status;

    private Long createUser;


    private Long categoryId;


    private String remark;

    private Integer delStatus;

    private String cover;

}
