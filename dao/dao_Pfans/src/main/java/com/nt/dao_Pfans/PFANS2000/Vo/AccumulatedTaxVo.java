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
    private String USER_ID;

    /**
     * 本月应扣缴所得税
     */
    private String THISMONTHADJUSTMENT;

    /**
     * 当月応発工資（工资总额(纳税+免税)+只納税）
     */
    private String SHOULDWAGES;

}
