package com.lin.missyou.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UnifyResponse {

    private int code;
    private String message;
    private String request;
}
