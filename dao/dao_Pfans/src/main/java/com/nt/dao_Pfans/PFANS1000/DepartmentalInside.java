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
@Table(name = "departmentalinside")
public class DepartmentalInside extends BaseModel {

	private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DEPARTMENTALINSIDE_ID")
    private String departmentalinside_id;

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

    /*委托元*/
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

    /*合同状态*/
    @Column(name = "ENTRYCONDITION")
    private String entrycondition;

    /*PJNUMBER*/
    @Column(name = "PROJECT_ID")
    private String project_id;

    /*PJNUMBER*/
    @Column(name = "NUMBERS")
    private String numbers;

    /*员工rank*/
    @Column(name = "STAFFRANK")
    private String staffrank;

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

    /*第一季度——工数差*/
    @Column(name = "WORKDIFFERENTFIRST")
    private String workdifferentfirst;

    /*第一季度——RANK别价差*/
    @Column(name = "RANKDIFFERENTFIRST")
    private String rankdifferentfirst;

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

    /*第二季度——工数差*/
    @Column(name = "WORKDIFFERENTSECOND")
    private String workdifferentsecond;

    /*第二季度——RANK别价差*/
    @Column(name = "RANKDIFFERENTSECOND")
    private String rankdifferentsecond;

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

    /*第三季度——工数差*/
    @Column(name = "WORKDIFFERENTTHIRD")
    private String workdifferentthird;

    /*第三季度——RANK别价差*/
    @Column(name = "RANKDIFFERENTTHIRD")
    private String rankdifferentthird;

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

    /*第四季度——工数差*/
    @Column(name = "WORKDIFFERENTFOURTH")
    private String workdifferentfourth;

    /*第四季度——RANK别价差*/
    @Column(name = "RANKDIFFERENTFOURTH")
    private String rankdifferentfourth;



}
