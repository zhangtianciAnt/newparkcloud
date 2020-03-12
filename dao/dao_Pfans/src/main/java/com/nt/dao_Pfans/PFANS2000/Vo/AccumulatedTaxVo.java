package com.nt.dao_Pfans.PFANS2000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccumulatedTaxVo {
    /**
     * 基数表
     */
    private String user_id;

    private String january;
    private String february;
    private String march;
    private String april;
    private String may;
    private String june;
    private String july;
    private String august;
    private String september;
    private String october;
    private String november;
    private String december;
    /**
     * 年间累计税金
     */
    private String sumThis;

    /**
     * 年度应纳税总额
     */
    private String shouldwages;

    /**
     * 年度应纳付税金
     */
    private String shouldtax;

    /**
     * 還付差額
     */
    private String balance;

    private Integer rowindex;

}
