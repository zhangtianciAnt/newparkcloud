package com.nt.dao_BASF;

import java.util.Date;
import javax.persistence.*;

public class Dictionary {
    @Id
    private String dictionaryid;

    /**
     * 字典code

     */
    private String code;

    /**
     * 字典名称

     */
    private String value;

    /**
     * 字典分类

     */
    private String type;

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
     * @return dictionaryid
     */
    public String getDictionaryid() {
        return dictionaryid;
    }

    /**
     * @param dictionaryid
     */
    public void setDictionaryid(String dictionaryid) {
        this.dictionaryid = dictionaryid;
    }

    /**
     * 获取字典code

     *
     * @return code - 字典code

     */
    public String getCode() {
        return code;
    }

    /**
     * 设置字典code

     *
     * @param code 字典code

     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取字典名称

     *
     * @return value - 字典名称

     */
    public String getValue() {
        return value;
    }

    /**
     * 设置字典名称

     *
     * @param value 字典名称

     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取字典分类

     *
     * @return type - 字典分类

     */
    public String getType() {
        return type;
    }

    /**
     * 设置字典分类

     *
     * @param type 字典分类

     */
    public void setType(String type) {
        this.type = type;
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