package com.nt.utils;


import lombok.Getter;

@Getter
public enum ResultEnum {


    SUCCESS(0,"成功"),
    ERROR(1,"失败"),
    ERROR_PAGE(2,"error/error"),
    PARAMS_NOT_FOUNT(3,"缺少必要参数"),
    PUBLISH_REDIS_SUCCESS(100,"发送消息成功"),
    PUBLISH_REDIS_ERROR(101,"发送消息失败");
    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
