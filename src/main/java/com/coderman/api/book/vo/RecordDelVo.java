package com.coderman.api.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class RecordDelVo {

    private Long id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer type;


    private Long bookId;


    private Long createUser;

    @NotNull(message = "是否删除状态不能为空")
    private Integer delStatus;
}
