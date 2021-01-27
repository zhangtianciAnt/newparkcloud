package com.nt.dao_Pfans.PFANS2000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalCostGsSum extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     *RN
     */
    private String exrankGsSum ;


    /**
     *人数
     */
    private String peopleGsSum ;

    /**
     *月工资
     */
    private String monthGsSum ;

    /**
     * 基本工资
     */
    private String basicGsSum;

    /**
     * 职责工资
     */
    private String balityGsSum;

    /**
     * 补贴总计
     */
    private String totalsubsidiesGsSum;

    /**
     * 月度奖金
     */
    private String monthlybonusGsSum;

    /**
     * 年度奖金
     */
    private String annualbonusGsSum;


    /**
     * 工资总额
     */
    private String totalwagesGsSum;

    /**
     * 工会经费
     */
    private String tradeunionfundsGsSum;

    /**
     * （4-6）社保公司负担总计
     */
    private String sbgsajGsSum;

    /**
     * (4-6）公积金公司负担总计
     */
    private String gjjgsfdajGsSum;

    /**
     * （4-6）人件费总计
     */
    private String aptojuGsSum;

    /**
     * （7-3）社保公司负担总计
     */
    private String sbgsjmGsSum;

    /**
     * （7-3）公积金公司负担总计
     */
    private String gjjgsfdjmGsSum;

    /**
     * （7-3）人件费总计
     */
    private String jutomaGsSum;

    /**
     * 加班费时给
     */
    private String overtimepayGsSum;
}