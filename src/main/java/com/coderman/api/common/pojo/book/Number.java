package com.coderman.api.common.pojo.book;

import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
public class Number {

    @Id
    @NotNull(message = "创建人不能为空")
    private Long createUser;

    private Long upNumber;

    private Long downNumber;

    private Long readNumber;
}
