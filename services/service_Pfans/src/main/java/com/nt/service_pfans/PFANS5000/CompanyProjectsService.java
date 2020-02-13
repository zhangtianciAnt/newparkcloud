package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface CompanyProjectsService {
    //新建
    void insert(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception;

    public List<CompanyProjects> list(CompanyProjects talentPlan) throws Exception;
    //编辑
    void update(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception;

    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects,HttpServletRequest request) throws Exception;
    //按id查询
    CompanyProjectsVo selectById(String companyprojectsid) throws Exception;
}
