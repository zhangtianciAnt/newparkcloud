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
@Table(name = "quotation")
public class Quotation extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "QUOTATION_ID")
    private String quotationid;

    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    @Column(name = "CONTRACTTYPE")
    private String contracttype;

    @Column(name = "TRUSTEEJAPANESE")
    private String trusteejapanese;

    @Column(name = "TRUSTEECHINESE")
    private String trusteechinese;

    @Column(name = "ENTRUSTEDJAPANESE")
    private String entrustedjapanese;

    @Column(name = "ENTRUSTEDCHINESE")
    private String entrustedchinese;

    @Column(name = "DEPLOYMENT")
    private String deployment;

    @Column(name = "PJJAPANESE")
    private String pjjapanese;

    @Column(name = "PJCHINESE")
    private String pjchinese;

    @Column(name = "STARTDATE")
    private Date startdate;

    @Column(name = "ENDDATE")
    private Date enddate;

    @Column(name = "CURRENCYPOSITION")
    private String currencyposition;

    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    @Column(name = "LOADINGJUDGE")
    private String loadingjudge;

    @Column(name = "REQUESTNUMBER")
    private String requestnumber;

    @Column(name = "DRAFTINGDATE")
    private Date draftingdate;

    @Column(name = "BATCH")
    private String batch;

    @Column(name = "SETTLEMENT")
    private String settlement;

    @Column(name = "CONTRACT")
    private String contract;

    @Column(name = "DEPLOY")
    private String deploy;

    @Column(name = "RESPONSIBLE")
    private String responsible;

    @Column(name = "TEL")
    private String tel;

    @Column(name = "CONSTITUTIONCHART")
    private String constitutionchart;

    @Column(name = "CLAIMDATETIME")
    private String claimdatetime;
}
