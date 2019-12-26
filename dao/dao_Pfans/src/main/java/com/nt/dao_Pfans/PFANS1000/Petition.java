package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(name = "DEPOSITENGLISH")
    private String depositenglish;

    @Column(name = "DEPOSITCHINESE")
    private String depositchinese;

    @Column(name = "DEREENGLISH")
    private String dereenglish;

    @Column(name = "PRPLACEENGLISH")
    private String prplaceenglish;

    @Column(name = "PRPLACECHINESE")
    private String prplacechinese;

    @Column(name = "PJNAMEJAPANESE")
    private String pjnamejapanese;

    @Column(name = "PJNAMECHINESE")
    private String pjnamechinese;

    @Column(name = "DEVELOPDATE")
    private String developdate;

    @Column(name = "BUSINESSCODE")
    private String businesscode;

    @Column(name = "DELIVERYDATE")
    private String deliverydate;

    @Column(name = "CLAIMONEY")
    private String claimoney;

    @Column(name = "DEPOSITPHONE")
    private String depositphone;

    @Column(name = "CLAIMNUMBER")
    private String claimnumber;

    @Column(name = "CLAIMTYPE")
    private String claimtype;

    @Column(name = "CURRENCYFORMAT")
    private String currencyformat;
}
