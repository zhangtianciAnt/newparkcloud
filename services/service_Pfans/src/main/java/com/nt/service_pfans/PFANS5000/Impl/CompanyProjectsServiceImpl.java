package com.nt.service_pfans.PFANS5000.Impl;

import com.nt.dao_Pfans.PFANS5000.*;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.mapper.*;
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
    private ProjectsystemMapper projectsystemMapper;

    @Autowired
    private OutSourceMapper outSourceMapper;
    @Autowired
    private StageInformationMapper stageinformationMapper;

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
        Projectsystem projectsystem = new Projectsystem();
        OutSource outSource = new OutSource();
        StageInformation stageInformation = new StageInformation();
        projectPlan.setCompanyprojects_id(companyprojectsid);
        projectsystem.setCompanyprojects_id(companyprojectsid);
        outSource.setCompanyprojects_id(companyprojectsid);
        stageInformation.setCompanyprojects_id(companyprojectsid);
        List<ProjectPlan> projectPlanList = projectplanMapper.select(projectPlan);
        List<Projectsystem> projectsystemList = projectsystemMapper.select(projectsystem);
        List<OutSource> outsourcesList = outSourceMapper.select(outSource);
        List<StageInformation> stageinformationList = stageinformationMapper.select(stageInformation);
        projectPlanList = projectPlanList.stream().sorted(Comparator.comparing(ProjectPlan::getRowindex)).collect(Collectors.toList());
        projectsystemList = projectsystemList.stream().sorted(Comparator.comparing(Projectsystem::getRowindex)).collect(Collectors.toList());
        outsourcesList = outsourcesList.stream().sorted(Comparator.comparing(OutSource::getRowindex)).collect(Collectors.toList());
        stageinformationList = stageinformationList.stream().sorted(Comparator.comparing(StageInformation::getRowindex)).collect(Collectors.toList());
        CompanyProjects Staff = companyprojectsMapper.selectByPrimaryKey(companyprojectsid);
        staffVo.setCompanyprojects(Staff);
        staffVo.setProjectplan(projectPlanList);
        staffVo.setProjectsystem(projectsystemList);
        staffVo.setOutSources(outsourcesList);
        staffVo.setStageinformation(stageinformationList);
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
//        ProjectPlan pro = new ProjectPlan();
//        Projectsystem sou = new Projectsystem();
//        OutSource out = new OutSource();
//        StageInformation sta = new StageInformation();
//        pro.setCompanyprojects_id(companyprojectsid);
//        sou.setCompanyprojects_id(companyprojectsid);
//        out.setCompanyprojects_id(companyprojectsid);
//        sta.setCompanyprojects_id(companyprojectsid);
//        projectplanMapper.delete(pro);
//        projectsystemMapper.delete(sou);
//        outSourceMapper.delete(out);
//        stageinformationMapper.delete(sta);
        List<ProjectPlan> projectPlanList = companyProjectsVo.getProjectplan();
        List<Projectsystem> projectsystemList = companyProjectsVo.getProjectsystem();
        List<OutSource> outSourceList = companyProjectsVo.getOutSources();
        List<StageInformation> stageinformationList = companyProjectsVo.getStageinformation();
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
        if (projectsystemList != null) {
            int rowundex = 0;
            for (Projectsystem projectsystem : projectsystemList) {
                rowundex = rowundex + 1;
                projectsystem.preInsert(tokenModel);
                projectsystem.setProjectsystem_id(UUID.randomUUID().toString());
                projectsystem.setCompanyprojects_id(companyprojectsid);
                projectsystem.setRowindex(rowundex);
                projectsystemMapper.insertSelective(projectsystem);
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
        if (stageinformationList != null) {
            int rowindex = 0;
            for (StageInformation stageinformation : stageinformationList) {
                rowindex = rowindex + 1;
                stageinformation.preInsert(tokenModel);
                stageinformation.setStageinformation_id(UUID.randomUUID().toString());
                stageinformation.setCompanyprojects_id(companyprojectsid);
                stageinformation.setRowindex(rowindex);
                stageinformationMapper.insertSelective(stageinformation);
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
        List<Projectsystem> projectsystemList = companyProjectsVo.getProjectsystem();
        List<OutSource> outSourceList = companyProjectsVo.getOutSources();
        List<StageInformation> stageInformationList = companyProjectsVo.getStageinformation();
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
        if (projectsystemList != null) {
            int rowundex = 0;
            for (Projectsystem projectsystem : projectsystemList) {
                rowundex = rowundex + 1;
                projectsystem.preInsert(tokenModel);
                projectsystem.setProjectsystem_id(UUID.randomUUID().toString());
                projectsystem.setCompanyprojects_id(companyprojectsid);
                projectsystem.setRowindex(rowundex);
                projectsystemMapper.insertSelective(projectsystem);
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
        if (stageInformationList != null) {
            int rowundex = 0;
            for (StageInformation stageInformation : stageInformationList) {
                rowundex = rowundex + 1;
                stageInformation.preInsert(tokenModel);
                stageInformation.setStageinformation_id(UUID.randomUUID().toString());
                stageInformation.setCompanyprojects_id(companyprojectsid);
                stageInformation.setRowindex(rowundex);
                stageinformationMapper.insertSelective(stageInformation);
            }
        }
    }
}
