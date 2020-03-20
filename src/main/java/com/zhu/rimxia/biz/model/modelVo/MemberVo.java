package com.zhu.rimxia.biz.model.modelVo;

import com.zhu.rimxia.biz.validGroup.AddGroup;
import com.zhu.rimxia.biz.validGroup.UpdateGroup;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberVo {
    /**
     * 用户id
     */
    @NotNull(message = "用户Id无效",groups = UpdateGroup.class)
    private Long userId;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空",groups = AddGroup.class)
    @Pattern(regexp = "^[\\w\\u4e00-\\u9fa5]+$",groups = AddGroup.class,message = "用户名只能由数字字母下划线构成")
    private String userName;

    /**
     * 头像
     */
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
    @NotEmpty(message = "密码无效",groups = AddGroup.class)
    private String password;

    @NotNull(message = "请输入验证码",groups = {AddGroup.class})
    private String valiCode;

    /**
     * 邮箱
     */
    @NotNull(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$",
            message = "邮箱格式不正确",
            groups = {AddGroup.class})
    private String emailAddr;
}
