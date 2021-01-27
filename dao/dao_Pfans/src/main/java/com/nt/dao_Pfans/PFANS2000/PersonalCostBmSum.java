package com.nt.dao_Pfans.PFANS2000;

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
public class PersonalCostBmSum extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 部门简称
     */
    private String departshortBmSum;

    /**
     *Rank
     */
    private String exrankBmSum ;

    /**
     *人数
     */
    private String peopleBmSum ;

    /**
     *月工资
     */
    private String monthBmSum ;

    /**
     * 基本工资
     */
    private String basicBmSum;

    /**
     * 职责工资
     */
    private String balityBmSum;

    /**
     * 补贴总计
     */
    private String totalsubsidiesBmSum;

    /**
     * 月度奖金
     */
    private String monthlybonusBmSum;

    /**
     * 年度奖金
     */
    private String annualbonusBmSum;


    /**
     * 工资总额
     */
    private String totalwagesBmSum;

    /**
     * 工会经费
     */
    private String tradeunionfundsBmSum;

    /**
     * （4-6）社保公司负担总计
     */
    private String sbgsajBmSum;

    /**
     * (4-6）公积金公司负担总计
     */
    private String gjjgsfdajBmSum;

    /**
     * （4-6）人件费总计
     */
    private String aptojuBmSum;

    /**
     * （7-3）社保公司负担总计
     */
    private String sbgsjmBmSum;

    /**
     * （7-3）公积金公司负担总计
     */
    private String gjjgsfdjmBmSum;

    /**
     * （7-3）人件费总计
     */
    private String jutomaBmSum;

    /**
     * 加班费时给
     */
    private String overtimepayBmSum;
}