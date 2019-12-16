package com.nt.service_pfans.PFANS5000.Impl;


import com.nt.dao_Pfans.PFANS5000.PersonalProjects;
import com.nt.dao_Pfans.PFANS5000.Vo.PersonalProjectsVo;
import com.nt.service_pfans.PFANS5000.PersonalProjectsService;
import com.nt.service_pfans.PFANS5000.mapper.PersonalProjectsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nt.utils.dao.TokenModel;
import java.util.List;
import java.util.UUID;


@Service
@Transactional(rollbackFor=Exception.class)
public class PersonalProjectsServiceImpl implements PersonalProjectsService {

    @Autowired
    private PersonalProjectsMapper personalprojectsMapper;

    @Override
    public List<PersonalProjects> getProjectList(PersonalProjects personalprojects) throws Exception {

        return personalprojectsMapper.select(personalprojects) ;
    }

    @Override
    public void delete(PersonalProjects personalprojects, TokenModel tokenModel) throws Exception {
        personalprojectsMapper.delete(personalprojects);
    }

    @Override
    public void insert(PersonalProjectsVo personalprojectsvo, TokenModel tokenModel) throws Exception {
        PersonalProjects personalprojects = new PersonalProjects();
        List<PersonalProjects> PersonalProjectslist = personalprojectsvo.getPersonalprojects();
        for(PersonalProjects personal:PersonalProjectslist){
            personalprojects.setProject_name(personal.getProject_name());
            personalprojects.setUser_id(personal.getUser_id());
            personalprojects.setProject_id(personal.getProject_id());
            personalprojects.setPersonalprojects_id(UUID.randomUUID().toString());
            personalprojects.preInsert(tokenModel);
            personalprojectsMapper.insert(personalprojects);
        }
    }

}

