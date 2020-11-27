package com.coderman.api.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class CategoryVo {

    private Long id;

    @NotBlank(message = "图书检索名称")
    private String name;

    private Long sort;

    private Long pid;

}
