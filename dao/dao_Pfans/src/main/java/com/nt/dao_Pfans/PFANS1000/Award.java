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

    @Column(name = "CUSTOCHINESE")
    private String custochinese;

    @Column(name = "PLACEJAPANESE")
    private String placejapanese;

    @Column(name = "PLACECHINESE")
    private String placechinese;

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







}
