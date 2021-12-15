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
@Table(name = "departmentalinsidetail")
public class DepartmentalinsiDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DEPARTMENTALINSIDETAIL_ID")
    private String departmentalinsidetail_id;

    /*年度*/
    @Column(name = "YEARS")
    private String years;

    /*部门*/
    @Column(name = "DEPARTMENT")
    private String department;

    /*THEMEINFOR_ID*/
    @Column(name = "THEMEINFOR_ID")
    private String themeinfor_id;

    /*分类名*/
    @Column(name = "CONTRACTNUMBER")
    private String contractnumber;

    /*分类
    * (1-员工工数；2-外注工数；3-外注费用）
    * */
    @Column(name = "TYPEE")
    private String typee;

    /*四月计划*/
    @Column(name = "STAFFCUSTPLAN04")
    private String staffcustplan04;

    /*四月实际*/
    @Column(name = "STAFFCUSTACTUAL04")
    private String staffcustactual04;

    /*五月计划*/
    @Column(name = "STAFFCUSTPLAN05")
    private String staffcustplan05;

    /*五月实际*/
    @Column(name = "STAFFCUSTACTUAL05")
    private String staffcustactual05;

    /*六月计划*/
    @Column(name = "STAFFCUSTPLAN06")
    private String staffcustplan06;

    /*六月实际*/
    @Column(name = "STAFFCUSTACTUAL06")
    private String staffcustactual06;

    /*七月计划*/
    @Column(name = "STAFFCUSTPLAN07")
    private String staffcustplan07;

    /*七月实际*/
    @Column(name = "STAFFCUSTACTUAL07")
    private String staffcustactual07;

    /*八月计划*/
    @Column(name = "STAFFCUSTPLAN08")
    private String staffcustplan08;

    /*八月实际*/
    @Column(name = "STAFFCUSTACTUAL08")
    private String staffcustactual08;

    /*九月计划*/
    @Column(name = "STAFFCUSTPLAN09")
    private String staffcustplan09;

    /*九月实际*/
    @Column(name = "STAFFCUSTACTUAL09")
    private String staffcustactual09;

    /*十月计划*/
    @Column(name = "STAFFCUSTPLAN10")
    private String staffcustplan10;

    /*十月实际*/
    @Column(name = "STAFFCUSTACTUAL10")
    private String staffcustactual10;

    /*十一月计划*/
    @Column(name = "STAFFCUSTPLAN11")
    private String staffcustplan11;

    /*十一月实际*/
    @Column(name = "STAFFCUSTACTUAL11")
    private String staffcustactual11;

    /*十二月计划*/
    @Column(name = "STAFFCUSTPLAN12")
    private String staffcustplan12;

    /*十二月实际*/
    @Column(name = "STAFFCUSTACTUAL12")
    private String staffcustactual12;

    /*一月计划*/
    @Column(name = "STAFFCUSTPLAN01")
    private String staffcustplan01;

    /*一月实际*/
    @Column(name = "STAFFCUSTACTUAL01")
    private String staffcustactual01;

    /*二月计划*/
    @Column(name = "STAFFCUSTPLAN02")
    private String staffcustplan02;

    /*二月实际*/
    @Column(name = "STAFFCUSTACTUAL02")
    private String staffcustactual02;

    /*三月计划*/
    @Column(name = "STAFFCUSTPLAN03")
    private String staffcustplan03;

    /*三月实际*/
    @Column(name = "STAFFCUSTACTUAL03")
    private String staffcustactual03;

    /*年间——工数差*/
    @Column(name = "WORKDIFFERENTOFYEAR")
    private String workdifferentofyear;

    /*年间——RANK别价差*/
    @Column(name = "RANKDIFFERENTOFYEAR")
    private String rankdifferentofyear;
}
