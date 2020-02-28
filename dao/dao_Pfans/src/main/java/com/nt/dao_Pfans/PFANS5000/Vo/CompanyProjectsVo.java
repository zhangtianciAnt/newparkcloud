package com.nt.dao_Pfans.PFANS5000.Vo;

import com.nt.dao_Pfans.PFANS2000.Citation;
import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.dao_Pfans.PFANS5000.*;
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
}
