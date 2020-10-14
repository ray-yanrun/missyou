package com.lin.missyou.exception.http;

public class UpdateSuccess extends HttpException {

    public UpdateSuccess(int code){
        this.code = code;
        this.httpStatusCode = 202;
    }
}
