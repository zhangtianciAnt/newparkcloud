package com.nt.dao_BASF;

import java.util.Date;
import javax.persistence.*;

public class Todonotice {
    /**
     * 主键
     */
    @Id
    @Column(name = "NOTICEID")
    private String noticeid;

    /**
     * 类型（1：待办，2：消息）
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 标题
     */
    @Column(name = "TITLE")
    private String title;

    /**
     * 数据ID
     */
    @Column(name = "DATAID")
    private String dataid;

    /**
     * 页面路径
     */
    @Column(name = "URL")
    private String url;

    @Column(name = "WORKFLOWURL")
    private String workflowurl;

    /**
     * 发起人
     */
    @Column(name = "INITIATOR")
    private String initiator;

    /**
     * 内容
     */
    @Column(name = "CONTENT")
    private String content;

    /**
     * 创建人
     */
    @Column(name = "CREATEBY")
    private String createby;

    /**
     * 创建时间
     */
    @Column(name = "CREATEON")
    private Date createon;

    /**
     * 更新人
     */
    @Column(name = "MODIFYBY")
    private String modifyby;

    /**
     * 更新时间
     */
    @Column(name = "MODIFYON")
    private Date modifyon;

    /**
     * 负责人
     */
    @Column(name = "OWNER")
    private String owner;

    /**
     * 状态
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 租户ID
     */
    @Column(name = "TENANTID")
    private String tenantid;

    /**
     * 获取主键
     *
     * @return NOTICEID - 主键
     */
    public String getNoticeid() {
        return noticeid;
    }

    /**
     * 设置主键
     *
     * @param noticeid 主键
     */
    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    /**
     * 获取类型（1：待办，2：消息）
     *
     * @return TYPE - 类型（1：待办，2：消息）
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型（1：待办，2：消息）
     *
     * @param type 类型（1：待办，2：消息）
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取标题
     *
     * @return TITLE - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取数据ID
     *
     * @return DATAID - 数据ID
     */
    public String getDataid() {
        return dataid;
    }

    /**
     * 设置数据ID
     *
     * @param dataid 数据ID
     */
    public void setDataid(String dataid) {
        this.dataid = dataid;
    }

    /**
     * 获取页面路径
     *
     * @return URL - 页面路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置页面路径
     *
     * @param url 页面路径
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return WORKFLOWURL
     */
    public String getWorkflowurl() {
        return workflowurl;
    }

    /**
     * @param workflowurl
     */
    public void setWorkflowurl(String workflowurl) {
        this.workflowurl = workflowurl;
    }

    /**
     * 获取发起人
     *
     * @return INITIATOR - 发起人
     */
    public String getInitiator() {
        return initiator;
    }

    /**
     * 设置发起人
     *
     * @param initiator 发起人
     */
    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    /**
     * 获取内容
     *
     * @return CONTENT - 内容
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
     * 获取创建人
     *
     * @return CREATEBY - 创建人
     */
    public String getCreateby() {
        return createby;
    }

    /**
     * 设置创建人
     *
     * @param createby 创建人
     */
    public void setCreateby(String createby) {
        this.createby = createby;
    }

    /**
     * 获取创建时间
     *
     * @return CREATEON - 创建时间
     */
    public Date getCreateon() {
        return createon;
    }

    /**
     * 设置创建时间
     *
     * @param createon 创建时间
     */
    public void setCreateon(Date createon) {
        this.createon = createon;
    }

    /**
     * 获取更新人
     *
     * @return MODIFYBY - 更新人
     */
    public String getModifyby() {
        return modifyby;
    }

    /**
     * 设置更新人
     *
     * @param modifyby 更新人
     */
    public void setModifyby(String modifyby) {
        this.modifyby = modifyby;
    }

    /**
     * 获取更新时间
     *
     * @return MODIFYON - 更新时间
     */
    public Date getModifyon() {
        return modifyon;
    }

    /**
     * 设置更新时间
     *
     * @param modifyon 更新时间
     */
    public void setModifyon(Date modifyon) {
        this.modifyon = modifyon;
    }

    /**
     * 获取负责人
     *
     * @return OWNER - 负责人
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 设置负责人
     *
     * @param owner 负责人
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 获取状态
     *
     * @return STATUS - 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取租户ID
     *
     * @return TENANTID - 租户ID
     */
    public String getTenantid() {
        return tenantid;
    }

    /**
     * 设置租户ID
     *
     * @param tenantid 租户ID
     */
    public void setTenantid(String tenantid) {
        this.tenantid = tenantid;
    }
}