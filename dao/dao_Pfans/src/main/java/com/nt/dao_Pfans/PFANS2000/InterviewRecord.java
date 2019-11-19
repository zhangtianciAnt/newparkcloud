package com.nt.dao_Pfans.PFANS2000;

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
@Table(name = "interviewrecord" )
public class InterviewRecord  extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 本社面试记录ID
     */
    @Id
    @Column(name = "INTERVIEWRECORD_ID")
    private String interviewrecord_id;

    /**
     * 姓名ID
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 性别ID
     */
    @Column(name = "SEX")
    private String sex;

    /**
     * 联系邮箱ID
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 简历接受时间ID
     */
    @Column(name = "ACCEPT_DATE")
    private Date accept_date;

    /**
     * 向部门推荐时间ID
     */
    @Column(name = "RECOMMEND_DATE")
    private Date recommend_date;

    /**
     * 推荐部门ID
     */
    @Column(name = "RECOMMENDDEP")
    private String recommenddep;

    /**
     * 面试时间ID
     */
    @Column(name = "INTERVIEW_DATE")
    private Date interview_date;

    /**
     * 面试部门ID
     */
    @Column(name = "INTERVIEWDEP")
    private String interviewdep;

    /**
     * 简历来源ID
     */
    @Column(name = "SOURCE")
    private String source;

    /**
     * 学校
     */
    @Column(name = "SCHOOL")
    private String school;

    /**
     * 教育补充信息
     */
    @Column(name = "SUPPLEMENT")
    private String supplement;

    /**
     * 经验特长
     */
    @Column(name = "SPECIALITY")
    private String speciality;

    /**
     * 技术分类
     */
    @Column(name = "TECHNOLOGY")
    private String technology;

    /**
     * 面试是否通过
     */
    @Column(name = "RESULT")
    private String result;

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
     * 薪资
     */
    @Column(name = "SALARY")
    private String salary;

    /**
     * 结果说明
     */
    @Column(name = "RESULTSHOWS")
    private String resultshows;

    /**
     * 联系电话
     */
    @Column(name = "CONTACTINFORMATION")
    private String contactinformation;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 社员推荐
     */
    @Column(name = "MEMBER")
    private String member;

    /**
     * 网络招聘
     */
    @Column(name = "NETWORK")
    private String network;

}





