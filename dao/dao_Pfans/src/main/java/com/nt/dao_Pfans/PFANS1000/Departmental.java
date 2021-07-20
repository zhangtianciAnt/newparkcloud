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
@Table(name = "Departmental")
public class Departmental extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "DEPARTMENTAL_ID")
    private String departmental_id;

    /*年度*/
    @Column(name = "YEARS")
    private String years;

    /*部门*/
    @Column(name = "DEPARTMENT")
    private String department;

    /*THEMEINFOR_ID*/
    @Column(name = "THEMEINFOR_ID")
    private String themeinfor_id;

    /*THEMENAME*/
    @Column(name = "THEMENAME")
    private String themename;

    /*委托元分类*/
    @Column(name = "DIVIDE")
    private String divide;

    /*委托元公司*/
    @Column(name = "TOOLSORGS")
    private String toolsorgs;

    /*合同号*/
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    /*合同金额*/
    @Column(name = "CLAIMAMOUNT")
    private String claimamount;

    /*合同金额-请求金额*/
    @Column(name = "CONTRACATAMOUNTDETAIL")
    private String contracatamountdetail;

    /*合同回数*/
    @Column(name = "CLAIMTYPE")
    private String claimtype;

    /*PJNUMBER*/
    @Column(name = "NUMBERS")
    private String numbers;

    /*员工工数*/
    @Column(name = "STAFFNUM")
    private String staffnum;

    /*外驻工数*/
    @Column(name = "OUTSTAFFNUM")
    private String outstaffnum;

    /*外驻社名*/
    @Column(name = "OUTCOMPANY")
    private String outcompany;

    /*四月*/
    @Column(name = "STAFFCUST04")
    private String staffcust04;

    /*五月*/
    @Column(name = "STAFFCUST05")
    private String staffcust05;

    /*六月*/
    @Column(name = "STAFFCUST06")
    private String staffcust06;

    /*七月*/
    @Column(name = "STAFFCUST07")
    private String staffcust07;

    /*八月*/
    @Column(name = "STAFFCUST08")
    private String staffcust08;

    /*九月*/
    @Column(name = "STAFFCUST09")
    private String staffcust09;

    /*十月*/
    @Column(name = "STAFFCUST10")
    private String staffcust10;

    /*十一月*/
    @Column(name = "STAFFCUST11")
    private String staffcust11;

    /*十二月*/
    @Column(name = "STAFFCUST12")
    private String staffcust12;

    /*一月*/
    @Column(name = "STAFFCUST01")
    private String staffcust01;

    /*二月*/
    @Column(name = "STAFFCUST02")
    private String staffcust02;

    /*三月*/
    @Column(name = "STAFFCUST03")
    private String staffcust03;

}
