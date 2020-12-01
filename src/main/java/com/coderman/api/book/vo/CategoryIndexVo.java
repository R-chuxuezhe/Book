package com.coderman.api.book.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryIndexVo {

    private Long id;

    @NotBlank(message = "图书检索名称")
    private String name;

    private Long sort;

    private Long pid;

    private Integer addCount;

    private Integer readCount;

    private Integer downCount;

}
