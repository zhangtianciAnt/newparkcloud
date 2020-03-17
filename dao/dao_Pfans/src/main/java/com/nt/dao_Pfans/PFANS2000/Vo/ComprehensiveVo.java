package com.nt.dao_Pfans.PFANS2000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComprehensiveVo {
    /**
     * 基数表
     */
    private String user_id;
    /**
     * 工资表
     */
    private String month1wages;
    private String month1appreciation;
    private String month2wages;
    private String month2appreciation;
    private String month3wages;
    private String month3appreciation;
    private String month4wages;
    private String month4appreciation;
    private String month5wages;
    private String month5appreciation;
    private String month6wages;
    private String month6appreciation;
    private String month7wages;
    private String month7appreciation;
    private String month8wages;
    private String month8appreciation;
    private String month9wages;
    private String month9appreciation;
    private String month10wages;
    private String month10appreciation;
    private String month11wages;
    private String month11appreciation;
    private String month12wages;
    private String month12appreciation;

    /**
     * 年度賞与（当年度）
     */
    private String totalbonus1;

    /**
     * 累计月度赏与
     */
    private String appreciationtotal;

    /**
     * 累计年间收入(不含12月)
     */
    private String totalwithout12;

    /**
     * 累计年间收入
     */
    private String totalwithin12;
}
