package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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

    //add-ws-4/28-报销人添加
    @Column(name = "USER_NAME")
    private String user_name;
    //add-ws-4/28-报销人添加

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

    //add_qhr_20210810 添加项目名称
    @Transient
    private String projectname;

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


//add-ws-5/11-备注添加
    @Column(name = "PREPAREFOR")
    private String preparefor;
//add-ws-5/11-备注添加



    //add_fjl --希望付款日
    @Column(name = "EXPECTEDPAYDATE")
    private Date expectedpaydate;

    @Column(name = "EXPORTCSV")
    private String exportcsv;

    //add_fjl --希望付款日


    /**
     * 附件
     */
    @Column(name = "UPLOADFILE")
    private String uploadfile;

    @Column(name = "TORMB")
    private String tormb;

    //add fjl   供应商/社员名称
    @Column(name = "ACCENAME")
    private String accename;
    //决裁摘要内容
    @Column(name = "REMARKSDETAIL")
    private String remarksdetail;
    //决裁类型
    @Column(name = "JUDGEMENTS_TYPE")
    private String judgements_type;
    //决裁金额
    @Column(name = "JUDGEMENTS_MONEYS")
    private String judgements_moneys;
    // add-ws-8/12-禅道任务446
    //处理状态
    @Column(name = "PROCESSINGSTATUS")
    private String processingstatus;
    // add-ws-8/12-禅道任务446

    //暂借款类型 0-个人账户（办公室）1-个人账户（费用）2-其他 //PSDCD_PFANS_20210519_BUG_006 修改供应商编码 供应商地点错误 fr
    @Column(name = "LOANTYPE")
    private String loantype;
    //PSDCD_PFANS_20210519_BUG_006 修改供应商编码 供应商地点错误 to
    //region add_qhr_20210830 添加外注费用check和记账月份
    //外注费用check 0-是 1-否
    @Column(name = "CHECKEDWZFY")
    private String checkedWZFY;

    //记账月份
    @Column(name = "JZMONTH")
    private Date jzmonth;
    //endregion add_qhr_20210830 添加外注费用check和记账月份
}
