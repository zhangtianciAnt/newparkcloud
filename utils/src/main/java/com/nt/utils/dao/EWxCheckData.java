package com.nt.utils.dao;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信请求返回的基础状态数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EWxCheckData {
    /**
     * 数据主键ID
     */
    private String userid;
    /**
     * 企业LOGO
     */
    private String groupname;
    /**
     * 统一社会信息代码
     */
    private String checkin_type;
    /**
     * 公司地址
     */
    private String exception_type;
    /**
     * 公司法人
     */
    private int checkin_time;
    /**
     * 公司执照
     */
    private String location_title;
    /**
     * 公司成立时间
     */
    private String location_detail;
    /**
     * 门户网址
     */
    private String wifiname;
    /**
     * 门户网址
     */
    private String notes;
    /**
     * 门户网址
     */
    private String wifimac;
    /**
     * 门户网址
     */
    private String mediaids;
    /**
     * 门户网址
     */
    private int lat;
    /**
     * 门户网址
     */
    private int lng;
    /**
     * 门户网址
     */
    private String deviceid;

    //阿里fastjson.JSON插件
    public static EWxCheckData fromJson(String json) {
        return JSON.parseObject(json, EWxCheckData.class);
    }
}

