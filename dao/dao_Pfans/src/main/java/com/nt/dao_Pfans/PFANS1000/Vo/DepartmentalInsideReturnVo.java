package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.DepartmentalInside;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

public class DepartmentalInsideReturnVo {

    private String departmentalinside_id;
    /*年度*/
    private String years;
    /*部门*/
    private String department;
    /*THEMEINFOR_ID*/
    private String themeinfor_id;
    /*THEMENAME*/
    private String themename;
    /*委托元分类*/
    private String divide;
    /*委托元*/
    private String toolsorgs;
    /*合同号*/
    private String contractnumber;
    /*合同金额*/
    private String claimamount;
    /*合同金额-请求金额*/
    private String contracatamountdetail;
    /*合同回数*/
    private String claimtype;
    /*合同状态*/
    private String entrycondition;
    /*PJNUMBER*/
    private String project_id;
    /*PJNUMBER*/
    private String numbers;
    /*员工rank*/
    private String staffrank;
    /*四月计划*/
    private String staffcustplan04;
    /*四月实际*/
    private String staffcustactual04;
    /*五月计划*/
    private String staffcustplan05;
    /*五月实际*/
    private String staffcustactual05;
    /*六月计划*/
    private String staffcustplan06;
    /*六月实际*/
    private String staffcustactual06;
    /*第一季度——工数差*/
    private String workdifferentfirst;
    /*第一季度——RANK别价差*/
    private String rankdifferentfirst;
    /*七月计划*/
    private String staffcustplan07;
    /*七月实际*/
    private String staffcustactual07;
    /*八月计划*/
    private String staffcustplan08;
    /*八月实际*/
    private String staffcustactual08;
    /*九月计划*/
    private String staffcustplan09;
    /*九月实际*/
    private String staffcustactual09;
    /*第二季度——工数差*/
    private String workdifferentsecond;
    /*第二季度——RANK别价差*/
    private String rankdifferentsecond;
    /*十月计划*/
    private String staffcustplan10;
    /*十月实际*/
    private String staffcustactual10;
    /*十一月计划*/
    private String staffcustplan11;
    /*十一月实际*/
    private String staffcustactual11;
    /*十二月计划*/
    private String staffcustplan12;
    /*十二月实际*/
    private String staffcustactual12;
    /*第三季度——工数差*/
    private String workdifferentthird;
    /*第三季度——RANK别价差*/
    private String rankdifferentthird;
    /*一月计划*/
    private String staffcustplan01;
    /*一月实际*/
    private String staffcustactual01;
    /*二月计划*/
    private String staffcustplan02;
    /*二月实际*/
    private String staffcustactual02;
    /*三月计划*/
    private String staffcustplan03;
    /*三月实际*/
    private String staffcustactual03;
    /*第四季度——工数差*/
    private String workdifferentfourth;
    /*第四季度——RANK别价差*/
    private String rankdifferentfourth;
    List<DepartmentalInside> departmentalInsideList;
}
