package com.nt.dao_Pfans.PFANS5000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProjectsVo2 {
    /**
     * 项目名称
     */
    private String project_name;

    /**
     * 项目名称
     */
    private String project_namejp;

    /**
     * 项目编号
     */
    private String numbers;

    /**
     * 工作阶段
     */
    private String phase;

    /**
     * 成果物状态
     */
    private String productstatus;

    /**
     * 阶段状态
     */
    private String phasestatus;


    /**
     * 预计工数
     */
    private String estimatedwork;

    /**
     * 实际工数
     */
    private String actualwork;


    /**
     * 合同状态
     */
    private String contractstatus;

    /**
     * 状态
     */
    private String status;

    /**
     * id
     */
    private String companyprojects_id;

    /**
     * 契約書番号
     */
    private String contractnumber;

    /**
     * 项目负责人(PL)
     */
    private String leaderid;

    /**
     * 项目分野
     */
    private String field;

    /**
     * 契約形式 todo
     */
//    private String contractnumber;

    /**
     * 受託工数
     */
    private String work;

    /**
     * 委托元
     */
    private String entrust;

    /**
     * 实际开始时间
     */
    private Date actualstarttime;

    /**
     * 实际结束时间
     */
    private Date actualendtime;

    /**
     * 负责人
     */
    private String owner;
}
