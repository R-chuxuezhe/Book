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
public class UserVO{

    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @Email(message = "请输入正确的邮箱格式")
    private String email;

    @NotBlank(message = "电话号码不能为空")
    private String phoneNumber;

    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @NotNull(message = "性别不能为空")
    private Integer sex;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy年MM月dd日")
    @NotNull(message = "生日不能为空")
    private Date birth;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String avatar;

    private Integer upCount;

    private Integer downCount;

}
