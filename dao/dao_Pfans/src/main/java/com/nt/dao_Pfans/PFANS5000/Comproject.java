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
@Table(name = "comproject")
public class Comproject extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 立项申请ID
     */
    @Id
    @Column(name = "COMPROJECT_ID")
    private String comproject_id;


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
     * 项目类型
     */
    @Column(name = "PROJECTTYPE")
    private String projecttype;


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
     * 项目简介
     */
    @Column(name = "BRIEFINTRODUCTION")
    private String briefintroduction;






}
