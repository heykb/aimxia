package com.zhu.rimxia.biz.model.domain;

import lombok.*;

import javax.persistence.*;

@Table(name = "activation_code")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ActivationCode {
    /**
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户激活码
     */
    @Column(name = "activation_code")
    private String activationCode;

    /**
     * 激活码生成时间
     */
    @Column(name = "made_time")
    private Long madeTime;


}