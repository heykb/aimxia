package com.zhu.rimxia.biz.exception;

/**
 * 核心模块错误(0-999),具体细分子模块：<br>
 * 0-99为系统常见异常的错误码，如IllegalArgumentException<br>
 * 100-199为日期相关错误<br>
 *
 * <br>
 * 900-999为action,state,audit相关的错误定义
 * <br>
 */
public enum CommonErrorCode implements ErrorCode {

    SYSTEM_ERROR(1, "系统异常:{0}"),
    SYSTEM_INVALID_CONFIG(2, "配置错误:{0}"),
    SYSTEM_CLASS_NOT_FOUND(3, "无法加载class:{0}"),
    SYSTEM_UNSUPPORTED_ENCODING(4, "不支持的编码:{0}"),
    SYSTEM_UNSUPPORTED_OPERATION(5, "不支持操作方式:{0}"),
    SYSTEM_USER_UNLOGED(9, "用户没有登录"),
    OBJECT_ALREADY_EXISTS(10, "对象已经存在:{0}"),
    OBJECT_IS_NULL(11, "对象为空:{0}"),
    OBJECT_NOT_EXISTS(12, "对象不存在:{0}"),
    ILLEGAL_TYPE(13, "非法的类型:{0}"),
    OBJECT_NOT_FOUND(14, "未找到对象"),
    ACCOUNT_NOT_EXISTS(15,"账号不存在"),
    PASSWORD_ERROR(16,"密码错误"),

    ILLEGAL_PARAM(20, "非法的参数{0}"),
    ILLEGAL_USER(21, "非法的用户{0}"),
    ILLEGAL_STATE(22, "非法的状态：{0}"),
    ILLEGAL_PARAM_EMPTY(23, "参数{0}不能为空"),
    ILLEGAL_PARAM_NOT_INTEGER(24, "参数{0}必须为整数"),
    ILLEGAL_PARAM_NOT_POSITIVE_INTEGER(25, "参数{0}必须为正整数"),
    EMAIL_AUTHENTICATION(30, "邮件服务器认证不通过"),
    EMAIL_TEMPLATE(31, "邮件模板{0}异常"),
    EMAIL_MESSAGE_ERROR(32, "邮件消息错误"),
    EMAIL_SEND_TO_NULL(33, "邮件发往的邮箱地址不能为空"),
    EMAIL_CONTENT_NULL(34, "邮件内容不能为空"),
    INEFFECTIVE_STATE(35, "目前状态为[{0}]，不可修改为[{1}]"),
    MEMBER_IS_ACITON_STATUS(36,"用户{0}已处于激活状态"),
    EMAIL_LINK_EXPIRE(37,"此激活链接已过期"),
    EAMIL_ACTIVATION_CODE_ERROR(38,"激活码错误"),
    CURRENCY(39, "{0}"),

    FILE_NO_EXIST(40, "文件{0}不存在"),
    FILE_READ_ERROR(41, "文件读取错误:{0}"),
    FILE_NO_SUPPORT(42, "文件{0}不支持"),
    FILE_UPLOAD_ERROR(43, "文件上传出错"),
    File_TOO_large(44,"文件最大{0}"),
    INVALID_PARAMETER(45, "{0}:{1}"),//参数无效
    FILE_ENCODE_ERROR(46,"{0}视频转码失败"),

    //日期错误代码定义
    DATE_ILLEGAL_FORMAT(100, "日期{0}的格式非法"),
    DATE_FORMAT_ERROR(101, "字符串{0}无法转换成格式为{1}的日期"),
    DATE_START_NULL(102, "开始时间不能为空"),
    DATE_END_NULL(103, "结束时间不能为空"),
    DATE_END_LESSTHAN_NOW(104, "结束时间{0}不能小于当前时间"),
    DATE_END_BEFORE_START(105, "开始时间{0}必须早于结束时间{1}"),
    DATE_RANGE_OVERSTEP_DAYS(106, "开始时间{0}和结束时间{1}范围不能超过{2}天"),
    DATE_RANGE_LESSTHAN_DAYS(107, "开始时间{0}和结束时间{1}范围不能小于{2}天"),

    VALICODE_ERROR(108,"验证码错误"),
    VALICODE_EXPIRED(109,"验证码过期"),
    NEW_PASSWORD_SAME_AS_LAST(110,"新密码不能与旧密码一样"),

    NO_TOKEN(1000, "未授权的访问！"),
    TOKEN_EXPIRED(1003,"Token过期"),
    TOKEN_INVALID(1001, "Token无效"),
    ROLE_INVALID(1004,"权限不够"),
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    INNER_ERROR(500, "系统内部错误:{0}"),


    ALREADY_LIKE(1010,"已经点赞了");


    private int code;
    private String errorMsg;

    CommonErrorCode(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
