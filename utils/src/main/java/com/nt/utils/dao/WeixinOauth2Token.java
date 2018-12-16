package com.nt.utils.dao;
import com.alibaba.fastjson.JSON;

/**
 *
 * 类名称：WeixinOauth2Token 类描述：网页授权信息 创建人：Lijianpeng 创建时间：2014-8-10 下午8:47:49
 *
 * @version
 */
public class WeixinOauth2Token {
    // 网页授权接口调用凭证
    private String accessToken;
    // 凭证有效时长
    private int    expiresIn;
    // 用于刷新凭证
    private String refreshToken;
    // 用户标识
    private String openid;
    // 用户授权作用域
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openId) {
        this.openid = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public static WeixinOauth2Token fromJson(String json) {
        return JSON.parseObject(json, WeixinOauth2Token.class);
    }
}
