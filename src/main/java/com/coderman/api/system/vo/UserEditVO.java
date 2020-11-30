package com.coderman.api.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Version 1.0
 **/
@Data
public class UserEditVO {
    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;


    private String nickname;


    private String email;


    private String phoneNumber;


    private Integer sex;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birth;

    private String password;

    private String avatar;

}
