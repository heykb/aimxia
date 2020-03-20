package com.zhu.rimxia.biz.exception;

/**
 * 错误码，为每一种错误定义一个错误码，避免为每种错误创建异常.错误码分配如下：<br>
 */
public interface ErrorCode {

    public int getCode();

    public String getErrorMsg();
}
