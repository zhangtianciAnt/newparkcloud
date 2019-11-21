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
@Table(name="publicexpense")
public class PublicExpense extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PUBLICEXPENSE_ID")
    private String publicexpenseid;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "CENTER_ID")
    private String centerid;

    @Column(name = "GROUP_ID")
    private String groupid;

    @Column(name = "TEAM_ID")
    private String teamid;

    @Column(name = "TELEPHONE")
    private String telephone;

    @Column(name = "BUDGETUNIT")
    private String budgetunit;

    @Column(name = "MODULEID")
    private String moduleid;

    @Column(name = "ACCOUNTNUMBER")
    private String accountnumber;

    @Column(name = "REIMBURSEMENTDATE")
    private Date reimbursementdate;

    @Column(name = "MONEYS")
    private String moneys;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "FOREIGNCURRENCY")
    private String foreigncurrency;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "PAYMENTMETHOD")
    private String paymentmethod;

    @Column(name = "PAYEENAME")
    private String payeename;

    @Column(name = "PAYEECODE")
    private String payeecode;

    @Column(name = "PAYEEBANKACCOUNTNUMBER")
    private String payeebankaccountnumber;

    @Column(name = "PAYEEBANKACCOUNT")
    private String payeebankaccount;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @Column(name = "RECEIVABLES")
    private String receivables;

    @Column(name = "LOAN")
    private String loan;

    @Column(name = "JUDGEMENT")
    private String judgement;

    @Column(name = "FULLNAME")
    private String fullname;
}
