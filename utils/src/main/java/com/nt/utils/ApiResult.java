package com.nt.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiResult {


    private static Logger log = LoggerFactory.getLogger(ApiResult.class);

    private int code;    // 返回码
    private String message;    //	返回消息

    @JsonInclude(Include.NON_NULL)
    private Object data;    // 返回数据

    public ApiResult() {
        this.setCode(0);
    }

    public ApiResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiResult(Object data) {
        this.data = data;
    }

    /**
     * 设置自定义错误，其中code设置为-1，一般用于检测参数的错误信息
     *
     * @param message 要设置的错误内容
     */
    public void setCustomerMessage(String message) {
        this.code = -1;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setCode(String msg) {
        this.message = msg;
    }

    /**
     * 错误消息
     *
     * @param message
     * @return
     */
    public static ApiResult fail(String message) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCustomerMessage(message);
        log.warn(message);
        return apiResult;
    }

    /**
     * 错误消息
     *
     * @return
     */
    public static ApiResult fail() {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(-1);
        return apiResult;
    }

    public static ApiResult fail(Object data) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(-1);
        apiResult.setData(data);
        return apiResult;
    }

    /**
     * 成功消息
     *
     * @return
     */
    public static ApiResult success() {
        return new ApiResult();
    }

    /**
     * 成功消息
     *
     * @param message 自定义的成功消息
     * @return
     */
    public static ApiResult success(String message) {
        return success(message, null);
    }

    /**
     * 成功消息
     *
     * @return
     */
    public static ApiResult success(Object data) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(0);
        apiResult.setData(data);
        return apiResult;
    }

    /**
     * 成功消息
     *
     * @return
     */
    public static ApiResult success(String message, Object data) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(0);
        apiResult.setMessage(message);
        apiResult.setData(data);
        return apiResult;
    }


}
