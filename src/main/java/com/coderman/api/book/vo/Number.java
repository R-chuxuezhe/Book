package com.coderman.api.book.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Number {

    @NotNull(message = "创建人不能为空")
    private Long createUser;

    private Long upNumber;

    private Long downNumber;

    private Long readNumber;
}
