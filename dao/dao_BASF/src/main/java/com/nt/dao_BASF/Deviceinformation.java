package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deviceinformation")
public class Deviceinformation extends BaseModel {
    @Id
    private String deviceinformationid;

    /**
     * 地图信息主键
     */
    private String mapid;

    /**
     * 设备编号
     */
    private String deviceno;

    /**
     * 设备小分类
     */
    private String devicetypesmall;

    /**
     * 设备大分类
     */
    private String devicetype;

    /**
     * 设备名称
     */
    private String devicename;

    /**
     * 责任人
     */
    private String personliable;

    /**
     * 所在区域
     */
    private String region;

    /**
     * 所在防区
     */
    private String defencearea;

    /**
     * 所在装置
     */
    private String device;

    /**
     * 所在楼层
     */
    private String floor;

    /**
     * 设备所属系统
     */
    private String subordinatesystem;

    /**
     * 详细位置
     */
    private String detailedlocation;

    /**
     * 设备状态
     */
    private String devicestatus;

    /**
     * GIS坐标
     */
    @Column(name = "GIS")
    private String gis;

    /**
     * 备注
     */
    private String remark;

    /**
     * 设备地址
     */
    private String ip;

    /**
     * 总厂/分厂名称
     */
    private String factoryname;

    /**
     * 回路号/寄存器地址
     */
    private String line;

    /**
     * 设备地址/寄存器位
     */
    private String row;



}
