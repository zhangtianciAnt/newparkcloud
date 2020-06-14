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
     * 用户id
     */
    private String userid;
    /**
     * 打卡规则名称
     */
    private String groupname;
    /**
     * 打卡类型。字符串，目前有：上班打卡，下班打卡，外出打卡
     */
    private String checkin_type;
    /**
     * 异常类型，字符串，包括：时间异常，地点异常，未打卡，wifi异常，非常用设备。如果有多个异常，以分号间隔
     */
    private String exception_type;
    /**
     * 打卡时间。Unix时间戳
     */
    private int checkin_time;
    /**
     * 打卡地点title
     */
    private String location_title;
    /**
     * 打卡地点详情
     */
    private String location_detail;
    /**
     * 打卡wifi名称
     */
    private String wifiname;
    /**
     * 打卡备注
     */
    private String notes;
    /**
     * 打卡的MAC地址/bssid
     */
    private String wifimac;
    /**
     * 打卡的附件media_id，可使用media/get获取附件
     */
    private String mediaids;
    /**
     * 位置打卡地点纬度，是实际纬度的1000000倍，与腾讯地图一致采用GCJ-02坐标系统标准
     */
    private int lat;
    /**
     * 位置打卡地点经度，是实际经度的1000000倍，与腾讯地图一致采用GCJ-02坐标系统标准
     */
    private int lng;
    /**
     * 打卡设备id
     */
    private String deviceid;

    //阿里fastjson.JSON插件
    public static EWxCheckData fromJson(String json) {
        return JSON.parseObject(json, EWxCheckData.class);
    }
}

