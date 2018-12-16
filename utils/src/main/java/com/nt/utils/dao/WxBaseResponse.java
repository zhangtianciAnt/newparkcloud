package com.nt.utils.dao;


/**
 * 微信请求返回的基础状态数据
 */
public class WxBaseResponse {
    private int    errcode; // 错误码,为0表示请求成功
    private String errmsg;  // 错误信息

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

}

