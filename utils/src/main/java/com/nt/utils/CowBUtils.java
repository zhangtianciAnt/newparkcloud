package com.nt.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class CowBUtils {

    @Value("${h5s.ip}")
    private String h5sUrl;

    @Value("${h5s.user}")
    private String h5sUser;

    @Value("${h5s.password}")
    private String h5sPassword;

    /**
     * 用于判断一个字符串中，是否包含多个字符串中的任意一个（例如：aabbccddeeffgg中是否包含aa,bb,cc其中任意一个，如果包含则返回true，否则false）
     *
     * @param inputString 进行判断的字符串
     * @param words       是否包含的字符串数组
     * @return true 包含，false 不包含
     */
    public static boolean wordsIndexOf(String inputString, String[] words) {
        boolean result = false;
        for (String w : words) {
            if (inputString.indexOf(w) > -1) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 用于判断一个字符串，是否与数组字符串中的任意一个相等
     *
     * @param inputString 进行判断的字符串
     * @param words       字符串数组
     * @return true 有相同，false 没有相同
     */
    public static boolean wordsEqualOf(String inputString, String[] words) {
        boolean result = false;
        for (String w : words) {
            if (inputString.equals(w)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 获取h5s session
     */
    public String getH5sSession() {
        String session = "";
        // 获取session请求地址
        String url = h5sUrl + "/api/v1/Login";
        // 设置请求参数
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user", h5sUser);
        paramMap.put("password", h5sPassword);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.get(url, paramMap));
        if (jsonObject.get("bStatus").equals(true)) {
            session = jsonObject.get("strSession").toString();
        }
        return session;
    }

    /**
     * 向h5s conf 中添加摄像头信息
     *
     * @param token    h5s conf token 唯一值
     * @param user     摄像头用户名
     * @param password 摄像头密码
     * @param url      摄像头流rtsp地址（需要encode编码）
     * @param session  h5s 获取到的session
     * @return
     */
    public String insertH5sInfomation(String token, String user, String password, String url, String session) {
        String strCode = "";
        String insertUrl = h5sUrl + "/api/v1/AddSrcRTSP";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("token", token);
        paramMap.put("user", user);
        paramMap.put("password", password);
        paramMap.put("url", url);
        paramMap.put("session", session);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.get(insertUrl, paramMap));
        if (jsonObject.get("bStatus").equals(true)) {
            strCode = jsonObject.get("strCode").toString();
        }
        return strCode;
    }
}
