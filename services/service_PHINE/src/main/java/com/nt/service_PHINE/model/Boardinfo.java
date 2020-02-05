package com.nt.service_PHINE.model;

import com.nt.utils.dao.BaseModel;
import javax.persistence.*;

public class Boardinfo extends BaseModel {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 添加时指定，不可重复
     */
    private String boardid;

    /**
     * 板卡类型
     */
    private String boardtype;

    /**
     * 板卡IP地址
     */
    private String boardipaddress;

    /**
     * 所属设备编号
     */
    private String deviceid;

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
     * @return boardid - 添加时指定，不可重复
     */
    public String getBoardid() {
        return boardid;
    }

    /**
     * 设置添加时指定，不可重复
     *
     * @param boardid 添加时指定，不可重复
     */
    public void setBoardid(String boardid) {
        this.boardid = boardid;
    }

    /**
     * 获取板卡类型
     *
     * @return boardtype - 板卡类型
     */
    public String getBoardtype() {
        return boardtype;
    }

    /**
     * 设置板卡类型
     *
     * @param boardtype 板卡类型
     */
    public void setBoardtype(String boardtype) {
        this.boardtype = boardtype;
    }

    /**
     * 获取板卡IP地址
     *
     * @return boardipaddress - 板卡IP地址
     */
    public String getBoardipaddress() {
        return boardipaddress;
    }

    /**
     * 设置板卡IP地址
     *
     * @param boardipaddress 板卡IP地址
     */
    public void setBoardipaddress(String boardipaddress) {
        this.boardipaddress = boardipaddress;
    }

    /**
     * 获取所属设备编号
     *
     * @return deviceid - 所属设备编号
     */
    public String getDeviceid() {
        return deviceid;
    }

    /**
     * 设置所属设备编号
     *
     * @param deviceid 所属设备编号
     */
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
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