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

    @Column(name = "PROJECT_ID")
    private String project_id;

    @Column(name = "ACCOUNTNUMBER")
    private String accountnumber;

    @Column(name = "JUDGEMENT_NAME")
    private String judgement_name;

    @Column(name = "REIMBURSEMENTDATE")
    private Date reimbursementdate;

    @Column(name = "MONEYS")
    private String moneys;

    @Column(name = "CURRENCYRATE")
    private String currencyrate;

    @Column(name = "RMBEXPENDITURE")
    private String rmbexpenditure;

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

    @Column(name = "TYPE")
    private String type;

    @Column(name = "FULLNAME")
    private String fullname;

    @Column(name = "SUBJECTNUMBER")
    private String subjectnumber;

    @Column(name = "SUBJECTNAME")
    private String subjectname;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "PURCHASESUBJECTNUMBER")
    private String purchasesubjectnumber;

    @Column(name = "PURCHASESUBJECTNAME")
    private String purchasesubjectname;

    @Column(name = "PURCHASEREMARKS")
    private String purchaseremarks;

    @Column(name = "OTHERSUBJECTNUMBER")
    private String othersubjectnumber;

    @Column(name = "OTHERSUBJECTNAME")
    private String othersubjectname;

    @Column(name = "OTHERREMARKS")
    private String otherremarks;

    @Column(name = "SUPPLIERNAME")
    private String suppliername;

    @Column(name = "INVOICENO")
    private String invoiceno;

//add-ws-4/27-BS科目添加
    @Column(name = "BSEXTERNAL")
    private String bsexternal;
//add-ws-4/27-BS科目添加
}
