package com.zhu.rimxia.biz.model.modelVo;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginVo {

    @NotBlank(message = "登录名不能为空")
    private String loginName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码key无效")
    private String key;

    @NotBlank(message = "验证码不能为空")
    private String valicode;
}
