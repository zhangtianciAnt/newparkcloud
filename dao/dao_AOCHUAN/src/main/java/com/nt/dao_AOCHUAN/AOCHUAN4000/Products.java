package com.nt.dao_AOCHUAN.AOCHUAN4000;

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
@Table(name = "products")

public class Products extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRODUCTS_ID")
    private String products_id;

    @Column(name = "CHINANAME")
    private String chinaname;


    @Column(name = "ENGLISHNAME")
    private String englishname;


    @Column(name = "KOREANAME")
    private String koreaname;


    @Column(name = "JAPANESENAME")
    private String japanesename;


    @Column(name = "CASNUM")
    private String casnum;


    @Column(name = "DRUG")
    private String drug;


    @Column(name = "SIMILARDRUGS")
    private String similardrugs;


    @Column(name = "ORIGINAL")
    private String original;


    @Column(name = "HSCODE")
    private String hscode;

    @Column(name = "TAX")
    private String tax;


    @Column(name = "DANGEROUSINFO")
    private String dangerousinfo;

    @Column(name = "TRANSPORTCONDITION")
    private String transportcondition;

    @Column(name = "SUPPLIERINFOR")
    private String Supplierinfor;

    @Column(name = "PATENTINFO")
    private String patentinfo;

    @Column(name = "DMFINFO")
    private String dmfinfo;

    @Column(name = "FZMEDICINETIM")
    private Date fzmedicinetim;

    @Column(name = "DRUGPRICE")
    private String drugprice;

    @Column(name = "QUESTIONNAIRE")
    private String questionnaire;

    @Column(name = "IMSINFO")
    private String imsinfo;

    @Column(name = "REFERENCEPRICE")
    private String referenceprice;

    @Column(name = "UNITPRICE")
    private String unitprice;

    @Column(name = "PATENTSITUATIONINFO")
    private String patentsituationinfo;

    @Column(name = "PRODUCTMANUAL")
    private String productmanual;

    @Column(name = "MSDSINFO")
    private String msdsinfo;

    @Column(name = "IDENTIFICATION")
    private String identification;

    @Column(name = "PHARMACOPOEIA")
    private String pharmacopoeia;

    @Column(name = "DOMESTICINFO")
    private String domesticinfo;

    @Column(name = "GMPINFO")
    private String gmpinfo;

    @Column(name = "WINNINGINFO")
    private String winninginfo;

    @Column(name = "PNAME")
    private String pname;

    @Column(name = "MANUFACTURER")
    private String manufacturer;

    @Column(name = "QUOTENUMBER")
    private String quotenumber;

    @Column(name = "APPLICATIONTIM")
    private Date applicationtim;

    @Column(name = "GTSUPPLEMENTTIM")
    private Date gtsupplementtim;

    @Column(name = "APPROVALDATE")
    private Date approvaldate;

    @Column(name = "PZAPPROVALDATE")
    private Date pzapprovaldate;

    @Column(name = "MFDSNAME")
    private String mfdsname;

    @Column(name = "DISTINGUISH")
    private String distinguish;

    @Column(name = "SPECIFICATION")
    private String specification;

    @Column(name = "GMPBOOK")
    private String gmpbook;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "FACTORYPROGRESS")
    private String factoryprogress;

    @Column(name = "AOCHUANPROGRESS")
    private String aochuanprogress;

    @Column(name = "MFDSNAMEPROGRESS")
    private String mfdsnameprogress;

    @Column(name = "REGISTRATIONNUM")
    private String registrationnum;

    @Column(name = "PRODUCTNAME")
    private String productname;

    @Column(name = "WHMANUFACTURER")
    private String whmanufacturer;

    @Column(name = "MANUFACTURERADRESS")
    private String manufactureradress;

    @Column(name = "COUNTRYS")
    private String countrys;

    @Column(name = "WHGTSUPPLEMENTTIM")
    private Date whgtsupplementtim;

    @Column(name = "WHAPPROVALDATE")
    private Date whapprovaldate;

    @Column(name = "STORAGE")
    private String storage;

    @Column(name = "EXPIRATIONTIM")
    private String expirationtim;

    @Column(name = "WHSPECIFICATION")
    private String whspecification;

    @Column(name = "HOLDER")
    private String holder;

    @Column(name = "WHGMPBOOK")
    private String whgmpbook;

    @Column(name = "NOWAUDIT")
    private String nowaudit;

    @Column(name = "WHREMARKS")
    private String whremarks;

    @Column(name = "CHANGELOG")
    private String changelog;

    @Column(name = "REGISTRATIONPLAN")
    private String registrationplan;

    @Column(name = "REGISTRATIONPROGRESS")
    private String registrationprogress;

    @Column(name = "AUDITPROGRESS")
    private String auditprogress;

    @Column(name = "APPROVALPROGRESS")
    private String approvalprogress;

}
