package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalCostRb extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    private String useridRb;

    /**
     * 部门简称
     */
    private String departshortRb;

    /**
     *Rank
     */
    private String ltrankRb ;

    /**
     *工资总额
     */
    private String totalwagesRb ;

    /**
     *养老保险基4
     */
    private String oldylbxjajRb ;

    /**
     * 失业保险基4
     */
    private String lossybxjajRb;

    /**
     * 工伤保险基4
     */
    private String gsbxjajRb;

    /**
     * 医疗保险基4
     */
    private String ylbxjajRb;

    /**
     * 生育保险基4
     */
    private String sybxjajRb;

    /**
     * 公积金基数4
     */
    private String gjjjsajRb;


    /**
     * 养老保险基7
     */
    private String oldylbxjjmRb;

    /**
     * 失业保险基7
     */
    private String lossybxjjmRb;

    /**
     * 工伤保险基7
     */
    private String gsbxjjmRb;

    /**
     * 医疗保险基7
     */
    private String ylbxjjmRb;

    /**
     * 生育保险基7
     */
    private String sybxjjmRb;

    /**
     * 公积金基数7
     */
    private String gjjjsjmRb;


    /**
     *  4月-计划
     */
    private String aprilPlan;

    /**
     *  4月-预测
     */
    private String aprilFore;

    /**
     *  4月-实际
     */
    private String aprilTrue;

    /**
     *  5月-计划
     */
    private String mayPlan;

    /**
     *  5月-预测
     */
    private String mayFore;

    /**
     *  5月-实际
     */
    private String mayTrue;

    /**
     *  6月-计划
     */
    private String junePlan;

    /**
     *  6月-预测
     */
    private String juneFore;

    /**
     *  6月-实际
     */
    private String juneTrue;

    /**
     *  7月-计划
     */
    private String julyPlan;
    /**
     *  7月-预测
     */
    private String julyFore;

    /**
     *  7月-实际
     */
    private String julyTrue;

    /**
     *  8月-计划
     */
    private String augPlan;
    /**
     *  8月-预测
     */
    private String augFore;

    /**
     *  8月-实际
     */
    private String augTrue;

    /**
     *  9月-计划
     */
    private String sepPlan;

    /**
     *  9月-预测
     */
    private String sepFore;

    /**
     *  9月-实际
     */
    private String sepTrue;

    /**
     *  10月-计划
     */
    private String octPlan;

    /**
     *  10月-预测
     */
    private String octFore;

    /**
     *  10月-实际
     */
    private String octTrue;

    /**
     *  11月-计划
     */
    private String novePlan;

    /**
     *  11月-预测
     */
    private String noveFore;

    /**
     *  11月-实际
     */
    private String noveTrue;

    /**
     *  12月-计划
     */
    private String decePlan;

    /**
     *  12月-预测
     */
    private String deceFore;

    /**
     *  12月-实际
     */
    private String deceTrue;

    /**
     *  1月-计划
     */
    private String janPlan;

    /**
     *  1月-预测
     */
    private String janFore;

    /**
     *  1月-实际
     */
    private String janTrue;

    /**
     *  2月-计划
     */
    private String febPlan;

    /**
     *  2月-预测
     */
    private String febFore;

    /**
     *  2月-实际
     */
    private String febTrue;

    /**
     *  3月-计划
     */
    private String marPlan;

    /**
     *  3月-预测
     */
    private String marFore;

    /**
     *  3月-实际
     */
    private String marTrue;

}