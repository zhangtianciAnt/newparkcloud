package com.nt.utils.dao;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 微信请求返回的基础状态数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EWxBaseResponse {

    private int opencheckindatatype; // 打卡类型。1：上下班打卡；2：外出打卡；3：全部打卡
    private int starttime; // 获取打卡记录的开始时间。Unix时间戳
    private int endtime; // 获取打卡记录的结束时间。Unix时间戳
    private String useridlist;  // 需要获取打卡记录的用户列表

    //用户ID
    private int errcode;
    //事假请假天数
    private String errmsg;

    // region 租户信息
    private List<EWxCheckData> checkindata;

    //阿里fastjson.JSON插件
    public static EWxBaseResponse fromJson(String json) {
        return JSON.parseObject(json, EWxBaseResponse.class);
    }
}

