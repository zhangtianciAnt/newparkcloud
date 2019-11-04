package com.nt.dao_BASF;

import java.util.Date;
import javax.persistence.*;

public class Emailmessage {
    /**
     * 邮件短息主键

     */
    @Id
    private String emailmesageid;

    /**
     * 模板名称

     */
    private String templatename;

    /**
     * 邮件/短信分类

     */
    private String templatetype;

    /**
     * 一级分类

     */
    private String firsttype;

    /**
     * 二级分类

     */
    private String secondtype;

    /**
     * 接收人

     */
    private String addressee;

    /**
     * 抄送人

     */
    private String copyperson;

    /**
     * 主题

     */
    private String theme;

    /**
     * 内容

     */
    private String content;

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
     * 获取邮件短息主键

     *
     * @return emailmesageid - 邮件短息主键

     */
    public String getEmailmesageid() {
        return emailmesageid;
    }

    /**
     * 设置邮件短息主键

     *
     * @param emailmesageid 邮件短息主键

     */
    public void setEmailmesageid(String emailmesageid) {
        this.emailmesageid = emailmesageid;
    }

    /**
     * 获取模板名称

     *
     * @return templatename - 模板名称

     */
    public String getTemplatename() {
        return templatename;
    }

    /**
     * 设置模板名称

     *
     * @param templatename 模板名称

     */
    public void setTemplatename(String templatename) {
        this.templatename = templatename;
    }

    /**
     * 获取邮件/短信分类

     *
     * @return templatetype - 邮件/短信分类

     */
    public String getTemplatetype() {
        return templatetype;
    }

    /**
     * 设置邮件/短信分类

     *
     * @param templatetype 邮件/短信分类

     */
    public void setTemplatetype(String templatetype) {
        this.templatetype = templatetype;
    }

    /**
     * 获取一级分类

     *
     * @return firsttype - 一级分类

     */
    public String getFirsttype() {
        return firsttype;
    }

    /**
     * 设置一级分类

     *
     * @param firsttype 一级分类

     */
    public void setFirsttype(String firsttype) {
        this.firsttype = firsttype;
    }

    /**
     * 获取二级分类

     *
     * @return secondtype - 二级分类

     */
    public String getSecondtype() {
        return secondtype;
    }

    /**
     * 设置二级分类

     *
     * @param secondtype 二级分类

     */
    public void setSecondtype(String secondtype) {
        this.secondtype = secondtype;
    }

    /**
     * 获取接收人

     *
     * @return addressee - 接收人

     */
    public String getAddressee() {
        return addressee;
    }

    /**
     * 设置接收人

     *
     * @param addressee 接收人

     */
    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    /**
     * 获取抄送人

     *
     * @return copyperson - 抄送人

     */
    public String getCopyperson() {
        return copyperson;
    }

    /**
     * 设置抄送人

     *
     * @param copyperson 抄送人

     */
    public void setCopyperson(String copyperson) {
        this.copyperson = copyperson;
    }

    /**
     * 获取主题

     *
     * @return theme - 主题

     */
    public String getTheme() {
        return theme;
    }

    /**
     * 设置主题

     *
     * @param theme 主题

     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * 获取内容

     *
     * @return content - 内容

     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容

     *
     * @param content 内容

     */
    public void setContent(String content) {
        this.content = content;
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