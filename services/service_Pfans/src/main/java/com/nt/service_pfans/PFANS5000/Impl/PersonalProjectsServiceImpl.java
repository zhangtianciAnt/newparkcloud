package com.nt.service_pfans.PFANS5000.Impl;


import com.nt.dao_Pfans.PFANS5000.PersonalProjects;
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
    public void insert(PersonalProjects personalprojects, TokenModel tokenModel) throws Exception {
        personalprojects.preInsert(tokenModel);
        personalprojects.setPersonalprojects_id(UUID.randomUUID().toString());
        personalprojectsMapper.insert(personalprojects);
    }

    @Override
    public void delete(PersonalProjects personal, TokenModel tokenModel) throws Exception{

        personalprojectsMapper.delete(personal);
    }
}

