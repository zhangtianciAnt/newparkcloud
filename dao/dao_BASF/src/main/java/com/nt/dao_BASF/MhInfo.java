package com.nt.dao_BASF;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Transient;

@Data
public class MhInfo {
    @Transient
    private VehicleManagement vehicleManagement;
    //平台接收时间
    private String ComTime;
    //IMEI(gps编号)
    private String IMEI;
    //GPS纬度
    private Double Lat;
    //GPS经度
    private Double Lng;
    //百度纬度
    private Double BLat;
    //百度经度
    private Double BLng;
    //方向。0~360
    private Integer Dir;
    //速度，单位为千米/小时
    private Double Speed;
    //里程，单位千米
    private Double Mileage;
    //GSM信号强度
    private Integer GSM;
    //GPS卫星颗数
    private Integer GPS;
    //北斗卫星颗数
    private Integer BDS;
    //定位类型编码。数据范围：0,1,2,4,5,8。
    //0：未知；
    //1：GPS；
    //2：基站定位；
    //4：北斗定位；
    //5：GPS和北斗定位；
    //8：格洛纳斯
    private Integer LocateType;
    //运行状态
    //9：未使用。
    //1：行驶（在线）；
    //2：停止（在线）；
    //3：离线
    private Integer RunStatus;
    private MhAddr Addr;

    private String enable;
}
