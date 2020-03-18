package com.nt.dao_Pfans.PFANS5000.Vo;

import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS5000.*;
import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProjectsVo {
    /**
     * 项目管理
     */
    private CompanyProjects companyprojects;
    /**
     * 项目计划
     */
    private List<StageInformation> stageinformation;
    /**
     * 项目体制
     */
    private List<Projectsystem> projectsystem;
    /**
     * 日志管理
     */
    private List<LogManagement> logmanagement;
    /**
     * 项目合同
     */
    private List<ProjectContract> projectcontract;
    /**
     * 委托信息
     */
    private List<Delegainformation> delegainformation;
    /**
     * 委托元为内采的合同
     */
    private List<Contractnumbercount> contractnumbercount;

}
