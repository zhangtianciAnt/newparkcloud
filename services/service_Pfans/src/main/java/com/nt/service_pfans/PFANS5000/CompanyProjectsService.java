package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.dao_Pfans.PFANS5000.StageInformation;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo2;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface CompanyProjectsService {
    //新建
    void insert(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception;

    public List<CompanyProjects> list(CompanyProjects companyprojects) throws Exception;

    //编辑
    void update(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception;

    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects, HttpServletRequest request) throws Exception;

    //按id查询
    CompanyProjectsVo selectById(String companyprojectsid) throws Exception;

//    //    //附表查询
//    List<Projectsystem> select(String companyprojectsid) throws Exception;

    List<StageInformation> getstageInformation(StageInformation stageInformation) throws Exception;

    //现场管理list
    List<CompanyProjectsVo2> getSiteList() throws Exception;

    //PJ完了审批
    List<CompanyProjectsVo2> getPjList(String flag) throws Exception;

}
