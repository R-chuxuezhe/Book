package com.coderman.api.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class RecordCountVo {

    private Long id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @NotNull(message = "查询类型不能为空")
    private Integer type;

    private Integer coverRead;

    @NotNull(message = "查询天数不能为空")
    private Integer day;

    private String sdate;
}
