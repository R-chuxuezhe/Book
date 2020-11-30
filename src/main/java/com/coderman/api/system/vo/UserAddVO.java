package com.coderman.api.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * @Version 1.0
 **/
@Data
public class UserAddVO {
    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String nickname;


    private String email;


    private String phoneNumber;

    private Integer status;


    private Date createTime;


    private Date modifiedTime;


    private Integer sex;


    private String salt;


    private Integer type;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birth;

    private String avatar;
}
