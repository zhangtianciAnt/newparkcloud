package com.nt.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nt.utils.dao.EWeixinOauth2Token;
import com.nt.utils.dao.EWxBaseResponse;
import com.nt.utils.dao.WxEnterpriseUser;
import com.nt.utils.dao.WxUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业微信平台接口的直接实现工具类.
 *
 * @author YANGSHUBO
 */
public class EWxUserApi {

    private static Logger logger = LoggerFactory.getLogger(EWxUserApi.class);

    // 获取企业微信access_token
    public final static String EWECHAT_SERVICE_ACCESS_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ww5c49fd2e4bbc6981&corpsecret=h-kQQjJMqhxYk9OmzPRXtwl0JsGRDS2pNx9erhv2Vw8";

    // 获取企业微信打卡数据
    public final static String EWECHAT_SERVICE_CHECKINDATA = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=B0q6k-j-JwLLzyTP7TFktaO3TksYFu3DNwC0TayXwScA_6anZ-0gqV2IMOsmstPgzzZ2-0tEEtRUnoz2UHixcpEUr4aYCF83f5ZDrQw1HA2KK3fTDemsrUDUDJnPRHEfTQbZp-ZPXI00PM0MP_sPS5JLRZZETqCIKytWudgNCFXuXVMqNDUAzQpo6Ohqoddrgf1cGSWuZyLFW-Od-3MG1w";

    /**
     * 获取企业微信access_token
     *
     * @param corpid     企业ID
     * @param corpsecret 应用的凭证密钥
     * @return access_token值
     */
    public static EWeixinOauth2Token getWeChatOauth2Token(String corpid, String corpsecret) {
        String responseContent = null;
        try {
            String accessToken = WxBaseApi.getAccessToken(corpid, corpsecret);

            String EWECHAT_SERVICE_ACCESS_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret;

            String requestUrl = String.format(EWECHAT_SERVICE_ACCESS_TOKEN, accessToken);

            responseContent = WxHttpUtils.httpsRequest(requestUrl, "GET");

            if (logger.isInfoEnabled()) {
                logger.info("获取登录凭证：" + responseContent);
            }
        } catch (Exception e) {
            logger.error("获取登录凭证API调用失败：", e);
        }
        return EWeixinOauth2Token.fromJson(responseContent);

    }

    /**
     * 获取企业微信打卡数据
     *
     * @param access_Token        接口凭证token
     * @param openCheckInDataType 打卡类型
     * @param startTime           开始时间
     * @param endTime             结束时间
     * @param userIdList          用户列表
     * @return access_token值
     */
    public static EWxBaseResponse inData(String access_Token, int openCheckInDataType, long startTime, long endTime, String[] userIdList) {

        String responseContent = null;

        JSONObject jsob = new JSONObject();
        jsob.put("opencheckindatatype", openCheckInDataType);
        jsob.put("starttime", startTime);
        jsob.put("endtime", endTime);
//        jsob.put("useridlist", new String[]{"409a81d084aa2ebb7671684177e45342", "YangShuBo", "lifeline"});
        jsob.put("useridlist", userIdList);
        String json = JSON.toJSONString(jsob);

        try {
//            String accessToken = WxBaseApi.getAccessToken(access_Token, openCheckInDataType);
            String SERVICE_CHECKINDATA = "https://qyapi.weixin.qq.com/cgi-bin/checkin/getcheckindata?access_token=" + access_Token;
            String requestUrl = String.format(SERVICE_CHECKINDATA, access_Token);
            responseContent = WxHttpUtils.httpsRequestWithJson(requestUrl, "POST", json);
            if (logger.isInfoEnabled()) {
                logger.info("获取打卡数据返回信息：" + responseContent);
            }
        } catch (
                Exception e) {
            logger.error("获取打卡数据调用失败：", e);
        }
        return EWxBaseResponse.fromJson(responseContent);
    }

}

