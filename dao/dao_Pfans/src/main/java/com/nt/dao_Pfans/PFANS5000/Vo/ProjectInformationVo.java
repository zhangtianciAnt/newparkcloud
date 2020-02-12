package com.nt.dao_Pfans.PFANS5000.Vo;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.ProjectInformation;
import com.nt.dao_Pfans.PFANS5000.ProjectPlan;
import com.nt.dao_Pfans.PFANS5000.StageInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProjectInformationVo {

    /**
     * 起案书
     */
    private ProjectInformation projectInformation;

    /**
     * 项目开发计划
     */
    private List<StageInformation> stageInformations;


}
