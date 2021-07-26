package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS5000.*;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo2;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo3;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanageMentVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface CompanyProjectsService {
    //新建
    void insert(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception;

    public List<CompanyProjects> list(CompanyProjects companyprojects) throws Exception;

    public List<CompanyProjects> getPjnameList(CompanyProjects companyprojects) throws Exception;

    public List<CompanyProjects> getPjnameList6007(String account) throws Exception;
    public List<CompanyProjects> listPsdcd(String numbers) throws Exception;

    public List<Comproject> getPjnameList6007_1(String account) throws Exception;

    public LogmanageMentVo logmanageMentVo(CompanyProjects companyprojects) throws Exception;

    //编辑
    void update(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception;

    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects, HttpServletRequest request) throws Exception;

    //按id查询
    CompanyProjectsVo selectById(String companyprojectsid) throws Exception;

    Contractnumbercount selectConnumList(String contractnumbercount_id) throws Exception;

    List<ProjectContract> selectAll() throws Exception;
    List<StageInformation> getstageInformation(StageInformation stageInformation) throws Exception;

    //现场管理list
    List<CompanyProjectsVo2> getSiteList(CompanyProjects companyProjects) throws Exception;
    List<CompanyProjectsVo2> getSiteList2(CompanyProjects companyProjects) throws Exception;
    List<CompanyProjectsVo2> getSiteList3(CompanyProjects companyProjects) throws Exception;
    List<CompanyProjectsVo2> getSiteList4(CompanyProjects companyProjects) throws Exception;
    //PJ完了审批
    List<CompanyProjectsVo2> getPjList(String flag) throws Exception;

    List<CompanyProjectsVo2> getList2(String flag, List<String> ownerList, String owner) throws Exception;

    //获取外住人员所在的项目
    List<CompanyProjectsVo3> getCompanyProject (String SyspName)throws Exception;

    void getPLLeader() throws Exception;
}
