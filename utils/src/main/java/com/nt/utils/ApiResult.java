package com.nt.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ApiResult {

	private int code;	// 返回码
    private String message;	//	返回消息
    
    @JsonInclude(Include.NON_NULL)
    private Object data;	// 返回数据

    public ApiResult() {
    	this.setCode(ApiCode.SUCCESS);
    }

    public ApiResult(ApiCode apiCode) {
        this(apiCode.getCode(), apiCode.getMessage(), null);
    }

    public ApiResult(ApiCode apiCode, Object data) {
        this(apiCode.getCode(), apiCode.getMessage(), data);
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

    public ApiResult(Object data){
    	this.data=data;
    }
    
    /**
     * 设置自定义错误，其中code设置为-1，一般用于检测参数的错误信息
     * 
     * @param message
     *            要设置的错误内容
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
    
    public void setCode(ApiCode apiCode) {
        this.code = apiCode.getCode();
        this.message = apiCode.getMessage();
    }
    
    /**
     * 错误消息
     * @param message
     * @return
     */
    public static ApiResult fail(String message) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCustomerMessage(message);
        return apiResult;
    }
    
    /**
     * 错误消息
     * @param apiCode 状态码
     * @return
     */
    public static ApiResult fail(ApiCode apiCode) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(apiCode);
        return apiResult;
    }
    
    /**
     * 成功消息
     * @return
     */
    public static ApiResult success() {
        return new ApiResult();
    }
    
    /**
     * 成功消息
     * @param message 自定义的成功消息
     * @return
     */
    public static ApiResult success(String message) {
        return success(message, null);
    }
    
    /**
     * 成功消息
     * @return
     */
    public static ApiResult success(Object data) {
    	 ApiResult apiResult = new ApiResult();
         apiResult.setCode(ApiCode.SUCCESS);
         apiResult.setData(data);
         return apiResult;
    }
    
    /**
     * 成功消息
     * @return
     */
    public static ApiResult success(String message, Object data) {
    	 ApiResult apiResult = new ApiResult();
         apiResult.setCode(ApiCode.SUCCESS);
         apiResult.setMessage(message);
         apiResult.setData(data);
         return apiResult;
    }
	
	
}
