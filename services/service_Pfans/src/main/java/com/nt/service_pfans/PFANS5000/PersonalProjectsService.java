package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.PersonalProjects;
import com.nt.utils.dao.TokenModel;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 *
 */
public interface PersonalProjectsService {


    public List<PersonalProjects> getProjectList(PersonalProjects personalprojects,HttpServletRequest request) throws Exception;

    void insert(PersonalProjects personalprojects, TokenModel tokenModel)throws Exception;
}
