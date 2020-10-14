package com.lin.missyou.exception.http;

public class DeleteSuccess extends HttpException {

    public DeleteSuccess(int code){
        this.code = code;
        this.httpStatusCode = 204;
    }
}
