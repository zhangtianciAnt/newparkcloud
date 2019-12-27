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
     * 出生日期
     */
    @Column(name = "BIRTH")
    private Date birth;

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
     * 作业形态
     */
    @Column(name = "OPERATIONFORM")
    private String operationform;

    /**
     * 作业分类
     */
    @Column(name = "JOBCLASSIFICATION")
    private String jobclassification;

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

}
