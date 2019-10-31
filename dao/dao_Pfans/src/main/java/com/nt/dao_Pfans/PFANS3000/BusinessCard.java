package com.nt.dao_Pfans.PFANS3000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;



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

}
