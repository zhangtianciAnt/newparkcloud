package com.nt.service_pfans.PFANS5000.Impl;

import com.nt.dao_Pfans.PFANS5000.ProjectPlan;
import com.nt.dao_Pfans.PFANS5000.ProjectreSources;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectplanMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectresourcesMapper;
import org.springframework.beans.BeanUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor=Exception.class)
public class CompanyProjectsServiceImpl implements CompanyProjectsService {

    @Autowired
    private CompanyProjectsMapper companyprojectsMapper;
    @Autowired
    private ProjectplanMapper projectplanMapper;
    @Autowired
    private ProjectresourcesMapper projectresourcesMapper;
    @Override
    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects,HttpServletRequest request) throws Exception {

        return companyprojectsMapper.select(companyprojects) ;
    }


    @Override
    public List<CompanyProjects> list(CompanyProjects companyProjects ) throws Exception {
        return companyprojectsMapper.select(companyProjects);
    }
    //按id查询
    @Override
    public CompanyProjectsVo selectById(String companyprojectsid) throws Exception {
        CompanyProjectsVo staffVo = new CompanyProjectsVo();
        ProjectPlan projectPlan = new ProjectPlan();
        ProjectreSources projectreSources = new ProjectreSources();
        projectPlan.setCompanyprojects_id(companyprojectsid);
        projectreSources.setCompanyprojects_id(companyprojectsid);
        List<ProjectPlan> projectPlanList = projectplanMapper.select(projectPlan);
        List<ProjectreSources> projectreSourcesList = projectresourcesMapper.select(projectreSources);
        projectPlanList = projectPlanList.stream().sorted(Comparator.comparing(ProjectPlan::getRowindex)).collect(Collectors.toList());
        projectreSourcesList = projectreSourcesList.stream().sorted(Comparator.comparing(ProjectreSources::getRowindex)).collect(Collectors.toList());
        CompanyProjects Staff = companyprojectsMapper.selectByPrimaryKey(companyprojectsid);
        staffVo.setCompanyprojects(Staff);
        staffVo.setProjectplan(projectPlanList);
        staffVo.setProjectresources(projectreSourcesList);
        return staffVo;
    }

    //更新
    @Override
    public void update(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception {
        CompanyProjects companyProjects = new CompanyProjects();
        BeanUtils.copyProperties(companyProjectsVo.getCompanyprojects(), companyProjects);
        companyProjects.preUpdate(tokenModel);
        companyprojectsMapper.updateByPrimaryKey(companyProjects);
        String companyprojectsid = companyProjects.getCompanyprojects_id();
        ProjectPlan pro = new ProjectPlan();
        ProjectreSources sou = new ProjectreSources();
        pro.setCompanyprojects_id(companyprojectsid);
        projectplanMapper.delete(pro);
        projectresourcesMapper.delete(sou);
        List<ProjectPlan> projectPlanList = companyProjectsVo.getProjectplan();
        List<ProjectreSources> projectreSourcesList = companyProjectsVo.getProjectresources();
        if (projectPlanList != null) {
            int rowundex = 0;
            for (ProjectPlan projectPlan : projectPlanList) {
                rowundex = rowundex + 1;
                projectPlan.preInsert(tokenModel);
                projectPlan.setProjectplan_id(UUID.randomUUID().toString());
                projectPlan.setCompanyprojects_id(companyprojectsid);
                projectPlan.setRowindex(rowundex);
                projectplanMapper.insertSelective(projectPlan);
            }
        }
        if (projectreSourcesList != null) {
            int rowundex = 0;
            for (ProjectreSources projectreSources : projectreSourcesList) {
                rowundex = rowundex + 1;
                projectreSources.preInsert(tokenModel);
                projectreSources.setProjectresources_id(UUID.randomUUID().toString());
                projectreSources.setCompanyprojects_id(companyprojectsid);
                projectreSources.setRowindex(rowundex);
                projectresourcesMapper.insertSelective(projectreSources);
            }
        }
    }
    //新建
    @Override
    public void insert(CompanyProjectsVo companyProjectsVo, TokenModel tokenModel) throws Exception {
        String companyprojectsid = UUID.randomUUID().toString();
        CompanyProjects companyProjects = new CompanyProjects();
        BeanUtils.copyProperties(companyProjectsVo.getCompanyprojects(), companyProjects);
        companyProjects.preInsert(tokenModel);
        companyProjects.setCompanyprojects_id(companyprojectsid);
        companyprojectsMapper.insertSelective(companyProjects);
        List<ProjectPlan> projectPlanList = companyProjectsVo.getProjectplan();
        List<ProjectreSources> projectreSourcesList = companyProjectsVo.getProjectresources();
        if (projectPlanList != null) {
            int rowundex = 0;
            for (ProjectPlan projectPlan : projectPlanList) {
                rowundex = rowundex + 1;
                projectPlan.preInsert(tokenModel);
                projectPlan.setProjectplan_id(UUID.randomUUID().toString());
                projectPlan.setCompanyprojects_id(companyprojectsid);
                projectPlan.setRowindex(rowundex);
                projectplanMapper.insertSelective(projectPlan);
            }
        }
        if (projectreSourcesList != null) {
            int rowundex = 0;
            for (ProjectreSources projectreSources : projectreSourcesList) {
                rowundex = rowundex + 1;
                projectreSources.preInsert(tokenModel);
                projectreSources.setProjectresources_id(UUID.randomUUID().toString());
                projectreSources.setCompanyprojects_id(companyprojectsid);
                projectreSources.setRowindex(rowundex);
                projectresourcesMapper.insertSelective(projectreSources);
            }
        }
    }
}
