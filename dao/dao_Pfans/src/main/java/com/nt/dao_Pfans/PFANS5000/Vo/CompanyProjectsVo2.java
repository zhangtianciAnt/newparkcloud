package com.nt.dao_Pfans.PFANS5000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProjectsVo2 {
    /**
     * 项目名称
     */
    private String project_name;

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

}
