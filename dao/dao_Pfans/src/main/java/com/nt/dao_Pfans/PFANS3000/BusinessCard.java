package com.nt.dao_Pfans.PFANS3000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "businesscard")

public class BusinessCard extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 名片申请ID
     */
    @Id
    @Column(name = "BUSINESSCARD_ID")
    private String businesscardid;

    /**
     * 申请人ID
     */
    @Column(name = "USER_ID")
    private String userid;

    /**
     * 申请人ID(英)
     */
    @Column(name = "EUSER_ID")
    private String euser_id;

    /**
     * 部门(英)
     */
    @Column(name = "ECENTER")
    private String ecenter;

    /**
     * 所属グループID
     */
    @Column(name = "CENTER_ID")
    private String centerid;

    /**
     * 所属チームID
     */
    @Column(name = "GROUP_ID")
    private String groupid;

    /**
     * 所属センターID
     */
    @Column(name = "TEAM_ID")
    private String teamid;

    /**
     * 职称
     */
    @Column(name = "OCCUPATIONAL")
    private String occupational;

    /**
     * 职称(英)
     */
    @Column(name = "EOCCUPATIONAL")
    private String eoccupational;

    /**
     * 内线号
     */
    @Column(name = "INSIDELINE")
    private String insideline;

    /**
     * 邮箱地址
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 事業計画
     */
    @Column(name = "PLAN")
    private String plan;

    /**
     * 事业计划类型
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 分类类型
     */
    @Column(name = "CLASSIFICATION")
    private String classification;

    /**
     * 事业计划余额
     */
    @Column(name = "BALANCE")
    private String balance;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATIONDATE")
    private Date applicationdate;

    /**
     * 是否受理
     */
    @Column(name = "ACCEPT")
    private String accept;

    /**
     * 受理状态
     */
    @Column(name = "ACCEPTSTATUS")
    private String acceptstatus;

    /**
     * 完成日期
     */
    @Column(name = "FINDATE")
    private Date findate;

    /**
     * 拒绝理由
     */
    @Column(name = "REFUSEREASON")
    private String refusereason;
}
