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
@Table(name = "delegainformation")
public class Delegainformation extends BaseModel {
    private static final long serialVersionUID = 1L;

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
    private Date admissiontime;

    /**
     * 退场时间
     */
    @Column(name = "EXITTIME")
    private Date exittime;

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
     * ID
     */
    @Column(name = "SUPPLIERINFOR_ID")
    private String supplierinfor_id;

    /**
     * GROUP_ID
     */
    @Column(name = "GROUP_ID")
    private String group_id;



}
