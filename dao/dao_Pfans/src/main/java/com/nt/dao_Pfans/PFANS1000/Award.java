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

    @Column(name = "ENTRUSTJAPANESE")
    private String entrustjapanese;

    @Column(name = "ENTRUSTCHINESE")
    private String entrustchinese;

    @Column(name = "ENPLACEJAPANESE")
    private String enplacejapanese;

    @Column(name = "ENPLACECHINESE")
    private String enplacechinese;

    @Column(name = "DEPLOYMENT")
    private String deployment;

    @Column(name = "PJNAMEJAPANESE")
    private String pjnamejapanese;

    @Column(name = "PJNAMECHINESE")
    private String pjnamechinese;

    @Column(name = "DEVELOPDATE")
    private String developdate;

    @Column(name = "DELIVERYDATE")
    private Date deliverydate;

    @Column(name = "CURRENCYFORMAT")
    private String currencyformat;

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

    @Column(name = "DEPOSITJAPANESE")
    private String depositjapanese;

    @Column(name = "DEPOSITCHINESE")
    private String depositchinese;

    @Column(name = "DEPLACEJAPANESE")
    private String deplacejapanese;

    @Column(name = "DEPLACECHINESE")
    private String deplacechinese;

    @Column(name = "TOTAL")
    private String total;

    @Column(name = "NUMBERMOTH")
    private String numbermoth;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "RATE")
    private String rate;

    @Column(name = "MAKETYPE")
    private String maketype;








}
