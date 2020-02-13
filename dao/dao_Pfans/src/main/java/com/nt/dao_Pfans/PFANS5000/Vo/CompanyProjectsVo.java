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
    private List<ProjectPlan> projectplan;
    /**
     * 项目资源
     */
    private List<ProjectreSources> projectresources;


    private List<OutSource> outSources;
    /**
     * 项目开发计划
     */
    private List<StageInformation> stageinformation;
}
