package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface CompanyProjectsService {



    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects,HttpServletRequest request) throws Exception;
}
