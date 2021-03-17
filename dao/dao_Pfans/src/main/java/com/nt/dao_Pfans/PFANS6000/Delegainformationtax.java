package com.nt.dao_Pfans.PFANS6000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delegainformationtax")
public class Delegainformationtax extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 委托信息税率
     */
    @Id
    @Column(name = "DELEGAINFORMATIONTAX_ID")
    private String delegainformationtax_id;

    /**
     * 四月含税总额
     */
    @Column(name = "APRIL")
    private String april;

    /**
     * 四月税金
     */
    @Column(name = "APRILTAX")
    private String apriltax;

    /**
     * 五月含税总额
     */
    @Column(name = "MAY")
    private String may;

    /**
     * 五月税金
     */
    @Column(name = "MAYTAX")
    private String maytax;

    /**
     * 六月含税总额
     */
    @Column(name = "JUNE")
    private String june;

    /**
     * 六月税金
     */
    @Column(name = "JUNETAX")
    private String junetax;

    /**
     * 七月含税总额
     */
    @Column(name = "JULY")
    private String july;

    /**
     * 七月税金
     */
    @Column(name = "JULYTAX")
    private String julytax;

    /**
     * 八月含税总额
     */
    @Column(name = "AUGUST")
    private String august;

    /**
     * 八月税金
     */
    @Column(name = "AUGUSTTAX")
    private String augusttax;

    /**
     * 九月含税总额
     */
    @Column(name = "SEPTEMBER")
    private String september;

    /**
     * 九月税金
     */
    @Column(name = "SEPTEMBERTAX")
    private String septembertax;

    /**
     * 十月含税总额
     */
    @Column(name = "OCTOBER")
    private String october;

    /**
     * 十月税金
     */
    @Column(name = "OCTOBERTAX")
    private String octobertax;

    /**
     * 十一月含税总额
     */
    @Column(name = "NOVEMBER")
    private String november;

    /**
     * 十一月税金
     */
    @Column(name = "NOVEMBERTAX")
    private String novembertax;

    /**
     * 十二月含税总额
     */
    @Column(name = "DECEMBER")
    private String december;

    /**
     * 十二月税金
     */
    @Column(name = "DECEMBERTAX")
    private String decembertax;


    /**
     * 一月（明年）含税总额
     */
    @Column(name = "JANUARY")
    private String january;


    /**
     * 一月（明年）税金
     */
    @Column(name = "JANUARYTAX")
    private String januarytax;

    /**
     * 二月（明年）含税总额
     */
    @Column(name = "FEBRUARY")
    private String february;

    /**
     * 二月（明年）税金
     */
    @Column(name = "FEBRUARYTAX")
    private String februarytax;

    /**
     * 三月（明年）含税总额
     */
    @Column(name = "MARCH")
    private String march;

    /**
     * 三月（明年）税金
     */
    @Column(name = "MARCHTAX")
    private String marchtax;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 年度
     */
    @Column(name = "YEAR")
    private String year;

    /**
     * GROUP_ID
     */
    @Column(name = "GROUP_ID")
    private String group_id;
}
