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
@Table(name = "communication")
public class LoanApplication extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *暂借款申请单ID
     */
    @Id
    @Column(name = "LOANAPPLICATION_ID")
    private String loanapplication_id;

    /**
     * 所属センター
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 所属グループ
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 所属チーム
     */
    @Column(name = "TEAM_ID")
    private String team_id;


    /**
     * 申请人
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * 内线电话
     */
    @Column(name = "TELEPHONE")
    private String telephone;

    /**
     * 预算单位
     */
    @Column(name = "BUDGETUNIT")
    private String budgetunit;

    /**
     * 模块
     */
    @Column(name = "MODULEID")
    private Date moduleid;

    /**
     * 发票/日记账编号
     */
    @Column(name = "ACCOUNTNUMBER")
    private String accountnumber;

    /**
     * 预计报销
     */
    @Column(name = "REIMBURSEMENT")
    private String reimbursement;

    /**
     * 货币选择
     */
    @Column(name = "CURRENCYCHOICE")
    private String currencychoice;

    /**
     * 金额
     */
    @Column(name = "MONEYS")
    private String moneys;

    /**
     * 摘要
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 付款方式
     */
    @Column(name = "PAYMENTMETHOD")
    private String paymentmethod;

    /**
     * 收款方全称
     */
    @Column(name = "PAYEENAME")
    private Date payeename;

    /**
     * 收款方编码
     */
    @Column(name = "PAYEECODE")
    private String payeecode;

    /**
     * 收款方银行账号
     */
    @Column(name = "PAYEEBANKACCOUNTNUMBER")
    private String payeebankaccountnumber;

    /**
     * 收款方开户行
     */
    @Column(name = "PAYEEBANKACCOUNT")
    private String payeebankaccount;

    /**
     * 个人姓名
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 个人编码
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 拟报销日期
     */
    @Column(name = "PROPOSEDDATE")
    private String proposeddate;

    /**
     * 延期原因
     */
    @Column(name = "REASONFORDELAY")
    private String reasonfordelay;

}
