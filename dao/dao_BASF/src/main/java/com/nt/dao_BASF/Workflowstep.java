package com.nt.dao_BASF;

import java.util.Date;
import javax.persistence.*;

public class Workflowstep {
    /**
     * ID
     */
    @Id
    @Column(name = "WORKFLOWSTEPID")
    private String workflowstepid;

    /**
     * ID
     */
    @Column(name = "WORKFLOWNODEINSTANCEID")
    private String workflownodeinstanceid;

    /**
     * 结果(0-同意;1-退回;2-查看;3-循环同意;4-循环退回)
     */
    @Column(name = "RESULT")
    private String result;

    /**
     * 步骤名
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 用户ID
     */
    @Column(name = "ITEMID")
    private String itemid;

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
     * 备注
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 获取ID
     *
     * @return WORKFLOWSTEPID - ID
     */
    public String getWorkflowstepid() {
        return workflowstepid;
    }

    /**
     * 设置ID
     *
     * @param workflowstepid ID
     */
    public void setWorkflowstepid(String workflowstepid) {
        this.workflowstepid = workflowstepid;
    }

    /**
     * 获取ID
     *
     * @return WORKFLOWNODEINSTANCEID - ID
     */
    public String getWorkflownodeinstanceid() {
        return workflownodeinstanceid;
    }

    /**
     * 设置ID
     *
     * @param workflownodeinstanceid ID
     */
    public void setWorkflownodeinstanceid(String workflownodeinstanceid) {
        this.workflownodeinstanceid = workflownodeinstanceid;
    }

    /**
     * 获取结果(0-同意;1-退回;2-查看;3-循环同意;4-循环退回)
     *
     * @return RESULT - 结果(0-同意;1-退回;2-查看;3-循环同意;4-循环退回)
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置结果(0-同意;1-退回;2-查看;3-循环同意;4-循环退回)
     *
     * @param result 结果(0-同意;1-退回;2-查看;3-循环同意;4-循环退回)
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 获取步骤名
     *
     * @return NAME - 步骤名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置步骤名
     *
     * @param name 步骤名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取用户ID
     *
     * @return ITEMID - 用户ID
     */
    public String getItemid() {
        return itemid;
    }

    /**
     * 设置用户ID
     *
     * @param itemid 用户ID
     */
    public void setItemid(String itemid) {
        this.itemid = itemid;
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
     * 获取备注
     *
     * @return REMARK - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}