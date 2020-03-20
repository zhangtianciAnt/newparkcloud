package com.nt.dao_Pfans.PFANS6000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.nt.dao_Pfans.PFANS6000.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelegainformationVo {
    /**
     * 委托信息(活用情报)
     */
    @Id
    @Column(name = "DELEGAINFORMATION_ID")
    private String delegainformation_id;

    /**
     * 项目操作（外键）
     */
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    /**
     * 项目体制（外键）
     */
    @Column(name = "PROJECTSYSTEM_ID")
    private String projectsystem_id;


    /**
     * 入场时间
     */
    @Column(name = "ADMISSIONTIME")
    private String admissiontime;

    /**
     * 退场时间
     */
    @Column(name = "EXITIME")
    private String exitime;


    /**
     * 四月
     */
    @Column(name = "APRIL")
    private String april;

    /**
     * 五月
     */
    @Column(name = "MAY")
    private String may;

    /**
     * 六月
     */
    @Column(name = "JUNE")
    private String june;

    /**
     * 七月
     */
    @Column(name = "JULY")
    private String july;

    /**
     * 八月
     */
    @Column(name = "AUGUST")
    private String august;

    /**
     * 九月
     */
    @Column(name = "SEPTEMBER")
    private String september;

    /**
     * 十月
     */
    @Column(name = "OCTOBER")
    private String october;

    /**
     * 十一月
     */
    @Column(name = "NOVEMBER")
    private String november;

    /**
     * 十二月
     */
    @Column(name = "DECEMBER")
    private String december;


    /**
     * 一月（明年）
     */
    @Column(name = "JANUARY")
    private String january;

    /**
     * 二月（明年）
     */
    @Column(name = "FEBRUARY")
    private String february;

    /**
     * 三月（明年）
     */
    @Column(name = "MARCH")
    private String march;

    /**
     * 勤続月数
     */
    @Column(name = "MONTHLENGTH")
    private String monthlength;

    /**
     * 年度
     */
    @Column(name = "YEAR")
    private String year;

    /**
     * 协力公司
     */
    @Column(name = "COMPANY")
    private String company;

    /**
     * 外注人员ID
     */
    @Column(name = "SUPPLIERINFOR_ID")
    private String supplierinfor_id;

    /**
     * 项目名称(中)
     */
    @Column(name = "PROJECT_NAME")
    private String project_name;

    /**
     * 外注人员姓名ID
     */
    @Column(name = "EXPNAME")
    private String expname;

    /**
     * 项目经理(PM)
     */
    @Column(name = "MANAGERID")
    private String managerid;

    /**
     * 作業形態
     */
    @Column(name = "OPERATIONFORM")
    private String operationform;

    /**
     * 作業分類
     */
    @Column(name = "JOBCLASSIFICATION")
    private String jobclassification;

    /**
     * 所有技術
     */
    @Column(name = "ALLTECHNOLOGY")
    private String alltechnology;

    /**
     * 現場評価
     */
    @Column(name = "SITEVALUATION")
    private String sitevaluation;

    /**
     * 退场理由
     */
    @Column(name = "EXITREASON")
    private String exitreason;

    /**
     * 業務影響
     */
    @Column(name = "BUSINESSIMPACT")
    private String businessimpact;

    /**
     * 対策
     */
    @Column(name = "COUNTERMEASURE")
    private String countermeasure;

    /**
     * 配赋对象
     */
    @Column(name = "DISTRIOBJECTS")
    private String distriobjects;

    /**
     * 社内对象
     */
    @Column(name = "VENUETARGET")
    private String venuetarget;
}
