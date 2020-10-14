package com.lin.missyou.exception.http;

public class CreateSuccess extends HttpException {

    public CreateSuccess(int code){
        this.code = code;
        this.httpStatusCode = 201;
    }
}
