package com.coderman.api.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class Record {

    private Long id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @NotNull(message = "类型不能为空")
    private Integer type;

    @NotNull(message = "书籍ID不能为空")
    private Long bookId;

    @NotNull(message = "创建人不能为空")
    private Long createUser;
}
