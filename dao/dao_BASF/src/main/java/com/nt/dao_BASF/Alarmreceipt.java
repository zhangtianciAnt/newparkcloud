package com.nt.dao_BASF;

import java.util.Date;
import javax.persistence.*;

public class Alarmreceipt {
    @Id
    private String alarmreceiptid;

    /**
     * 接警单编号

     */
    private String alarmreceiptno;

    /**
     * 接警单类别

     */
    private String alarmreceipttype;

    /**
     * 报警时间

     */
    private Date alarmtime;

    /**
     * 报警级别

     */
    private String alarmlevel;

    /**
     * 报警指标名称

     */
    private String alarmname;

    /**
     * 监测值

     */
    private String detectionvalue;

    /**
     * 详细位置描述

     */
    private String detailedlocation;

    /**
     * 监测设备编号

     */
    private String devicename;

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
     * @return alarmreceiptid
     */
    public String getAlarmreceiptid() {
        return alarmreceiptid;
    }

    /**
     * @param alarmreceiptid
     */
    public void setAlarmreceiptid(String alarmreceiptid) {
        this.alarmreceiptid = alarmreceiptid;
    }

    /**
     * 获取接警单编号

     *
     * @return alarmreceiptno - 接警单编号

     */
    public String getAlarmreceiptno() {
        return alarmreceiptno;
    }

    /**
     * 设置接警单编号

     *
     * @param alarmreceiptno 接警单编号

     */
    public void setAlarmreceiptno(String alarmreceiptno) {
        this.alarmreceiptno = alarmreceiptno;
    }

    /**
     * 获取接警单类别

     *
     * @return alarmreceipttype - 接警单类别

     */
    public String getAlarmreceipttype() {
        return alarmreceipttype;
    }

    /**
     * 设置接警单类别

     *
     * @param alarmreceipttype 接警单类别

     */
    public void setAlarmreceipttype(String alarmreceipttype) {
        this.alarmreceipttype = alarmreceipttype;
    }

    /**
     * 获取报警时间

     *
     * @return alarmtime - 报警时间

     */
    public Date getAlarmtime() {
        return alarmtime;
    }

    /**
     * 设置报警时间

     *
     * @param alarmtime 报警时间

     */
    public void setAlarmtime(Date alarmtime) {
        this.alarmtime = alarmtime;
    }

    /**
     * 获取报警级别

     *
     * @return alarmlevel - 报警级别

     */
    public String getAlarmlevel() {
        return alarmlevel;
    }

    /**
     * 设置报警级别

     *
     * @param alarmlevel 报警级别

     */
    public void setAlarmlevel(String alarmlevel) {
        this.alarmlevel = alarmlevel;
    }

    /**
     * 获取报警指标名称

     *
     * @return alarmname - 报警指标名称

     */
    public String getAlarmname() {
        return alarmname;
    }

    /**
     * 设置报警指标名称

     *
     * @param alarmname 报警指标名称

     */
    public void setAlarmname(String alarmname) {
        this.alarmname = alarmname;
    }

    /**
     * 获取监测值

     *
     * @return detectionvalue - 监测值

     */
    public String getDetectionvalue() {
        return detectionvalue;
    }

    /**
     * 设置监测值

     *
     * @param detectionvalue 监测值

     */
    public void setDetectionvalue(String detectionvalue) {
        this.detectionvalue = detectionvalue;
    }

    /**
     * 获取详细位置描述

     *
     * @return detailedlocation - 详细位置描述

     */
    public String getDetailedlocation() {
        return detailedlocation;
    }

    /**
     * 设置详细位置描述

     *
     * @param detailedlocation 详细位置描述

     */
    public void setDetailedlocation(String detailedlocation) {
        this.detailedlocation = detailedlocation;
    }

    /**
     * 获取监测设备编号

     *
     * @return devicename - 监测设备编号

     */
    public String getDevicename() {
        return devicename;
    }

    /**
     * 设置监测设备编号

     *
     * @param devicename 监测设备编号

     */
    public void setDevicename(String devicename) {
        this.devicename = devicename;
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