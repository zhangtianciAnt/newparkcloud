package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deviceinformation")
public class Deviceinformation extends BaseModel {
    @Id
    private String deviceinformationid;

    /**
     * 设备编号

     */
    private String deviceno;

    /**
     * 设备分类

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
     * GIS坐标

     */
    @Column(name = "GIS")
    private String gis;

    private String remark;
}
