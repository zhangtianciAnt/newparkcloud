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
     *
     */
    @Column(name = "GROUP_ID")
    private String group_id;

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

    /**
     * 编号
     */
    @Column(name = "NUMBER")
    private String number;

    /**
     * 职位
     */
    @Column(name = "POST")
    private String post;

    /**
     * 供应商ID
     */
    @Column(name = "SUPPLIERINFOR_ID")
    private String supplierinfor_id;

    /**
     * 联系方式
     */
    @Column(name = "CONTACTINFORMATION")
    private String contactinformation;

    /**
     * 出生日期
     */
    @Column(name = "BIRTH")
    private String birth;

    /**
     * 经验特长
     */
    @Column(name = "SPECIALITY")
    private String speciality;

    /**
     * 面试部门
     */
    @Column(name = "INTERVIEWDEP")
    private String interviewdep;

    /**
     * 面试结果
     */
    @Column(name = "RESULT")
    private String result;

    /**
     * 入职与否
     */
    @Column(name = "WHETHERENTRY")
    private String whetherentry;


}
