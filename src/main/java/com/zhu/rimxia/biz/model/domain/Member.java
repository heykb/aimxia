package com.zhu.rimxia.biz.model.domain;

import javax.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {
    /**
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 头像
     */
    @Column(name = "head_img")
    private String headImg;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 密码
     */
    private String password;

    /**
     * 秘钥
     */
    private String salt;

    /**
     * 邮箱
     */
    @Column(name = "email_addr")
    private String emailAddr;

    /**
     * 禁用级别0未禁用
     */
    @Column(name = "is_disable")
    private Integer isDisable;

    /**
     * 是否删除
     */
    @Column(name = "is_del")
    private Boolean isDel;

    /**
     * 用户角色
     */
    private String roles;

    /**
     * 用户权限
     */
    private String permissions;

    /**
     * 禁止评论至日期
     */
    @Column(name = "disable_comment")
    private Long disableComment;
}