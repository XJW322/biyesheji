package com.rabbiter.association.controller;

import java.io.Serializable;
import lombok.Data;
// Result.java (通用返回结果类)
@Data
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public static Result success() {
        return success(null);
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}

