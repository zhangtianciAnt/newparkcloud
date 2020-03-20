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
@Table(name = "priceset")
public class Priceset extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @Column(name = "PRICESET_ID")
    private String priceset_id;

    /**
     *査定時間
     */
    @Column(name = "ASSESSTIME")
    private String assesstime;

    /**
     *单价设定姓名
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     *卒業年
     */
    @Column(name = "GRADUATION")
    private Date graduation;

    /**
     *会社名
     */
    @Column(name = "COMPANY")
    private String company;

    /**
     *技術スキル
     */
    @Column(name = "TECHNICAL")
    private String technical;

    /**
     *技術価値
     */
    @Column(name = "TECHNOLOGY")
    private String technology;

    /**
     *管理スキル
     */
    @Column(name = "MANAGEMENT")
    private String management;

    /**
     *管理価値
     */
    @Column(name = "VALUE")
    private String value;

    /**
     *分野スキル
     */
    @Column(name = "FIELDSKILLS")
    private String fieldskills;

    /**
     *分野価値
     */
    @Column(name = "FIELD")
    private String field;

    /**
     *語学スキル
     */
    @Column(name = "LANGUAGE")
    private String language;

    /**
     *語学価値
     */
    @Column(name = "LANGUAGEVALUE")
    private String languagevalue;

    /**
     *勤務スキル
     */
    @Column(name = "WORKSKILLS")
    private String workskills;

    /**
     *勤務価値
     */
    @Column(name = "SERVICE")
    private String service;

    /**
     *勤続評価
     */
    @Column(name = "EVALUATION")
    private String evaluation;

    /**
     *勤続価値
     */
    @Column(name = "RVICEVALUE")
    private String rvicevalue;

    /**
     *PSDCD駐在規模
     */
    @Column(name = "PSDCDSCALE")
    private String psdcdscale;

    /**
     *規模価値
     */
    @Column(name = "SCALEVALUE")
    private String scalevalue;

    /**
     *貢献評価
     */
    @Column(name = "CONTRIBUTION")
    private String contribution;

    /**
     *貢献係数
     */
    @Column(name = "COEFFICIENT")
    private String coefficient;

    /**
     *出向者PSDCD相当ランク
     */
    @Column(name = "STAFFPSDCDRANK")
    private String staffpsdcdrank;

    /**
     *ランク価値
     */
    @Column(name = "RANKVALUE")
    private String rankvalue;

    /**
     *貢献評価1
     */
    @Column(name = "BUTIONEVALUATION")
    private String butionevaluation;

    /**
     *貢献係数1
     */
    @Column(name = "BUTIONCOEFFICIENT")
    private String butioncoefficient;

    /**
     *開発単価微調整
     */
    @Column(name = "UNITPRICE")
    private String unitprice;

    /**
     *開発総単価
     */
    @Column(name = "TOTALUNIT")
    private String totalunit;

    /**
     *PSDCD相当ランク
     */
    @Column(name = "PSDCDRANK")
    private String psdcdrank;

    /**
     *前年単価
     */
    @Column(name = "YEARUNIT")
    private String yearunit;

    /**
     *前年差
     */
    @Column(name = "YEAR")
    private String year;

    /**
     *共通费用
     */
    @Column(name = "COMMON")
    private String common;

    /**
     *备考
     */
    @Column(name = "REMARKS")
    private String remarks;

}
