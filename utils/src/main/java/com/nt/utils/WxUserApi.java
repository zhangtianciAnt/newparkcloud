package com.nt.utils;

import com.nt.utils.dao.WeixinOauth2Token;
import com.nt.utils.dao.WxEnterpriseUser;
import com.nt.utils.dao.WxUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 微信平台接口的直接实现工具类.
 *
 * @author shenjian
 */
public class WxUserApi {

    private static Logger logger = LoggerFactory.getLogger(WxUserApi.class);

    // 根据openid获取用户信息
    public final static String USERINFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";

    // 获取应用下的用户信息
    public final static String USERINFO_URL_AGENT = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s&agentid=%s";

    // 获取微信服务号用户openid
    public final static String WECHAT_SERVICE_OPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 获取用户openid
     * @param appId 公众号的唯一标识
     * @param secret 公众号的appsecret
     * @param code 填写第一步获取的code参数
     * @return
     */
    public static WeixinOauth2Token getWeChatOauth2Token(String appId, String secret, String code) {
        WeixinOauth2Token weixinOauth = null;
        try {
            String requestUrl = String.format(WECHAT_SERVICE_OPENID, appId, secret, code);
            String responseContent = WxHttpUtils.httpsRequest(requestUrl, "GET");
            weixinOauth = WeixinOauth2Token.fromJson(responseContent);

        } catch (Exception e) {
            logger.error("获取用户openid调用失败：", e);
        }
        return weixinOauth;
    }

    /**
     * 获取微信接口所需的access_token
     *
     * @param appId
     *            凭证ID
     * @param appSecret
     *            凭证秘钥
     * @return access_token值
     */
    public static WxUser userInfo(String appId, String appSecret, String code) {
        String responseContent = null;
        try {
            String accessToken = WxBaseApi.getAccessToken(appId, appSecret);
            String requestUrl = String.format(USERINFO_URL, accessToken, code);

            responseContent = WxHttpUtils.httpsRequest(requestUrl, "GET");

            if (logger.isInfoEnabled()) {
                logger.info("获取用户头像的返回信息：" + responseContent);
            }
        } catch (Exception e) {
            logger.error("获取用户昵称和头像调用失败：", e);
        }
        return WxUser.fromJson(responseContent);
    }

    /**
     * 获取微信接口所需的access_token
     *
     * @param appId
     *            凭证ID
     * @param appSecret
     *            凭证秘钥
     * @return access_token值
     */
    public static WxEnterpriseUser userInfo(String appId, String appSecret, String code, String agentid) {
        String responseContent = null;
        try {
            String accessToken = WxBaseApi.getAccessToken(appId, appSecret);
            String requestUrl = String.format(USERINFO_URL_AGENT, accessToken, code, agentid);

            responseContent = WxHttpUtils.httpsRequest(requestUrl, "GET");

            if (logger.isInfoEnabled()) {
                logger.info("获取用户头像的返回信息：" + responseContent);
            }
        } catch (Exception e) {
            logger.error("获取用户昵称和头像调用失败：", e);
        }
        return WxEnterpriseUser.fromJson(responseContent);
    }

}

