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
@Table(name = "expatriatesinfor")
public class Expatriatesinfor extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 外驻人员信息
     */
    @Id
    @Column(name = "EXPATRIATESINFOR_ID")
    private String expatriatesinfor_id;

    /**
     * 姓名
     */
    @Column(name = "EXPNAME")
    private String expname;

    /**
     * 性别
     */
    @Column(name = "SEX")
    private String sex;

    /**
     * 年龄
     */
    @Column(name = "AGE")
    private String age;

//    /**
//     * 出生日期
//     */
//    @Column(name = "BIRTH")
//    private Date birth;

    /**
     * 供应商名称
     */
    @Column(name = "SUPPLIERNAME")
    private String suppliername;

    /**
     * 毕业院校
     */
    @Column(name = "GRADUATESCHOOL")
    private String graduateschool;

    /**
     * 学历
     */
    @Column(name = "EDUCATION")
    private String education;

    /**
     * 技术分类
     */
    @Column(name = "TECHNOLOGY")
    private String technology;

    /**
     * Rn
     */
    @Column(name = "RN")
    private String rn;

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
     * 毕业年
     */
    @Column(name = "GRADUATION_YEAR")
    private Date graduation_year;

    /**
     * 入场时间
     */
    @Column(name = "ADMISSIONTIME")
    private Date admissiontime;

    /**
     * 退场时间
     */
    @Column(name = "EXITIME")
    private Date exitime;

    /**
     * 退场理由
     */
    @Column(name = "EXITREASON")
    private String exitreason;

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
     * 退场与否
     */
    @Column(name = "EXITS")
    private String exits;

    /**
     * 一月
     */
    @Column(name = "JANUARY")
    private String january;

    /**
     * 二月
     */
    @Column(name = "FEBRUARY")
    private String february;

    /**
     * 三月
     */
    @Column(name = "MARCH")
    private String march;

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
     * 勤続月数
     */
    @Column(name = "MONTHLENGTH")
    private String monthlength;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 项目名称(中)
     */
    @Column(name = "PROJECT_NAME")
    private String project_name;

    /**
     * 项目经理(PM)
     */
    @Column(name = "MANAGERID")
    private String managerid;

    /**
     * BP会社名
     */
    @Column(name = "BPCOMPANY")
    private String bpcompany;

    /**
     * BP名前
     */
    @Column(name = "BPNAME")
    private String bpname;

    /**
     * 配賦対象
     */
    @Column(name = "DISTRIOBJECTS")
    private String distriobjects;

    /**
     * 構内対象
     */
    @Column(name = "VENUETARGET")
    private String venuetarget;


}
