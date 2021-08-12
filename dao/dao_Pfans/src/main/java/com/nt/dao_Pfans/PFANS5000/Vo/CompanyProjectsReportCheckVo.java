package com.nt.dao_Pfans.PFANS5000.Vo;

import com.nt.dao_Pfans.PFANS5000.*;
import com.nt.utils.Encryption.Encryption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProjectsReportCheckVo {

    private CompanyProjects companyProjects;

    private List<StageInformation> stageInformationList;

    private List<Projectsystem> projectsystemList;

    private List<ProjectContract> projectContractList;
}
