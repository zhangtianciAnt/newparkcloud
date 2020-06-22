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
@Table(name = "loanapplication")
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

    @Column(name = "JUDGEMENTS")
    private String judgements;
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
    private String moduleid;

    /**
     * 发票/日记账编号
     */
    @Column(name = "ACCOUNTNUMBER")
    private String accountnumber;

    /**
     * 预计报销日
     */
    @Column(name = "REIMBURSEMENT")
    private String reimbursement;

    /**
     * 申请日期
     */
    @Column(name = "APPLICATION_DATE")
    private String application_date;

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
    private String payeename;

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
     * 转账收款方全称
     */
    @Column(name = "ACCOUNTPAYEENAME")
    private String accountpayeename;

    /**
     * 延期原因
     */
    @Column(name = "REASONFORDELAY")
    private String reasonfordelay;
//add-ws-N0.70-相应字段添加
    /**
     * 决裁名
     */
    @Column(name = "JUDGEMENTS_NAME")
    private String judgements_name;
    /**
     * 收款人
     */
    @Column(name = "USER_NAME")
    private String user_name;
//add-ws-N0.70-相应字段添加

    /**
     * 申请单编号
     */
    @Column(name = "LOANAPNO")
    private String loanapno;

    /**
     * 附件
     */
    @Column(name = "UPLOADFILE")
    private String uploadfile;

    /**
     * 核销
     */
    @Column(name = "CANAFVER")
    private String canafver;
}
