package com.nt.dao_BASF;

import java.util.Date;
import javax.persistence.*;

public class Workflownodeinstance {
    /**
     * ID
     */
    @Id
    @Column(name = "WORKFLOWNODEINSTANCEID")
    private String workflownodeinstanceid;

    /**
     * ID
     */
    @Column(name = "WORKFLOWINSTANCEID")
    private String workflowinstanceid;

    /**
     * 节点名
     */
    @Column(name = "NODENAME")
    private String nodename;

    /**
     * 节点顺序
     */
    @Column(name = "NODEORD")
    private Integer nodeord;

    /**
     * 节点类型（1：确认，2：审批）
     */
    @Column(name = "NODETYPE")
    private String nodetype;

    /**
     * 节点人员类型（1：指定人，2：指定角色，3：上下级）
     */
    @Column(name = "NODEUSERTYPE")
    private String nodeusertype;

    /**
     * 审批人（1：用户ID，2：角色ID，3：无）
     */
    @Column(name = "ITEMID")
    private String itemid;

    @Column(name = "REMARKS")
    private String remarks;

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
     * 抄送人
     */
    @Column(name = "CC")
    private String cc;

    /**
     * 出口条件
     */
    @Column(name = "OUTCONDITION")
    private String outcondition;

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
     * 获取ID
     *
     * @return WORKFLOWINSTANCEID - ID
     */
    public String getWorkflowinstanceid() {
        return workflowinstanceid;
    }

    /**
     * 设置ID
     *
     * @param workflowinstanceid ID
     */
    public void setWorkflowinstanceid(String workflowinstanceid) {
        this.workflowinstanceid = workflowinstanceid;
    }

    /**
     * 获取节点名
     *
     * @return NODENAME - 节点名
     */
    public String getNodename() {
        return nodename;
    }

    /**
     * 设置节点名
     *
     * @param nodename 节点名
     */
    public void setNodename(String nodename) {
        this.nodename = nodename;
    }

    /**
     * 获取节点顺序
     *
     * @return NODEORD - 节点顺序
     */
    public Integer getNodeord() {
        return nodeord;
    }

    /**
     * 设置节点顺序
     *
     * @param nodeord 节点顺序
     */
    public void setNodeord(Integer nodeord) {
        this.nodeord = nodeord;
    }

    /**
     * 获取节点类型（1：确认，2：审批）
     *
     * @return NODETYPE - 节点类型（1：确认，2：审批）
     */
    public String getNodetype() {
        return nodetype;
    }

    /**
     * 设置节点类型（1：确认，2：审批）
     *
     * @param nodetype 节点类型（1：确认，2：审批）
     */
    public void setNodetype(String nodetype) {
        this.nodetype = nodetype;
    }

    /**
     * 获取节点人员类型（1：指定人，2：指定角色，3：上下级）
     *
     * @return NODEUSERTYPE - 节点人员类型（1：指定人，2：指定角色，3：上下级）
     */
    public String getNodeusertype() {
        return nodeusertype;
    }

    /**
     * 设置节点人员类型（1：指定人，2：指定角色，3：上下级）
     *
     * @param nodeusertype 节点人员类型（1：指定人，2：指定角色，3：上下级）
     */
    public void setNodeusertype(String nodeusertype) {
        this.nodeusertype = nodeusertype;
    }

    /**
     * 获取审批人（1：用户ID，2：角色ID，3：无）
     *
     * @return ITEMID - 审批人（1：用户ID，2：角色ID，3：无）
     */
    public String getItemid() {
        return itemid;
    }

    /**
     * 设置审批人（1：用户ID，2：角色ID，3：无）
     *
     * @param itemid 审批人（1：用户ID，2：角色ID，3：无）
     */
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    /**
     * @return REMARKS
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
     * 获取抄送人
     *
     * @return CC - 抄送人
     */
    public String getCc() {
        return cc;
    }

    /**
     * 设置抄送人
     *
     * @param cc 抄送人
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * 获取出口条件
     *
     * @return OUTCONDITION - 出口条件
     */
    public String getOutcondition() {
        return outcondition;
    }

    /**
     * 设置出口条件
     *
     * @param outcondition 出口条件
     */
    public void setOutcondition(String outcondition) {
        this.outcondition = outcondition;
    }
}