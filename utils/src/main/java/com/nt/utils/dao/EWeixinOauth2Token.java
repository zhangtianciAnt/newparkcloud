package com.nt.utils.dao;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类名称：EWeixinOauth2Token 类描述：获取登录凭证 创建人：YANGSHUBO 创建时间：20200609
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EWeixinOauth2Token {

    // 出错返回码，为0表示成功，非0表示调用失败
    private int errcode;
    // 返回码提示语
    private String errmsg;
    // 获取到的凭证，最长为512字节
    private int expires_in;
    // 凭证的有效时间（秒）
    private String access_token;

    //阿里fastjson.JSON插件
    public static EWeixinOauth2Token fromJson(String json) {
        return JSON.parseObject(json, EWeixinOauth2Token.class);
    }

}
