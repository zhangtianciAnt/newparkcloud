package com.nt.service_pfans.PFANS5000.Impl;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
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
}

