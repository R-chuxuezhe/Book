package com.coderman.api.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class BookUpDownVo {

    private Long id;

    @NotNull(message = "上下架状态不能为空")
    private Integer upDown;

    private Long createUser;


}
