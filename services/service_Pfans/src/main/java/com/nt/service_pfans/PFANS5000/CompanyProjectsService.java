package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface CompanyProjectsService {
    public void insert(CompanyProjects companyProjects, TokenModel tokenModel)throws Exception;
    public List<CompanyProjects> list(CompanyProjects talentPlan) throws Exception;
    public void upd(CompanyProjects companyProjects, TokenModel tokenModel)throws Exception;
    CompanyProjects One(String companyprojects_id) throws Exception;
    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects,HttpServletRequest request) throws Exception;
}
