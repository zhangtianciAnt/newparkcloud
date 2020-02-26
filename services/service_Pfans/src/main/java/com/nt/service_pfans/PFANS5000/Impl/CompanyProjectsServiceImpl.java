package com.nt.service_pfans.PFANS5000.Impl;

import com.nt.dao_Pfans.PFANS5000.*;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo2;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.mapper.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyProjectsServiceImpl implements CompanyProjectsService {

    @Autowired
    private CompanyProjectsMapper companyprojectsMapper;
    @Autowired
    private StageInformationMapper stageinformationMapper;
    @Autowired
    private ProjectsystemMapper projectsystemMapper;
    @Autowired
    private ProjectContractMapper projectcontractMapper;

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
        //项目计划
        StageInformation stageInformation = new StageInformation();
        //项目体制
        Projectsystem projectsystem = new Projectsystem();
        //项目合同
        ProjectContract projectcontract = new ProjectContract();
        projectsystem.setCompanyprojects_id(companyprojectsid);
        stageInformation.setCompanyprojects_id(companyprojectsid);
        projectcontract.setCompanyprojects_id(companyprojectsid);
        CompanyProjects companyprojects = companyprojectsMapper.selectByPrimaryKey(companyprojectsid);
        List<Projectsystem> projectsystemList = projectsystemMapper.select(projectsystem);
        List<StageInformation> stageinformationList = stageinformationMapper.select(stageInformation);
        List<ProjectContract> projectcontractList = projectcontractMapper.select(projectcontract);
        projectsystemList = projectsystemList.stream().sorted(Comparator.comparing(Projectsystem::getRowindex)).collect(Collectors.toList());
        stageinformationList = stageinformationList.stream().sorted(Comparator.comparing(StageInformation::getRowindex)).collect(Collectors.toList());
        projectcontractList = projectcontractList.stream().sorted(Comparator.comparing(ProjectContract::getRowindex)).collect(Collectors.toList());
        staffVo.setCompanyprojects(companyprojects);
        staffVo.setProjectsystem(projectsystemList);
        staffVo.setStageinformation(stageinformationList);
        staffVo.setProjectcontract(projectcontractList);
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
        //项目计划
        List<StageInformation> stageinformationList = companyProjectsVo.getStageinformation();
        if (stageinformationList != null) {
            StageInformation stageinformation = new StageInformation();
            stageinformation.setCompanyprojects_id(companyprojectsid);
            stageinformationMapper.delete(stageinformation);
            int rowindex = 0;
            for (StageInformation sta : stageinformationList) {
                rowindex = rowindex + 1;
                sta.preInsert(tokenModel);
                sta.setStageinformation_id(UUID.randomUUID().toString());
                sta.setCompanyprojects_id(companyprojectsid);
                sta.setRowindex(rowindex);
                stageinformationMapper.insertSelective(sta);
            }
        }
        //项目体制
        List<Projectsystem> projectsystemList = companyProjectsVo.getProjectsystem();
        if (projectsystemList != null) {
            Projectsystem projectsystem = new Projectsystem();
            projectsystem.setCompanyprojects_id(companyprojectsid);
            projectsystemMapper.delete(projectsystem);
            int rowundex = 0;
            for (Projectsystem pro : projectsystemList) {
                rowundex = rowundex + 1;
                pro.preInsert(tokenModel);
                pro.setProjectsystem_id(UUID.randomUUID().toString());
                pro.setCompanyprojects_id(companyprojectsid);
                pro.setRowindex(rowundex);
                projectsystemMapper.insertSelective(pro);
            }
        }
        //项目合同
        List<ProjectContract> projectcontractList = companyProjectsVo.getProjectcontract();
        if (projectcontractList != null) {
            ProjectContract projectcontract = new ProjectContract();
            projectcontract.setCompanyprojects_id(companyprojectsid);
            projectcontractMapper.delete(projectcontract);
            int rowundex = 0;
            for (ProjectContract pro : projectcontractList) {
                rowundex = rowundex + 1;
                pro.preInsert(tokenModel);
                pro.setProjectcontract_id(UUID.randomUUID().toString());
                pro.setCompanyprojects_id(companyprojectsid);
                pro.setRowindex(rowundex);
                projectcontractMapper.insertSelective(pro);
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
        //项目计划
        List<StageInformation> stageInformationList = companyProjectsVo.getStageinformation();
        //项目体制
        List<Projectsystem> projectsystemList = companyProjectsVo.getProjectsystem();
        //项目合同
        List<ProjectContract> projectcontractList = companyProjectsVo.getProjectcontract();
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
        if (projectcontractList != null) {
            int rowundex = 0;
            for (ProjectContract projectcontract : projectcontractList) {
                rowundex = rowundex + 1;
                projectcontract.preInsert(tokenModel);
                projectcontract.setProjectcontract_id(UUID.randomUUID().toString());
                projectcontract.setCompanyprojects_id(companyprojectsid);
                projectcontract.setRowindex(rowundex);
                projectcontractMapper.insertSelective(projectcontract);
            }
        }
    }

    /**
     * 开发计划查询列表
     * @param stageInformation
     * @return
     * @throws Exception
     */
    @Override
    public List<StageInformation> getstageInformation(StageInformation stageInformation) throws Exception {
        return stageinformationMapper.select(stageInformation);
    }

    /**
     * 现场管理列表
     * @作者：zy
     * @return List<CompanyProjectsVo2>
     */
    @Override
    public List<CompanyProjectsVo2> getSiteList() throws Exception {
        return companyprojectsMapper.getList();
    }

    /**
     * PJ完了审批列表
     * @作者：zy
     * @return List<CompanyProjectsVo2>
     */
    @Override
    public List<CompanyProjectsVo2> getPjList() throws Exception {
        CompanyProjects companyProjects = new CompanyProjects();
        List<CompanyProjects> companyProjectsList = companyprojectsMapper.select(companyProjects); // 主表，大部分数据都从这个里来
        //获取阶段信息实际开始时间，结束时间
        StageInformation stageInformation = new StageInformation();
        List<StageInformation> stageInformationList = stageinformationMapper.select(stageInformation);// 这个表取开始时间，结束时间，放到主表里

        List<CompanyProjectsVo2> listVo2 = companyprojectsMapper.getListVo2();
        Map<String, String> mapVo2 = new HashMap<>();
        for ( CompanyProjectsVo2 vo2 : listVo2 ) {
            String key = vo2.getCompanyprojects_id();
            String value = vo2.getContractnumber();
            if ( !mapVo2.containsKey(key) ) {
                mapVo2.put(key, value);
            }
        }

        Map<String, StageInformation> map = new HashMap<>();
        for ( StageInformation info : stageInformationList ) {
            String key = info.getCompanyprojects_id();
            if ( !map.containsKey(key) ) {
                map.put(key, info);
            } else {
                StageInformation oldVo = map.get(key);
                Date oStart = oldVo.getActualstarttime();
                Date oEnd = oldVo.getActualendtime();

                if(info.getActualstarttime() != null) {
                    if ( info.getActualstarttime().before(oStart) ) {
                        oldVo.setActualstarttime(info.getActualstarttime());
                    }
                }
                if(info.getActualendtime() != null ){
                    if ( info.getActualendtime().after(oEnd)) {
                        oldVo.setActualendtime(info.getActualendtime());
                    }
                }
            }
        }

        List<CompanyProjectsVo2> result = new ArrayList<>();

        for ( CompanyProjects projects : companyProjectsList) {
            CompanyProjectsVo2 vo = new CompanyProjectsVo2();
            vo.setCompanyprojects_id(projects.getCompanyprojects_id());
            //契约番号
            if ( mapVo2.containsKey(projects.getCompanyprojects_id()) ) {
                vo.setContractnumber(mapVo2.get(projects.getCompanyprojects_id()));
            }

            vo.setEntrust(projects.getEntrust());//委托元
            vo.setField(projects.getField());//项目分野
            vo.setLeaderid(projects.getLeaderid());//PL
            vo.setNumbers(projects.getNumbers());//项目编号
            vo.setProject_name(projects.getProject_name());//项目名称
            vo.setStatus(projects.getStatus());//审批状态
            vo.setWork(projects.getWork());//受托工数
            StageInformation info = map.get(projects.getCompanyprojects_id());
            if ( info!=null ) {
                vo.setActualstarttime(info.getActualstarttime());
                vo.setActualendtime(info.getActualendtime());
            }

            result.add(vo);
        }
        return result;
    }

}
