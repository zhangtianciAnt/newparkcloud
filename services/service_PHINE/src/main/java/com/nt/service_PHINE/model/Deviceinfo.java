package com.nt.service_PHINE.model;

import com.nt.utils.dao.BaseModel;
import javax.persistence.*;

public class Deviceinfo extends BaseModel {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 添加时指定，不可重复
     */
    private String deviceid;

    /**
     * 业务/时钟
     */
    private String devicetype;

    /**
     * 所属机房编号
     */
    private String machineroomid;

    private String currentuser;

    private String companyid;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取添加时指定，不可重复
     *
     * @return deviceid - 添加时指定，不可重复
     */
    public String getDeviceid() {
        return deviceid;
    }

    /**
     * 设置添加时指定，不可重复
     *
     * @param deviceid 添加时指定，不可重复
     */
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    /**
     * 获取业务/时钟
     *
     * @return devicetype - 业务/时钟
     */
    public String getDevicetype() {
        return devicetype;
    }

    /**
     * 设置业务/时钟
     *
     * @param devicetype 业务/时钟
     */
    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    /**
     * 获取所属机房编号
     *
     * @return machineroomid - 所属机房编号
     */
    public String getMachineroomid() {
        return machineroomid;
    }

    /**
     * 设置所属机房编号
     *
     * @param machineroomid 所属机房编号
     */
    public void setMachineroomid(String machineroomid) {
        this.machineroomid = machineroomid;
    }

    /**
     * @return currentuser
     */
    public String getCurrentuser() {
        return currentuser;
    }

    /**
     * @param currentuser
     */
    public void setCurrentuser(String currentuser) {
        this.currentuser = currentuser;
    }

    /**
     * @return companyid
     */
    public String getCompanyid() {
        return companyid;
    }

    /**
     * @param companyid
     */
    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    /**
     * 获取备注
     *
     * @return remarks - 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}