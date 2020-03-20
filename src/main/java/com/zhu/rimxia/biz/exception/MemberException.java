package com.zhu.rimxia.biz.exception;

/**
 * 用户信息相关的异常
 */
public enum MemberException implements ErrorCode {
    ID_CARD_IS_EXIST("身份证号已经存在",1001),
    TELEPHONE_IS_EXIST("该手机号已经被使用",1002),
    LOGINNAME_IS_EXIST("该登录名已经被使用",1003),
    MULTIPLE_DATA_EXISTS("存在多条数据",1004),
    USER_NOT_EXIST("用户不存在",1005),
    PASSWORD_IS_ERROR("用户密码错误",1006),
    USER_IS_DISABLED_1("用户未激活",1007),
    PHONE_IS_NULL("手机号为空",1008),
    EAMIL_IS_EXIST("该邮箱已被使用",1009),
    VALICODE_NO("验证码过期或错误",1010),
    USER_IS_DISABLED_2("用户被禁用，联系管理员",1011),
    USER_IS_DISABLED_3("用户被禁用，联系超级管理员",1012),
    USER_IS_DISABLED_4("用户被禁用，联系开发人员",1013);


    private int code;
    private String msg;

    MemberException(String msg, Integer code) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
