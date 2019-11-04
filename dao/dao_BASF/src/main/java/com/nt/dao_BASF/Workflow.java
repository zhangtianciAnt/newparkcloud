package com.nt.dao_BASF;

import java.util.Date;
import javax.persistence.*;

public class Workflow {
    /**
     * ID
     */
    @Id
    @Column(name = "WORKFLOWID")
    private String workflowid;

    /**
     * 流程名
     */
    @Column(name = "WORKFLOWNAME")
    private String workflowname;

    /**
     * 所属表单ID
     */
    @Column(name = "FORMID")
    private String formid;

    /**
     * 流程属性
     */
    @Column(name = "WORKFLOWOPERATION")
    private String workflowoperation;

    @Column(name = "CODE")
    private String code;

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
     * 流程图
     */
    @Column(name = "WORKFLOWCONTENT")
    private String workflowcontent;

    /**
     * 流程说明
     */
    @Column(name = "WORKFLOWTEXT")
    private String workflowtext;

    /**
     * 获取ID
     *
     * @return WORKFLOWID - ID
     */
    public String getWorkflowid() {
        return workflowid;
    }

    /**
     * 设置ID
     *
     * @param workflowid ID
     */
    public void setWorkflowid(String workflowid) {
        this.workflowid = workflowid;
    }

    /**
     * 获取流程名
     *
     * @return WORKFLOWNAME - 流程名
     */
    public String getWorkflowname() {
        return workflowname;
    }

    /**
     * 设置流程名
     *
     * @param workflowname 流程名
     */
    public void setWorkflowname(String workflowname) {
        this.workflowname = workflowname;
    }

    /**
     * 获取所属表单ID
     *
     * @return FORMID - 所属表单ID
     */
    public String getFormid() {
        return formid;
    }

    /**
     * 设置所属表单ID
     *
     * @param formid 所属表单ID
     */
    public void setFormid(String formid) {
        this.formid = formid;
    }

    /**
     * 获取流程属性
     *
     * @return WORKFLOWOPERATION - 流程属性
     */
    public String getWorkflowoperation() {
        return workflowoperation;
    }

    /**
     * 设置流程属性
     *
     * @param workflowoperation 流程属性
     */
    public void setWorkflowoperation(String workflowoperation) {
        this.workflowoperation = workflowoperation;
    }

    /**
     * @return CODE
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
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

    /**
     * 获取流程图
     *
     * @return WORKFLOWCONTENT - 流程图
     */
    public String getWorkflowcontent() {
        return workflowcontent;
    }

    /**
     * 设置流程图
     *
     * @param workflowcontent 流程图
     */
    public void setWorkflowcontent(String workflowcontent) {
        this.workflowcontent = workflowcontent;
    }

    /**
     * 获取流程说明
     *
     * @return WORKFLOWTEXT - 流程说明
     */
    public String getWorkflowtext() {
        return workflowtext;
    }

    /**
     * 设置流程说明
     *
     * @param workflowtext 流程说明
     */
    public void setWorkflowtext(String workflowtext) {
        this.workflowtext = workflowtext;
    }
}