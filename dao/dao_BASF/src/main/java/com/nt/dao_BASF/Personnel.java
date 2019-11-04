package com.nt.dao_BASF;

import java.util.Date;
import javax.persistence.*;

public class Personnel {
    @Id
    private String personnelid;

    /**
     * 姓名

     */
    private String name;

    /**
     * 性别

     */
    private String sex;

    /**
     * 手机号码

     */
    private String phone;

    /**
     * 证件类型

     */
    private String certificatestype;

    /**
     * 证件号码

     */
    private String certificatesno;

    /**
     * 用户组

     */
    private String groupid;

    /**
     * 岗位

     */
    private String post;

    /**
     * 人员ID

     */
    private String persionid;

    /**
     * 门禁卡ID

     */
    private String cardid;

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
     * @return personnelid
     */
    public String getPersonnelid() {
        return personnelid;
    }

    /**
     * @param personnelid
     */
    public void setPersonnelid(String personnelid) {
        this.personnelid = personnelid;
    }

    /**
     * 获取姓名

     *
     * @return name - 姓名

     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名

     *
     * @param name 姓名

     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取性别

     *
     * @return sex - 性别

     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别

     *
     * @param sex 性别

     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取手机号码

     *
     * @return phone - 手机号码

     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号码

     *
     * @param phone 手机号码

     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取证件类型

     *
     * @return certificatestype - 证件类型

     */
    public String getCertificatestype() {
        return certificatestype;
    }

    /**
     * 设置证件类型

     *
     * @param certificatestype 证件类型

     */
    public void setCertificatestype(String certificatestype) {
        this.certificatestype = certificatestype;
    }

    /**
     * 获取证件号码

     *
     * @return certificatesno - 证件号码

     */
    public String getCertificatesno() {
        return certificatesno;
    }

    /**
     * 设置证件号码

     *
     * @param certificatesno 证件号码

     */
    public void setCertificatesno(String certificatesno) {
        this.certificatesno = certificatesno;
    }

    /**
     * 获取用户组

     *
     * @return groupid - 用户组

     */
    public String getGroupid() {
        return groupid;
    }

    /**
     * 设置用户组

     *
     * @param groupid 用户组

     */
    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    /**
     * 获取岗位

     *
     * @return post - 岗位

     */
    public String getPost() {
        return post;
    }

    /**
     * 设置岗位

     *
     * @param post 岗位

     */
    public void setPost(String post) {
        this.post = post;
    }

    /**
     * 获取人员ID

     *
     * @return persionid - 人员ID

     */
    public String getPersionid() {
        return persionid;
    }

    /**
     * 设置人员ID

     *
     * @param persionid 人员ID

     */
    public void setPersionid(String persionid) {
        this.persionid = persionid;
    }

    /**
     * 获取门禁卡ID

     *
     * @return cardid - 门禁卡ID

     */
    public String getCardid() {
        return cardid;
    }

    /**
     * 设置门禁卡ID

     *
     * @param cardid 门禁卡ID

     */
    public void setCardid(String cardid) {
        this.cardid = cardid;
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