package com.nt.dao_AOCHUAN.AOCHUAN2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customerbaseinfor")
public class Customerbaseinfor extends BaseModel {
    @Id
    private String customerbaseinfor_id;

    private String customernameen;

    private String customernameja;

    private String customernameko;

    private String customernamecn;

    private String nation;

    private String website;

    private String address1;

    private String address2;

    private String linkman;

    private String establishmentdate;

    private String capital;

    private String tradbank;

    private String settlement;

    private String listedsituation;

    private String members;

    private String type;

    private String businessscope;

    private String devaddress;

    private String proaddress;

    private String businessaddress;

    private String company;

    private String productadvantage;

    private String totalscore;

    private String customerstatus;

    private String remittance;

    private String chinabranch;

    private String targetmarket;

    private String industrystatus;

    private String qualitypolicy;

    private String upperrelationship;

    private String support;

    private String customerstatusscore;

    private String remittancescore;

    private String chinabranchscore;

    private String targetmarketscore;

    private String industrystatusscore;

    private String qualitypolicyscore;

    private String upperrelationshipscore;

    private String supportscore;

    private String productadvantagescore;

    private String kisid;

    private String customermanager;

    private String ratio;
}
