package com.nt.service_PHINE.model;

import com.nt.utils.dao.BaseModel;
import javax.persistence.*;

public class Chipinfo extends BaseModel {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 添加时指定，不可重复
     */
    private String chipid;

    /**
     * VU440、KU115等
     */
    private String chiptype;

    /**
     * 所属板卡编号
     */
    private String boardid;

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
     * @return chipid - 添加时指定，不可重复
     */
    public String getChipid() {
        return chipid;
    }

    /**
     * 设置添加时指定，不可重复
     *
     * @param chipid 添加时指定，不可重复
     */
    public void setChipid(String chipid) {
        this.chipid = chipid;
    }

    /**
     * 获取VU440、KU115等
     *
     * @return chiptype - VU440、KU115等
     */
    public String getChiptype() {
        return chiptype;
    }

    /**
     * 设置VU440、KU115等
     *
     * @param chiptype VU440、KU115等
     */
    public void setChiptype(String chiptype) {
        this.chiptype = chiptype;
    }

    /**
     * 获取所属板卡编号
     *
     * @return boardid - 所属板卡编号
     */
    public String getBoardid() {
        return boardid;
    }

    /**
     * 设置所属板卡编号
     *
     * @param boardid 所属板卡编号
     */
    public void setBoardid(String boardid) {
        this.boardid = boardid;
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