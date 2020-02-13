package com.nt.dao_Pfans.PFANS5000;

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
@Table(name = "companyprojects")
public class CompanyProjects extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 立项申请ID
     */
    @Id
    @Column(name = "COMPANYPROJECTS_ID")
    private String companyprojects_id;

    //新增
    /**
     * 所属センター
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 所属グループ
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 所属チーム
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 受託工数
     */
    @Column(name = "WORK")
    private String work;

    /**
     * 納期
     */
    @Column(name = "DEADLINE")
    private String deadline;

    /**
     * 其他管理工具
     */
    @Column(name = "TOOLS")
    private String tools;

    /**
     * 委托元
     */
    @Column(name = "ENTRUST")
    private String entrust;

    /**
     * 委托元部署
     */
    @Column(name = "DEPLOYMENT")
    private String deployment;

    /**
     * 委托元代表
     */
    @Column(name = "BEHALF")
    private String behalf;

    /**
     * 委托元情报
     */
    @Column(name = "INTELLIGENCE")
    private String intelligence;
    //新增结束

    /**
     * 项目名称(中)
     */
    @Column(name = "PROJECT_NAME")
    private String project_name;

    /**
     * 项目名称(和)
     */
    @Column(name = "PROJECT_NAMEJP")
    private String project_namejp;

    /**
     * 项目编号
     */
    @Column(name = "NUMBERS")
    private String numbers;

    /**
     * 所属部门
     */
    @Column(name = "DEPARTMENTID")
    private String departmentid;

    /**
     * 项目负责人(PL)
     */
    @Column(name = "LEADERID")
    private String leaderid;

    /**
     * 项目经理(PM)
     */
    @Column(name = "MANAGERID")
    private String managerid;

    /**
     * 项目类型
     */
    @Column(name = "PROJECTTYPE")
    private String projecttype;

    /**
     * 项目领域
     */
    @Column(name = "FIELD")
    private String field;

    /**
     * 开发流程
     */
    @Column(name = "TECHNOLOGICAL")
    private String technological;

    /**
     * 开始时间
     */
    @Column(name = "STARTDATE")
    private Date startdate;

    /**
     * 预计完成时间
     */
    @Column(name = "ENDDATE")
    private Date enddate;

    /**
     * 预计人月
     */
    @Column(name = "MANMONTH")
    private String manmonth;

    /**
     * 预计成本 (千元)
     */
    @Column(name = "COST")
    private String cost;

    /**
     * 预计销售额 (千元)
     */
    @Column(name = "SALESVOLUME")
    private String salesvolume;

    /**
     * 项目标签
     */
    @Column(name = "PROJECTALABEL")
    private String projectalabel;

    /**
     * 开发语言
     */
    @Column(name = "LANGUAGES")
    private String languages;

    /**
     * 之前立项情况
     */
    @Column(name = "SITUATION")
    private String situation;

    /**
     * 机密程度
     */
    @Column(name = "CONFIDENTIAL")
    private String confidential;

    /**
     * 项目管理工具
     */
    @Column(name = "MANAGEMENTTOOL")
    private String managementtool;

    /**
     * 客户名称
     */
    @Column(name = "CUSTOMERNAME")
    private String customername;

    /**
     * 客户代表
     */
    @Column(name = "REPRESENTATIVE")
    private String representative;

    /**
     * 客户基本情况
     */
    @Column(name = "BASICSITUATION")
    private String basicsituation;

    /**
     * 项目简介
     */
    @Column(name = "BRIEFINTRODUCTION")
    private String briefintroduction;

    /**
     * 项目目的要求
     */
    @Column(name = "REQUIREMENT")
    private String requirement;

    @Column(name = "UPLOADFILE")
    private String uploadfile;


}
