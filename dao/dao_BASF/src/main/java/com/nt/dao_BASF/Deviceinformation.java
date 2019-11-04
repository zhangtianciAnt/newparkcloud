package com.nt.dao_BASF;

import java.util.Date;
import javax.persistence.*;

public class Deviceinformation {
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

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATEON")
    private Date createon;

    @Column(name = "CREATEBY")
    private String createby;

    @Column(name = "MODIFYON")
    private Date modifyon;

    @Column(name = "MODIFYBY")
    private String modifyby;

    @Column(name = "Owner")
    private String owner;

    @Column(name = "TENANTID")
    private String tenantid;

    /**
     * @return deviceinformationid
     */
    public String getDeviceinformationid() {
        return deviceinformationid;
    }

    /**
     * @param deviceinformationid
     */
    public void setDeviceinformationid(String deviceinformationid) {
        this.deviceinformationid = deviceinformationid;
    }

    /**
     * 获取设备编号

     *
     * @return deviceno - 设备编号

     */
    public String getDeviceno() {
        return deviceno;
    }

    /**
     * 设置设备编号

     *
     * @param deviceno 设备编号

     */
    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    /**
     * 获取设备分类

     *
     * @return devicetype - 设备分类

     */
    public String getDevicetype() {
        return devicetype;
    }

    /**
     * 设置设备分类

     *
     * @param devicetype 设备分类

     */
    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    /**
     * 获取设备名称

     *
     * @return devicename - 设备名称

     */
    public String getDevicename() {
        return devicename;
    }

    /**
     * 设置设备名称

     *
     * @param devicename 设备名称

     */
    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    /**
     * 获取责任人
     *
     * @return personliable - 责任人
     */
    public String getPersonliable() {
        return personliable;
    }

    /**
     * 设置责任人
     *
     * @param personliable 责任人
     */
    public void setPersonliable(String personliable) {
        this.personliable = personliable;
    }

    /**
     * 获取所在区域

     *
     * @return region - 所在区域

     */
    public String getRegion() {
        return region;
    }

    /**
     * 设置所在区域

     *
     * @param region 所在区域

     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 获取所在防区

     *
     * @return defencearea - 所在防区

     */
    public String getDefencearea() {
        return defencearea;
    }

    /**
     * 设置所在防区

     *
     * @param defencearea 所在防区

     */
    public void setDefencearea(String defencearea) {
        this.defencearea = defencearea;
    }

    /**
     * 获取所在装置

     *
     * @return device - 所在装置

     */
    public String getDevice() {
        return device;
    }

    /**
     * 设置所在装置

     *
     * @param device 所在装置

     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * 获取所在楼层

     *
     * @return floor - 所在楼层

     */
    public String getFloor() {
        return floor;
    }

    /**
     * 设置所在楼层

     *
     * @param floor 所在楼层

     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * 获取设备所属系统

     *
     * @return subordinatesystem - 设备所属系统

     */
    public String getSubordinatesystem() {
        return subordinatesystem;
    }

    /**
     * 设置设备所属系统

     *
     * @param subordinatesystem 设备所属系统

     */
    public void setSubordinatesystem(String subordinatesystem) {
        this.subordinatesystem = subordinatesystem;
    }

    /**
     * 获取详细位置

     *
     * @return detailedlocation - 详细位置

     */
    public String getDetailedlocation() {
        return detailedlocation;
    }

    /**
     * 设置详细位置

     *
     * @param detailedlocation 详细位置

     */
    public void setDetailedlocation(String detailedlocation) {
        this.detailedlocation = detailedlocation;
    }

    /**
     * 获取GIS坐标

     *
     * @return GIS - GIS坐标

     */
    public String getGis() {
        return gis;
    }

    /**
     * 设置GIS坐标

     *
     * @param gis GIS坐标

     */
    public void setGis(String gis) {
        this.gis = gis;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return STATUS
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return CREATEON
     */
    public Date getCreateon() {
        return createon;
    }

    /**
     * @param createon
     */
    public void setCreateon(Date createon) {
        this.createon = createon;
    }

    /**
     * @return CREATEBY
     */
    public String getCreateby() {
        return createby;
    }

    /**
     * @param createby
     */
    public void setCreateby(String createby) {
        this.createby = createby;
    }

    /**
     * @return MODIFYON
     */
    public Date getModifyon() {
        return modifyon;
    }

    /**
     * @param modifyon
     */
    public void setModifyon(Date modifyon) {
        this.modifyon = modifyon;
    }

    /**
     * @return MODIFYBY
     */
    public String getModifyby() {
        return modifyby;
    }

    /**
     * @param modifyby
     */
    public void setModifyby(String modifyby) {
        this.modifyby = modifyby;
    }

    /**
     * @return Owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return TENANTID
     */
    public String getTenantid() {
        return tenantid;
    }

    /**
     * @param tenantid
     */
    public void setTenantid(String tenantid) {
        this.tenantid = tenantid;
    }
}