package com.nt.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    private static String h5sSession = "";

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
     *
     * @return 返回""说明获取session失败，有值说明成功
     */
    public String getH5sSession() {
        String session = "";
        // 如果静态变量中有已经保活了的session，就使用已经保活了的session,否则重新向h5s获取。
        if (!"".equals(h5sSession)) {
            session = h5sSession;
        } else {
            // 获取session请求地址
            String url = h5sUrl + "/api/v1/Login";
            // 设置请求参数
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("user", h5sUser);
            paramMap.put("password", h5sPassword);
            JSONObject jsonObject = JSONObject.parseObject(HttpUtil.get(url, paramMap));
            if (jsonObject.get("bStatus").equals(true)) {
                session = jsonObject.get("strSession").toString();
                // 保活
                h5sKeepalive(session);
                // 向静态变量中存放session
                h5sSession = session;
            }
        }
        return session;
    }

    /**
     * h5s session 保活，保活后只要h5s服务不挂，session就一直有效
     *
     * @param h5sSession h5s 登录后 session
     * @return 返回""说明保活失败，Keepalive successfully保活成功
     */
    public String h5sKeepalive(String h5sSession) {
        String result = "";
        String url = h5sUrl + "/api/v1/Keepalive";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("session", h5sSession);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.get(url, paramMap));
        if (jsonObject.get("bStatus").equals(true)) {
            result = jsonObject.get("strCode").toString();
        }
        return result;
    }

    /**
     * 向h5s conf 中添加摄像头信息
     *
     * @param token    h5s conf token 唯一值
     * @param user     摄像头用户名
     * @param password 摄像头密码
     * @param url      摄像头流rtsp地址（需要encode编码）
     * @return String（Add successfully表示添加成功）
     */
    public String insertH5sInfomation(String token, String user, String password, String url) {
        String strCode = "";
        String insertUrl = h5sUrl + "/api/v1/AddSrcRTSP";
        // 获取h5s session
        // String session = getH5sSession();
        // 请求参数
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "RTSP");
        paramMap.put("token", token);
        paramMap.put("user", user);
        paramMap.put("password", password);
        paramMap.put("url", url);
        paramMap.put("audio", "true");
        // paramMap.put("session", "");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.get(insertUrl, paramMap));
        if (jsonObject.get("bStatus").equals(true)) {
            strCode = jsonObject.get("strCode").toString();
        } else {
            // TODO: 2020/8/20 插入失败异常 （如果摄像头编号重复 就会插入失败异常）
            //  可以在保存设备的时候，添加设备编号不允许重复判断来解决
        }
        return strCode;
    }

    /**
     * 删除h5s conf 中摄像头信息
     *
     * @param token
     * @return
     */
    public String delH5sInformation(String token) {
        String strCode = "";
        String delUrl = h5sUrl + "/api/v1/DelSrc";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("token", token);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.get(delUrl, paramMap, 20000));
        if (jsonObject.get("bStatus").equals(true)) {
            strCode = jsonObject.get("strCode").toString();
        } else {
            // TODO: 2020/8/20 删除失败异常 （如果摄像头编号在h5s conf里找不到，就回删除异常）
            //  插入添加设备编号不允许重复判断，并且页面添加loading不允许重复提交后，应该可以保证不会出现conf里找不到对应token的情况
        }
        return strCode;
    }

    /**
     * url encode 编码
     *
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, "UTF-8");
    }


}
