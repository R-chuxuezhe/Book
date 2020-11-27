package com.coderman.api.common.pojo.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Table(name = "tb_category")
public class Category {

    @Id
    private Long id;

    @NotBlank(message = "图书检索名称")
    private String name;

    private Long sort;

    private Long pid;

}
