package com.nt.utils.dao;

/**
 * 微信操作全局异常，所有访问微信接口的方法都会抛出此异常
 *
 * @author shenjian
 */
@SuppressWarnings("serial")
public class WeixinException extends Exception{

    public WeixinException(String msg) {
        super(msg);
    }

    public WeixinException(Exception cause) {
        super(cause);
    }

    public WeixinException(String msg, Exception cause) {
        super(msg, cause);
    }

}
