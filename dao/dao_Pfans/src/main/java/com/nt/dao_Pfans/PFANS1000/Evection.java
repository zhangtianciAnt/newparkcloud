package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "evection")

public class Evection extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 境内/外出差精算ID
     */
    @Id
    @Column(name = "EVECTION_ID")
    private String evectionid;

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
     * 内线
     */
    @Column(name = "TELEPHONE")
    private String telephone;

    /**
     * 出差地点
     */
    @Column(name = "PLACE")
    private String place;

    /**
     * 出差开始日期
     */
    @Column(name = "STARTDATE")
    private Date startdate;

    /**
     * 出差结束日期
     */
    @Column(name = "ENDDATE")
    private Date enddate;

    /**
     * 出差日数
     */
    @Column(name = "DATENUMBER")
    private String datenumber;

    /**
     * 预算单位
     */
    @Column(name = "BUDGETUNIT")
    private String budgetunit;

    /**
     * 借款总额
     */
    @Column(name = "LOANAMOUNT")
    private String loanamount;

    /**
     * 支出总额
     */
    @Column(name = "TOTALPAY")
    private Date totalpay;

    /**
     * 结余
     */
    @Column(name = "BALANCE")
    private String balance;

    /**
     * 外币总额
     */
    @Column(name = "TOTALCURRENCY")
    private String totalcurrency;

    /**
     * 外币金额
     */
    @Column(name = "FOREIGNCURRENCY")
    private String foreigncurrency;

    /**
     * 摘要
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 区分
     */
    @Column(name = "TYPE")
    private Date type;


}
