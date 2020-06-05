package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Comproject;
import com.nt.dao_Pfans.PFANS5000.StageInformation;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo2;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo3;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanageMentVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface ComprojectService {
    //新建
    void insert(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception;

    public List<Comproject> list(Comproject comproject) throws Exception;

    public List<CompanyProjects> getPjnameList(CompanyProjects companyprojects) throws Exception;

    public LogmanageMentVo logmanageMentVo(CompanyProjects companyprojects) throws Exception;

    //编辑
    void update(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception;

    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects, HttpServletRequest request) throws Exception;

    //按id查询
    CompanyProjectsVo selectById(String companyprojectsid) throws Exception;

    List<StageInformation> getstageInformation(StageInformation stageInformation) throws Exception;

    //现场管理list
    List<CompanyProjectsVo2> getSiteList(CompanyProjects companyProjects) throws Exception;

    //PJ完了审批
    List<CompanyProjectsVo2> getPjList(String flag) throws Exception;

    List<Comproject> getList2(String flag)throws Exception;

    //获取外住人员所在的项目
    List<CompanyProjectsVo3> getCompanyProject(String SyspName)throws Exception;

    List<Comproject> getComproject(Comproject comproject) throws Exception;
    //add-ws-6/5-禅道075任务，项目名称问题修正
    List<Comproject> Listproject(Comproject comproject) throws Exception;
    List<CompanyProjects> Listproject2(CompanyProjects companyprojects) throws Exception;
    //add-ws-6/5-禅道075任务，项目名称问题修正
    //add-ws-阚总日志问题修正
    List<Comproject> getComproject2(Comproject comproject) throws Exception;
    //add-ws-阚总日志问题修正
}
