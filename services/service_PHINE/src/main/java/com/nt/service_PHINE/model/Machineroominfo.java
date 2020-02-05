package com.nt.service_PHINE.model;

import com.nt.utils.dao.BaseModel;
import javax.persistence.*;

public class Machineroominfo extends BaseModel {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 机房编号&机柜编号
     */
    private String machineroomid;

    /**
     * 机房名
     */
    private String machineroomname;

    /**
     * 机房地址
     */
    private String machineroomaddress;

    /**
     * 机房负责人ID
     */
    private String machineroommanagerid;

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
     * 获取机房编号&机柜编号
     *
     * @return machineroomid - 机房编号&机柜编号
     */
    public String getMachineroomid() {
        return machineroomid;
    }

    /**
     * 设置机房编号&机柜编号
     *
     * @param machineroomid 机房编号&机柜编号
     */
    public void setMachineroomid(String machineroomid) {
        this.machineroomid = machineroomid;
    }

    /**
     * 获取机房名
     *
     * @return machineroomname - 机房名
     */
    public String getMachineroomname() {
        return machineroomname;
    }

    /**
     * 设置机房名
     *
     * @param machineroomname 机房名
     */
    public void setMachineroomname(String machineroomname) {
        this.machineroomname = machineroomname;
    }

    /**
     * 获取机房地址
     *
     * @return machineroomaddress - 机房地址
     */
    public String getMachineroomaddress() {
        return machineroomaddress;
    }

    /**
     * 设置机房地址
     *
     * @param machineroomaddress 机房地址
     */
    public void setMachineroomaddress(String machineroomaddress) {
        this.machineroomaddress = machineroomaddress;
    }

    /**
     * 获取机房负责人ID
     *
     * @return machineroommanagerid - 机房负责人ID
     */
    public String getMachineroommanagerid() {
        return machineroommanagerid;
    }

    /**
     * 设置机房负责人ID
     *
     * @param machineroommanagerid 机房负责人ID
     */
    public void setMachineroommanagerid(String machineroommanagerid) {
        this.machineroommanagerid = machineroommanagerid;
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