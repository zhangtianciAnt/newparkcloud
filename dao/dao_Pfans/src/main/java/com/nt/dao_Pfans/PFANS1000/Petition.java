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
@Table(name="petition")
public class Petition extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PETITION_ID")
    private String petition_id;

    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    @Column(name = "CONTRACTTYPE")
    private String contracttype;

    @Column(name = "CUSTOENGLISH")
    private String custoenglish;

    @Column(name = "CUSTOCHINESE")
    private String custochinese;

    @Column(name = "RESPONERGLISH")
    private String responerglish;

    @Column(name = "PLACEENGLISH")
    private String placeenglish;

    @Column(name = "PLACECHINESE")
    private String placechinese;


    @Column(name = "PJNAMEJAPANESE")
    private String pjnamejapanese;

    @Column(name = "PJNAMECHINESE")
    private String pjnamechinese;

    @Column(name = "ClAIMDATETIME")
    private String claimdatetime;

    @Column(name = "OPENINGDATE")
    private Date openingdate;


    @Column(name = "ENDDATE")
    private Date enddate;

    @Column(name = "BUSINESSCODE")
    private String businesscode;

    @Column(name = "DELIVERYFINSHDATE")
    private Date  deliveryfinshdate;

    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    @Column(name = "RESPONPHONE")
    private String responphone;

    @Column(name = "CLAIMNUMBER")
    private String claimnumber;

    @Column(name = "CLAIMTYPE")
    private String claimtype;

    @Column(name = "CURRENCYPOSITION")
    private String currencyposition;

    /**
     * 请求日
     */
    @Column(name = "CLAIMDATE")
    private Date claimdate;

    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 契約概要（/開発タイトル）和文
     */
    @Column(name = "CONJAPANESE")
    private String conjapanese;

}
