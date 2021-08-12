package com.nt.dao_Pfans.PFANS6000;

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
@Table(name = "pjexternalinjection")
public class PjExternalInjection extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * PJ别外注费id
     */
    @Id
    @Column(name = "PJEXTERNALINJECTION_ID")
    private String pjexternalinjection_id;

    /**
     * 年度
     */
    @Column(name = "YEARS")
    private String years;

    /**
     * 部门
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * themeinfor_id
     */
    @Column(name = "THEMEINFOR_ID")
    private String themeinfor_id;

    /**
     * theme名称
     */
    @Column(name = "THEMENAME")
    private String themename;

    /**
     * 委托元分类（分野）
     */
    @Column(name = "DIVIDE")
    private String divide;

    /**
     * 委托元公司
     */
    @Column(name = "TOOLSORGS")
    private String toolsorgs;

    /**
     * PJ项目id
     */
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    /**
     * PJ项目名称
     */
    @Column(name = "PROJECT_NAME")
    private String project_name;

    /**
     * 协力公司
     */
    @Column(name = "COMPANY")
    private String company;

    /**
     * 4月
     */
    @Column(name = "APRIL")
    private String april;

    /**
     * 5月
     */
    @Column(name = "MAY")
    private String may;

    /**
     * 6月
     */
    @Column(name = "JUNE")
    private String june;

    /**
     * 7月
     */
    @Column(name = "JULY")
    private String july;

    /**
     * 8月
     */
    @Column(name = "AUGUST")
    private String august;

    /**
     * 9月
     */
    @Column(name = "SEPTEMBER")
    private String september;

    /**
     * 10月
     */
    @Column(name = "OCTOBER")
    private String october;

    /**
     * 11月
     */
    @Column(name = "NOVEMBER")
    private String november;

    /**
     * 12月
     */
    @Column(name = "DECEMBER")
    private String december;

    /**
     * 1月
     */
    @Column(name = "JANUARY")
    private String january;

    /**
     * 2月
     */
    @Column(name = "FEBRUARY")
    private String february;

    /**
     * 3月
     */
    @Column(name = "MARCH")
    private String march;

    /**
     * 合计
     */
    @Column(name = "TOTAL")
    private String total;

}
