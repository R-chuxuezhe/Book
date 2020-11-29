package com.coderman.api.system.vo;

import lombok.Data;


import javax.validation.constraints.NotBlank;


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
}
