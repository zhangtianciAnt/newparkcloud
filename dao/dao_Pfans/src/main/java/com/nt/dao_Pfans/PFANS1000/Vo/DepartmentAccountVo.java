package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.DepartmentAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentAccountVo {

    /**
     * themeID
     */
    private String theme_id;
    /**
     * theme名
     */
    private String themename;
    /**
     * 委托元类型
     */
    private String contract;
    /**
     * 委托元
     */
    private String toolsorgs;
    /**
     * 合同ID
     */
    private String contractapplication_id;
    /**
     * 合同番号
     */
    private String contractnumber;

    /**
     * 合同金额
     */
    private String claimamounttotal;

    /**
     * 契約進捗状況
     */
    private String entrycondition;
    /**
     * 合同状态
     */
    private String state;
    /**
     * 项目ID
     */
    private String companyprojects_id;
    /**
     * 项目编号
     */
    private String numbers;
    /**
     * 年度
     */
    private String years;
    /**
     * 合同后金额
     */
    private String amount;

    /**
     * 4月实际
     */
    private Double moneyactual4;
    /**
     * 5月实际
     */
    private Double moneyactual5;
    /**
     * 6月实际
     */
    private Double moneyactual6;

    /**
     * 7月实际
     */
    private Double moneyactual7;
    /**
     * 8月实际
     */
    private Double moneyactual8;
    /**
     * 9月实际
     */
    private Double moneyactual9;
    /**
     * 10月实际
     */
    private Double moneyactual10;
    /**
     * 11月实际
     */
    private Double moneyactual11;
    /**
     * 12月实际
     */
    private Double moneyactual12;
    /**
     * 1月实际
     */
    private Double moneyactual1;
    /**
     * 2月实际
     */
    private Double moneyactual2;
    /**
     * 3月实际
     */
    private Double moneyactual3;

    /**
     * 4月计划
     */
    private Double moneyplan4;
    /**
     * 5月计划
     */
    private Double moneyplan5;
    /**
     * 6月计划
     */
    private Double moneyplan6;

    /**
     * 7月计划
     */
    private Double moneyplan7;
    /**
     * 8月计划
     */
    private Double moneyplan8;
    /**
     * 9月计划
     */
    private Double moneyplan9;
    /**
     * 10月计划
     */
    private Double moneyplan10;
    /**
     * 11月计划
     */
    private Double moneyplan11;
    /**
     * 12月计划
     */
    private Double moneyplan12;
    /**
     * 1月计划
     */
    private Double moneyplan1;
    /**
     * 2月计划
     */
    private Double moneyplan2;
    /**
     * 3月计划
     */
    private Double moneyplan3;

    //请求金额合计
    private String claimamountSum;

    //分配金额合计
    private String contractamountSum;

    //请求金额4月
    private String claimamountSum4;

    //请求金额5月
    private String claimamountSum5;

    //请求金额6月
    private String claimamountSum6;

    //请求金额7月
    private String claimamountSum7;

    //请求金额8月
    private String claimamountSum8;

    //请求金额9月
    private String claimamountSum9;

    //请求金额10月
    private String claimamountSum10;

    //请求金额11月
    private String claimamountSum11;

    //请求金额12月
    private String claimamountSum12;

    //请求金额1月
    private String claimamountSum1;

    //请求金额2月
    private String claimamountSum2;

    //请求金额3月
    private String claimamountSum3;

    //分配金额4月
    private String contractamountSum4;

    //分配金额5月
    private String contractamountSum5;

    //分配金额6月
    private String contractamountSum6;

    //分配金额7月
    private String contractamountSum7;

    //分配金额8月
    private String contractamountSum8;

    //分配金额9月
    private String contractamountSum9;

    //分配金额10月
    private String contractamountSum10;

    //分配金额11月
    private String contractamountSum11;

    //分配金额12月
    private String contractamountSum12;

    //分配金额1月
    private String contractamountSum1;

    //分配金额2月
    private String contractamountSum2;

    //分配金额3月
    private String contractamountSum3;

    private List<DepartmentAccount> departmentAccountList;

}
