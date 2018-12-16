package com.nt.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.nt.utils.dao.AccessToken;
import com.nt.utils.dao.WeixinException;
import com.nt.utils.dao.WxSupport;
import com.nt.utils.StringUtils;
import com.nt.utils.WxHttpUtils;


/**
 *
 * 微信平台接口的直接实现工具类.
 *
 * @author shenjian
 */
public class WxBaseApi {

    private static Logger logger = LoggerFactory.getLogger(WxBaseApi.class);

    // 获取凭证最多尝试的次数
    public static int         RETRY_COUNT      = 3;

    // 获取凭证的URL
    public final static String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    //token前缀
    private final static String ACCESS_TOKEN = "access_token_";

    /**
     * 获取微信接口所需的access_token
     *
     * @param appId
     *            凭证ID
     * @param appSecret
     *            凭证秘钥
     * @return access_token值
     */
    public static String getAccessToken(String corpid, String appSecret) throws WeixinException {
        String access_token = "";
        try {
            if (StringUtils.isEmpty(corpid) || StringUtils.isEmpty(appSecret)) { throw new WeixinException("无效的appid或appsecret。"); }
            // 由于对接口有频率限制，所以在一次请求后，在过期时间内，不在进行第二次请求
            String key = ACCESS_TOKEN + corpid + appSecret;
            access_token = JedisUtils.get(key);
            if (access_token == null || "".equals(access_token)) {
                // token过期，重新获取
                AccessToken accessToken = refreshAccessToken(corpid, appSecret);
                //有效期为 7200 秒
                access_token = accessToken.getAccess_token();
                JedisUtils.set(key, accessToken.getAccess_token(), 6200);
            }
            return access_token;
        } catch (Exception e) {
            logger.error("获取access_token异常：,{}",e);
        }
        return access_token;
    }

    /**
     * 强制刷新微信服务凭证
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public static synchronized AccessToken refreshAccessToken(String appId, String appSecret) throws WeixinException {
        String url = String.format(ACCESS_TOKEN_URL, appId, appSecret);

        AccessToken accessToken = new AccessToken();
        for (int i = 0; i < RETRY_COUNT; i++) {
            try {
                accessToken = JSON.parseObject(WxHttpUtils.fetch(url), AccessToken.class);
                if (!accessToken.isAvailable()) {
                    break;
                }
            } catch (Exception e) {
                // 网络出错自动重试，此异常不许处理
                logger.error("网络出错自动重试，此异常不许处理",e);
            }
        }

        // 若access_token的返回码为0，则刷新缓存信息，否则就抛出获取access_token失败的异常信息
        int errCode = accessToken.getErrcode();
        if (0 != errCode) {
            throw new WeixinException(WxSupport.getCause(errCode));
        } else {
//            AccessTokenCache.tokenMap.put(appId, accessToken);
            //把token放入redis中,有效期为 7200 秒
            JedisUtils.set(ACCESS_TOKEN + appId, accessToken.getAccess_token(), 7200);
        }

        return accessToken;
    }
}
