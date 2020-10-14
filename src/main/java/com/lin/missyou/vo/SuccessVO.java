package com.lin.missyou.vo;

import com.lin.missyou.exception.http.CreateSuccess;
import com.lin.missyou.exception.http.DeleteSuccess;
import com.lin.missyou.exception.http.UpdateSuccess;

// 通过异常的形式 返回给前端各种成功信息
public class SuccessVO {

    public static void create(){
        throw new CreateSuccess(0);
    }

    public static void update(){
        throw new UpdateSuccess(0);
    }

    public static void delete(){
        throw new DeleteSuccess(0);
    }
}
