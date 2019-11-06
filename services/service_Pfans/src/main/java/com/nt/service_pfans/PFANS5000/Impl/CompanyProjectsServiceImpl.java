package com.nt.service_pfans.PFANS5000.Impl;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;


@Service
@Transactional(rollbackFor=Exception.class)
public class CompanyProjectsServiceImpl implements CompanyProjectsService {

    @Autowired
    private CompanyProjectsMapper companyprojectsMapper;

    @Override
    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects,HttpServletRequest request) throws Exception {

        return companyprojectsMapper.select(companyprojects) ;
    }

    @Override
    public List<CompanyProjects> list(CompanyProjects companyProjects ) throws Exception {
        return companyprojectsMapper.select(companyProjects);
    }
    @Override
    public CompanyProjects One(String companyprojects_id) throws Exception {

        CompanyProjects log   =companyprojectsMapper.selectByPrimaryKey(companyprojects_id);
        return log;
    }

    @Override
    public void upd (CompanyProjects companyProjects, TokenModel tokenModel) throws Exception {
        companyProjects.preUpdate(tokenModel);
        companyprojectsMapper.updateByPrimaryKeySelective(companyProjects);
    }
    @Override
    public void insert(CompanyProjects companyProjects, TokenModel tokenModel) throws Exception {
        companyProjects.preInsert(tokenModel);
        companyProjects.setCompanyprojects_id(UUID.randomUUID().toString());
        companyprojectsMapper.insert(companyProjects);
    }
}
