package com.coderman.api.common.pojo.system;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Excel("user")
@Table(name = "tb_user")
public class User {
    @Id
    private Long id;


    private String username;


    private String nickname;


    private String email;


    private String phoneNumber;

    private Integer status;


    private Date createTime;


    private Date modifiedTime;


    private Integer sex;


    private String salt;


    private Integer type;


    private String password;


    private Date birth;

    private String avatar;

}
