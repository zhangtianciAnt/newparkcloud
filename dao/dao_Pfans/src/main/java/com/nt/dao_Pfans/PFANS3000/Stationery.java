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
@Table(name = "stationery")

public class Stationery extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 文房具领取申请ID
     */
    @Id
    @Column(name = "STATIONERY_ID")
    private String stationeryid;

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
     * 预算单位
     */
    @Column(name = "COMPANY")
    private String company;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATION_DATE")
    private Date applicationdate;

    /**
     * 类型
     */
    @Column(name = "TYPE")
    private String stationerytype;

    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 尺寸类型说明
     */
    @Column(name = "SIZE")
    private String size;

    /**
     * 数量
     */
    @Column(name = "NUMBERS")
    private String numbers;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

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
