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
@Table(name = "contractapplication")
public class Contractapplication extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CONTRACTAPPLICATION_ID")
    private String contractapplication_id;

    @Column(name = "CAREERYEAR")
    private String careeryear;

    @Column(name = "PERIODS")
    private String periods;

    @Column(name = "GROUP_ID")
    private String group_id;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "ORGNUMBER")
    private String orgnumber;

    @Column(name = "SIDEGROUP")
    private String sidegroup;

    @Column(name = "DEPLOYMENT")
    private String deployment;

    @Column(name = "APPLICATIONDATE")
    private Date applicationdate;

    @Column(name = "USER_ID")
    private String user_id;

    @Column(name = "CONTRACTTYPE")
    private String contracttype;

    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    @Column(name = "ENTRYCONDITION")
    private String entrycondition;

    @Column(name = "ENTRYPAYMENT")
    private Date entrypayment;

    @Column(name = "DELIVERYCONDITION")
    private String deliverycondition;

    @Column(name = "DELIVERY")
    private String delivery;

    @Column(name = "CLAIMCONDITION")
    private String claimcondition;

    @Column(name = "CLAIM")
    private String claim;

    @Column(name = "CLAIMTYPE")
    private String claimtype;

    @Column(name = "DELIVERYDATE")
    private Date deliverydate;

    @Column(name = "COMPLETIONDATE")
    private Date  completiondate;

    @Column(name = "DELIVERYFINSHDATE")
    private Date deliveryfinshdate;

    @Column(name = "LOADINGJUDGE")
    private String loadingjudge;

    @Column(name = "CLAIMDATETIME")
    private String  claimdatetime;

    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    @Column(name = "CURRENCYPOSITION")
    private String currencyposition;

    @Column(name = " SUPPORTDATE")
    private Date  supportdate;

    @Column(name = "CONTRACTDATE")
    private String  contractdate;

    @Column(name = "CLAIMDATE")
    private Date claimdate;

    @Column(name = "CUSTOJAPANESE")
    private String custojapanese;

    @Column(name = "CUSTOENGLISH")
    private String custoenglish;

    @Column(name = "CUSTOABBREVIATION")
    private String custoabbreviation;

    @Column(name = "CUSTOCHINESE")
    private String custochinese;

    @Column(name = "BUSINESSCODE")
    private String businesscode;

    @Column(name = "VARTO")
    private String varto;

    @Column(name = "PLACEJAPANESE")
    private String placejapanese;

    @Column(name = "PLACEENGLISH")
    private String  placeenglish;

    @Column(name = "PLACECHINESE")
    private String placechinese;

    @Column(name = "RESPONJAPANESE")
    private String responjapanese;

    @Column(name = "RESPONERGLISH")
    private String responerglish;

    @Column(name = "RESPONPHONE")
    private String responphone;

    @Column(name = "RESPONEMAIL")
    private String responemail;

    @Column(name = "CONJAPANESE")
    private String conjapanese;

    @Column(name = "CONENGLISH")
    private String conenglish;

    @Column(name = "CONCHINESE")
    private String conchinese;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "ENTRUSTEDNUMBER")
    private String entrustednumber;

    @Column(name = "PAPERCONTRACT")
    private String papercontract;

    @Column(name = "STATE")
    private String state;

    @Column(name = "DECIDE")
    private String decide;

    @Column(name = "FIRSTJUDGE")
    private String firstjudge;

    @Column(name = "SECONDJUDGE")
    private String secondjudge;

    @Column(name = "OUTPUTMANAGER")
    private String outputmanager;

    @Column(name = "MANAGER")
    private String manager;

    @Column(name = "DECISIONNUMBER")
    private String decisionnumber;

    @Column(name = "OUTNUMBER")
    private String  outnumber;

    @Column(name = "PRODUCTNUMBER")
    private String productnumber;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "MAKETYPE")
    private String maketype;

    @Column(name = "ROWINDEX")
    private String rowindex;

    @Column(name = "THEME")
    private String theme;

    @Column(name = "TEMAID")
    private String temaid;

    @Column(name = "QINGREMARKS")
    private String qingremarks;

    @Column(name = "EXTENSIONDATE")
    private Date extensiondate;
}
