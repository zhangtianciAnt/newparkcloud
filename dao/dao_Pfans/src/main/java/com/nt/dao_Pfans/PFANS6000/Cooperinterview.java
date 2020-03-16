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
@Table(name = "cooperinterview")
public class Cooperinterview extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 协力面试记录
     */
    @Id
    @Column(name = "COOPERINTERVIEW_ID")
    private String cooperinterview_id;


    /**
     * 姓名
     */
    @Column(name = "COOPERNAME")
    private String coopername;

    /**
     * 性别
     */
    @Column(name = "SEX")
    private String sex;

    /**
     * 联系方式
     */
    @Column(name = "CONTACTINFORMATION")
    private String contactinformation;

    /**
     * 出生日期
     */
    @Column(name = "BIRTH")
    private Date birth;

    /**
     * 年龄
     */
    @Column(name = "AGE")
    private String age;

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
     * 面试时间
     */
    @Column(name = "INTERVIEW_DATE")
    private Date interview_date;

    /**
     * 面试结果
     */
    @Column(name = "RESULT")
    private String result;

    /**
     * 技术分类
     */
    @Column(name = "TECHNOLOGY")
    private String technology;

    /**
     * 入职与否
     */
    @Column(name = "WHETHERENTRY")
    private String whetherentry;

    /**
     * Rn
     */
    @Column(name = "RN")
    private String rn;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 毕业年
     */
    @Column(name = "GRADUATION_YEAR")
    private Date graduation_year;

    /**
     * 供应商UUID
     */
    @Column(name = "SUPPLIERINFOR_ID")
    private String supplierinfor_id;
}
