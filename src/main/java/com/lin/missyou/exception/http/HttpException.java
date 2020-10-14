package com.lin.missyou.exception.http;

public class HttpException extends RuntimeException {

    protected Integer code;  // 自定义状态码
    protected Integer httpStatusCode = 500;  // http状态码

    public Integer getCode() {
        return code;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }
}
