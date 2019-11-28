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
    private String sumThis;
    /**
     * 当月応発工資（工资总额(纳税+免税)+只納税）
     */
    private String shouldwages;

    private Integer rowindex;

}
