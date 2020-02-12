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
@Table(name = "projectInformation")
public class ProjectInformation extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 起案id
     */
    @Id
    @Column(name = "PROJECTINFORMATION_ID")
    private String projectinformation_id;

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
     * 编号
     */
    @Column(name = "NUMBERS")
    private String numbers;

    /**
     * 项目名称(中)
     */
    @Column(name = "NAME1")
    private String name1;

    /**
     * 项目名称(和)
     */
    @Column(name = "NAME2")
    private String name2;

    /**
     * 项目负责人(PL)
     */
    @Column(name = "PL")
    private String pl;

    /**
     * 项目经理(TL)
     */
    @Column(name = "TL")
    private String tl;

    /**
     * 项目类型
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 项目领域
     */
    @Column(name = "AREA")
    private String area;

    /**
     * 开发语言
     */
    @Column(name = "LANGUAGE")
    private String language;

    /**
     * 预计开始时间
     */
    @Column(name = "STARTTIME")
    private Date starttime;

    /**
     * 预计完成时间
     */
    @Column(name = "ENDTIME")
    private Date endtime;

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
     * 项目简介
     */
    @Column(name = "INTRODUCTION")
    private String introduction;

    /**
     * 项目目的要求
     */
    @Column(name = "REQUIREMENTS")
    private String requirements;

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

}
