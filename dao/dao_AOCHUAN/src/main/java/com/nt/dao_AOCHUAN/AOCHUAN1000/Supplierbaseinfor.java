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

    private String qaqcnumber;
    private String plantarea;
    private String producttype;
    private String advtechnology;
    private String planttype;
    private String newproduct;
    private String other;

    private String registeredcapital2;
    private String detectability;
    private String envprotection;
    private String productcapacity;
    private String gmpcapacity;
    private String devability;
    private String costcontrol;
    private String qualitylevel;
    private String documentability;
    private String relation;
    private String industrystatus;
    private String decision;
    private String communication;
    private String cooperation;
    private String jkmarket;
    private String paymentterms;

}
