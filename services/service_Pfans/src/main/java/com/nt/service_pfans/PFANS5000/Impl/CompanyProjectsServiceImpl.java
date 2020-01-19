package com.nt.service_pfans.PFANS5000.Impl;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.OutSource;
import com.nt.dao_Pfans.PFANS5000.ProjectPlan;
import com.nt.dao_Pfans.PFANS5000.ProjectreSources;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.service_pfans.PFANS5000.mapper.OutSourceMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectplanMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectresourcesMapper;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyProjectsServiceImpl implements CompanyProjectsService {

    @Autowired
    private CompanyProjectsMapper companyprojectsMapper;
    @Autowired
    private ProjectplanMapper projectplanMapper;
    @Autowired
    private ProjectresourcesMapper projectresourcesMapper;

    @Autowired
    private OutSourceMapper outSourceMapper;

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Override
    public List<CompanyProjects> getCompanyProjectList(CompanyProjects companyprojects, HttpServletRequest request) throws Exception {
        return companyprojectsMapper.select(companyprojects);
    }


    @Override
    public List<CompanyProjects> list(CompanyProjects companyProjects) throws Exception {
        return companyprojectsMapper.select(companyProjects);
    }

    //按id查询
    @Override
    public CompanyProjectsVo selectById(String companyprojectsid) throws Exception {
        CompanyProjectsVo staffVo = new CompanyProjectsVo();
        ProjectPlan projectPlan = new ProjectPlan();
        ProjectreSources projectreSources = new ProjectreSources();
        OutSource outSource = new OutSource();
        projectPlan.setCompanyprojects_id(companyprojectsid);
        projectreSources.setCompanyprojects_id(companyprojectsid);
        outSource.setCompanyprojects_id(companyprojectsid);
        List<ProjectPlan> projectPlanList = projectplanMapper.select(projectPlan);
        List<ProjectreSources> projectreSourcesList = projectresourcesMapper.select(projectreSources);
        List<OutSource> outsourcesList = outSourceMapper.select(outSource);
        projectPlanList = projectPlanList.stream().sorted(Comparator.comparing(ProjectPlan::getRowindex)).collect(Collectors.toList());
        projectreSourcesList = projectreSourcesList.stream().sorted(Comparator.comparing(ProjectreSources::getRowindex)).collect(Collectors.toList());
        outsourcesList = outsourcesList.stream().sorted(Comparator.comparing(OutSource::getRowindex)).collect(Collectors.toList());
        CompanyProjects Staff = companyprojectsMapper.selectByPrimaryKey(companyprojectsid);
        staffVo.setCompanyprojects(Staff);
        staffVo.setProjectplan(projectPlanList);
        staffVo.setProjectresources(projectreSourcesList);
        staffVo.setOutSources(outsourcesList);
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
        OutSource out = new OutSource();
        pro.setCompanyprojects_id(companyprojectsid);
        sou.setCompanyprojects_id(companyprojectsid);
        out.setCompanyprojects_id(companyprojectsid);
        projectplanMapper.delete(pro);
        projectresourcesMapper.delete(sou);
        outSourceMapper.delete(out);
        List<ProjectPlan> projectPlanList = companyProjectsVo.getProjectplan();
        List<ProjectreSources> projectreSourcesList = companyProjectsVo.getProjectresources();
        List<OutSource> outSourceList = companyProjectsVo.getOutSources();
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
        if (outSourceList != null) {
            int rowindex = 0;
            for (OutSource outsource : outSourceList) {
                rowindex = rowindex + 1;
                outsource.preInsert(tokenModel);
                outsource.setOutsource_id(UUID.randomUUID().toString());
                outsource.setCompanyprojects_id(companyprojectsid);
                outsource.setRowindex(rowindex);
                outSourceMapper.insertSelective(outsource);
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
        List<OutSource> outSourceList = companyProjectsVo.getOutSources();
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
        if (outSourceList != null) {
            int rowindex = 0;
            for (OutSource outSource : outSourceList) {
                //对外驻人员登记表数据库进行操作
                Expatriatesinfor infor = expatriatesinforMapper.selectByPrimaryKey(outSource.getBpname());
                infor.setProject_name(companyProjects.getProject_name());
                infor.setManagerid(companyProjects.getManagerid());
                expatriatesinforMapper.updateByPrimaryKeySelective(infor);
                rowindex = rowindex + 1;
                outSource.preInsert(tokenModel);
                outSource.setOutsource_id(UUID.randomUUID().toString());
                outSource.setCompanyprojects_id(companyprojectsid);
                outSource.setRowindex(rowindex);
                outSourceMapper.insertSelective(outSource);
            }
        }
    }
}
