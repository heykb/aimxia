package com.zhu.rimxia.biz.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private String accessToken;

    private String memberId;

    private String[] roles;

    private String[] permissions;

    private String refreshToken;

}
