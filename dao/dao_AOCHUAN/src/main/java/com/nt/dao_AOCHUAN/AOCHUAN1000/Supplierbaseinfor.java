package com.nt.dao_AOCHUAN.AOCHUAN1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "supplierbaseinfor")
public class Supplierbaseinfor extends BaseModel {
    @Id
    private String supplierbaseinfor_id;

    private String suppliernamecn;

    private String suppliernameen;

    private String website;

    private String address1;

    private String address2;

    private String linkman;

    private String lealperson;

    private String registeredcapital;

    private String creditcode;

    private String telephone;

    private String depositbank;

    private String bankaddress;

    private String bankaccount;

    private String bankinformation;

    private String industryinvolved;

    private String gmp;

    private String iso;

    private String auditexperience;

    private String lastyearturnover;

    private String registeredcapital2;

    private String industrystatus;

    private String qualitysystem;

    private String communication;

    private String upperrelationship;

    private String support;

    private String exportexperience;

    private String documentation;

    private String marketawareness;

    private String deliverystatus;

    private String productadvantage;

    private String lastyearturnoverscore;

    private String registeredcapital2score;

    private String industrystatusscore;

    private String qualitysystemscore;

    private String communicationscore;

    private String upperrelationshipscore;

    private String supportscorescore;

    private String exportexperiencescore;

    private String documentationscore;

    private String marketawarenessscore;

    private String deliverystatusscore;

    private String productadvantagescore;

    private String lastyearturnoverstandard;

    private String registeredcapital2standard;

    private String productadvantagestandard;

    private String lastyearturnoverratio;

    private String registeredcapital2ratio;

    private String productadvantageratio;

    private String totalscore;
}
