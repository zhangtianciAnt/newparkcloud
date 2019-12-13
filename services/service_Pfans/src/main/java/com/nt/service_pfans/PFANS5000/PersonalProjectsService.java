package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.PersonalProjects;
import com.nt.dao_Pfans.PFANS5000.Vo.PersonalProjectsVo;
import com.nt.utils.dao.TokenModel;
import java.util.List;


/**
 *
 */
public interface PersonalProjectsService {


    public List<PersonalProjects> getProjectList(PersonalProjects personalprojects) throws Exception;

    void insert(PersonalProjectsVo personalprojectsvo, TokenModel tokenModel)throws Exception;

    void delete(PersonalProjects personalprojects, TokenModel tokenModel)throws Exception;
}
