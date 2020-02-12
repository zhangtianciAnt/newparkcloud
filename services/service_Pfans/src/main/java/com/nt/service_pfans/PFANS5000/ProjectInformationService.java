package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.ProjectInformation;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProjectInformationService {
    List<ProjectInformation> getProjectInformation(ProjectInformation projectinformation) throws Exception;

    public ProjectInformation One(String projectinformation_id) throws Exception;

    public void insertProjectInformation(ProjectInformation projectinformation, TokenModel tokenModel)throws Exception;

    public void updateProjectInformation(ProjectInformation projectinformation, TokenModel tokenModel)throws Exception;

}
