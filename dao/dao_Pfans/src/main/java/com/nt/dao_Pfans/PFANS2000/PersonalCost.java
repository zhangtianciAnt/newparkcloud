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
@Table(name = "personalcost")
public class PersonalCost extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 人件费ID
     */
    @Id
    @Column(name = "PERSONALCOSTID")
    private String personalcostid;

    /**
     * 人件费年度ID
     */
    @Column(name = "YEARSANTID")
    private String yearsantid;

    /**
     * 姓名
     */
    @Column(name = "USERID")
    private String userid;

    /**
     * 等级
     */
    @Column(name = "PRANK")
    private String prank;

    /**
     * 在职状态
     */
    @Column(name = "WORKDAY")
    private String workday;


    /**
     * 社保基数-养老
     */
    @Column(name = "YANGBASERESULT")
    private String yangbaseresult;

    /**
     * 社保基数-医疗
     */
    @Column(name = "YIBASERESULT")
    private String yibaseresult;

    /**
     * 社保基数-公积金
     */
    @Column(name = "ZHUBASERESULT")
    private String zhubaseresult;


    /**
     * 4月人件费预计
     */
    @Column(name = "APRILCOSTY")
    private String aprilcosty;

    /**
     * 5月人件费预计
     */
    @Column(name = "MAYCOSTY")
    private String maycosty;

    /**
     * 6月人件费预计
     */
    @Column(name = "JUNECOSTY")
    private String junecosty;

    /**
     * 7月人件费预计
     */
    @Column(name = "JULYCOSTY")
    private String julycosty;

    /**
     * 8月人件费预计
     */
    @Column(name = "AUGUSTCOSTY")
    private String augustcosty;

    /**
     * 9月人件费预计
     */
    @Column(name = "SEPTEMBERCOSTY")
    private String septembercosty;

    /**
     * 10月人件费预计
     */
    @Column(name = "OCTOBERCOSTY")
    private String octobercosty;

    /**
     * 11月人件费预计
     */
    @Column(name = "NOVEMBERCOSTY")
    private String novembercosty;

    /**
     * 12月人件费预计
     */
    @Column(name = "DECEMBERCOSTY")
    private String decembercosty;

    /**
     * 1月人件费预计
     */
    @Column(name = "JANUARYCOSTY")
    private String januarycosty;

    /**
     * 2月人件费预计
     */
    @Column(name = "FEBRUARYCOSTY")
    private String februarycosty;

    /**
     * 3月人件费预计
     */
    @Column(name = "MARCHCOSTY")
    private String marchcosty;


    /**
     * 4月人件费实际
     */
    @Column(name = "APRILCOSTS")
    private String aprilcosts;

    /**
     * 5月人件费实际
     */
    @Column(name = "MAYCOSTS")
    private String maycosts;

    /**
     * 6月人件费实际
     */
    @Column(name = "JUNECOSTS")
    private String junecosts;

    /**
     * 7月人件费实际
     */
    @Column(name = "JULYCOSTS")
    private String julycosts;

    /**
     * 8月人件费实际
     */
    @Column(name = "AUGUSTCOSTS")
    private String augustcosts;

    /**
     * 9月人件费实际
     */
    @Column(name = "SEPTEMBERCOSTS")
    private String septembercosts;

    /**
     * 10月人件费实际
     */
    @Column(name = "OCTOBERCOSTS")
    private String octobercosts;

    /**
     * 11月人件费实际
     */
    @Column(name = "NOVEMBERCOSTS")
    private String novembercosts;

    /**
     * 12月人件费实际
     */
    @Column(name = "DECEMBERCOSTS")
    private String decembercosts;

    /**
     * 1月人件费实际
     */
    @Column(name = "JANUARYCOSTS")
    private String januarycosts;

    /**
     * 2月人件费实际
     */
    @Column(name = "FEBRUARYCOSTS")
    private String februarycosts;

    /**
     * 3月人件费实际
     */
    @Column(name = "MARCHCOSTS")
    private String marchcosts;


    /**
     * 4~6月人件费
     */
    @Column(name = "APTOJUCOST")
    private String aptojucost;

    /**
     * 7~3月人件费 1月1日自动生成
     */
    @Column(name = "JUTOMACOST")
    private String jutomacost;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     *Centerid
     */
    @Column(name = "CENTERID ")
    private String centerid ;

    /**
     *GROUPID
     */
    @Column(name = "GROUPID ")
    private String groupid ;

}

