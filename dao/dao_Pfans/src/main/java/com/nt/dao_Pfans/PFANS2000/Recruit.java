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
@Table(name = "recruit")
public class Recruit extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 招聘申请ID
	 */
    @Id
    @Column(name = "RECRUIT_ID")
    private String recruitid;

    /**
     * 岗位名称
     */
    @Column(name = "POSTNAME")
    private String postname;

    /**
     * 申请センター
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 申请グループ
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 申请チーム
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 需求人数
     */
    @Column(name = "PEOPLEREQUIRED")
    private String peoplerequired;

    /**
     * 工作地点
     */
    @Column(name = "WORKPLACE")
    private String workplace;

    /**
     * 申请时间
     */
    @Column(name = "APPLICATIONTIME")
    private String applicationtime;

    /**
     * 所属项
     */
    @Column(name = "VIEWPROJECT")
    private String viewproject;

    /**
     * 建议招聘途径
     */
    @Column(name = "RECRUITMENTROUTE")
    private String recruitmentroute;

    /**
     * 基本其他
     */
    @Column(name = "OTHER")
    private String other;

    /**
     * 岗位需求
     */
    @Column(name = "JOBDEMAND")
    private String jobdemand;

    /**
     * 需否出差
     */
    @Column(name = "NEEDTOTRAVEL")
    private String needtotravel;

    /**
     * 需求等级
     */
    @Column(name = "DEMANDLEVEL")
    private String demandlevel;

    /**
     * 建议岗位薪资
     */
    @Column(name = "SUGGESTEDSALARY")
    private String suggestedsalary;

    /**
     * 转正日
     */
    @Column(name = "TURNINGDAY")
    private String turningday;

    /**
     * 转正后薪资
     */
    @Column(name = "AFTERTURNINGPOSITIV")
    private String afterturningpositiv;

    /**
     * 期望到岗时间
     */
    @Column(name = "EXPECTEDARRIVALTIME")
    private String expectedarrivaltime;

    /**
     * 技能等级级别
     */
    @Column(name = "SKILLLEVEL")
    private String skilllevel;

    /**
     * 性别要求
     */
        @Column(name = "GENDERREQUIREMENTS")
    private String genderrequirements;

    /**
     * 岗位其他
     */
    @Column(name = "POSTOTHER")
    private String postother;

    /**
     * 年龄要求
     */
    @Column(name = "AGEREQUIREMENT")
    private String agerequirement;

    /**
     * 学历要求
     */
    @Column(name = "REQUIREMENTS")
    private String requirements;

    /**
     * 专业要求
     */
    @Column(name = "PROFESSIONAL")
    private String professional;

    /**
     * 经验要求
     */
    @Column(name = "EXPERIENCE")
    private Date experience;

    /**
     * 其他要求
     */
    @Column(name = "OTHERREQUIREMENTS")
    private String otherrequirements;

    /**
     * 岗位职责
     */
    @Column(name = "RESPONSIBILITIES")
    private String responsibilities;
}
