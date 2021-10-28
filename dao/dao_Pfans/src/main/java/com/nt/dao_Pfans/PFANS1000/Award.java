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
@Table(name = "award")
public class Award extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "AWARD_ID")
    private String award_id;

    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    @Column(name = "CONTRACTTYPE")
    private String contracttype;

    @Column(name = "CUSTOJAPANESE")
    private String custojapanese;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "CUSTOCHINESE")
    private String custochinese;

    @Column(name = "PLACEJAPANESE")
    private String placejapanese;

    @Column(name = "PLACECHINESE")
    private String placechinese;

    @Column(name = "MEMBERCOST")
    private String membercost;

    @Column(name = "INVESTORSPEOPOR")
    private String investorspeopor;

    @Column(name = "DEPLOYMENT")
    private String deployment;

    @Column(name = "PJNAMEJAPANESE")
    private String pjnamejapanese;

    @Column(name = "PJNAMECHINESE")
    private String pjnamechinese;

    @Column(name = "CLAIMDATETIME")
    private String claimdatetime;

    @Column(name = "DELIVERYDATE")
    private Date deliverydate;

    @Column(name = "CURRENCYPOSITION")
    private String currencyposition;

    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "EXTRINSIC")
    private String extrinsic;

    @Column(name = "TELEPHONE")
    private String telephone;

    @Column(name = "EQUIPMENT")
    private String equipment;

    @Column(name = "COMMDEPARTMENT")
    private String commdepartment;

    @Column(name = "INDIVIDUAL")
    private String individual;

    @Column(name = "PLANNUMBER")
    private String plannumber;

    @Column(name = "VALUATION")
    private String valuation;

    @Column(name = "VALUATIONNUMBER")
    private String valuationnumber;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "COMMISSION")
    private String commission;

    @Column(name = "PLAN")
    private String plan;

    //事业计划分类
    @Column(name = "CLASSIFICATIONTYPE")
    private String classificationtype;

    //事业计划余额
    @Column(name = "BALANCE")
    private String balance;


    @Column(name = "TOTAL")
    private String total;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "NUMBERMOTH")
    private String numbermoth;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "RATE")
    private String rate;

    @Column(name = "OUTSOURCING")
    private String outsourcing;

    @Column(name = "PJRATE")
    private String pjrate;

    @Column(name = "MAKETYPE")
    private String maketype;

    @Column(name = "EXCHANGERATE")
    private String exchangerate;

    @Column(name = "SARMB")
    private String sarmb;

    @Column(name = "DRAFTINGDATE")
    private Date draftingdate;

    @Column(name = "SCHEDULEDDATE")
    private Date scheduleddate;

    @Column(name = "TABLECOMMUNT")
    private String tablecommunt;

    /**
     * 契約概要（/開発タイトル）和文
     */
    @Column(name = "CONJAPANESE")
    private String conjapanese;


    /**
     * 附件
     */
    @Column(name = "UPLOADFILE")
    private String uploadfile;

    // add-ws-7/17-禅道116任务
    /**
     * 方针合同关联
     */
    @Column(name = "POLICYCONTRACT_ID")
    private String policycontract_id;
    // add-ws-7/17-禅道116任务

    //add-ws-7/20-禅道任务342
    @Column(name = "SEALSTATUS")
    private String sealstatus;
    @Column(name = "STATUSPUBLIC")
    private String statuspublic;
    //年月
    @Column(name = "DATES")
    private String dates;
    //印章ID
    @Column(name = "SEALID")
    private String sealid;
    //add-ws-7/20-禅道任务342

    //ADD_FJL_0807 START
    //暂借款ID
    @Column(name = "LOANAPPLICATION_ID")
    private String loanapplication_id;
    //暂借款编号
    @Column(name = "LOANAPNO")
    private String loanapno;
    //ADD_FJL_0807 END
    //add_fjl_0813
    //余额
    @Column(name = "BALANCEJUDE")
    private String balancejude;
    //add_fjl_0813

    //add-ws-8/13-禅道任务432
    @Column(name = "PJNAMEENGLISH")
    private String pjnameenglish;

    @Column(name = "REGINDIFF")
    private String regindiff;
    //add-ws-8/13-禅道任务432

    //add ccm 20211026 受托决裁添加外注信息 fr
    //构内工数
    @Column(name = "INHOURS")
    private String inhours;
    //构内单价
    @Column(name = "INPRICE")
    private String inprice;
    //构内费用合计
    @Column(name = "INAMOUNT")
    private String inamount;
    //构外工数
    @Column(name = "OUTHOURS")
    private String outhours;
    //构外单价
    @Column(name = "OUTPRICE")
    private String outprice;
    //构外费用合计
    @Column(name = "OUTAMOUNT")
    private String outamount;
    //add ccm 20211026 受托决裁添加外注信息 to

}
