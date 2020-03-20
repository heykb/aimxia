package com.zhu.rimxia.biz.util;

import lombok.Data;


@Data
public class PasswordObject {
    private String salt;
    private String password;
}
