package com.nt.service_pfans.PFANS5000.Impl;


import com.nt.dao_Pfans.PFANS5000.ProjectInformation;
import com.nt.dao_Pfans.PFANS5000.StageInformation;
import com.nt.dao_Pfans.PFANS5000.Vo.ProjectInformationVo;
import com.nt.service_pfans.PFANS5000.ProjectInformationService;
import com.nt.service_pfans.PFANS5000.mapper.ProjectInformationMapper;
import com.nt.service_pfans.PFANS5000.mapper.StageInformationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class ProjectInformationServiceImpl implements ProjectInformationService {

    @Autowired
    private ProjectInformationMapper projectinformationMapper;
    @Autowired
    private StageInformationMapper stageinformationMapper;

    @Override
    public List<ProjectInformation> getProjectInformation(ProjectInformation projectinformation) throws Exception {
        return projectinformationMapper.select(projectinformation);
    }

    @Override
    public ProjectInformationVo selectById(String projectinformation_id) throws Exception {
        ProjectInformationVo piVo = new ProjectInformationVo();
        StageInformation stageinformation = new StageInformation();
        stageinformation.setProjectinformation_id(projectinformation_id);
        List<StageInformation> stageinformationlist = stageinformationMapper.select(stageinformation);
        stageinformationlist = stageinformationlist.stream().sorted(Comparator.comparing(StageInformation::getRowindex)).collect(Collectors.toList());
        ProjectInformation Pi = stageinformationMapper.selectByPrimaryKey(projectinformation_id);
        piVo.setProjectinformation(Pi);
        piVo.setStageinformation(stageinformationlist);
        return piVo;
    }

    @Override
    public void updateProjectInformationVo(ProjectInformationVo projectinformationVo, TokenModel tokenModel) throws Exception {
        ProjectInformation projectinformation = new ProjectInformation();
        BeanUtils.copyProperties(projectinformationVo.getProjectinformation(), projectinformation);
        projectinformation.preUpdate(tokenModel);
        projectinformationMapper.updateByPrimaryKey(projectinformation);
        String projectinformation_id = projectinformation.projectinformation_id();
        StageInformation si = new StageInformation();
        si.setProjectinformation_id(projectinformation_id);
        stageinformationMapper.delete(si);
        List<StageInformation> stageinformationlist = projectinformationVo.getStageinformation();
        if (stageinformationlist != null) {
            int rowundex = 0;
            for (StageInformation stageinformation : stageinformationlist) {
                rowundex = rowundex + 1;
                stageinformation.preInsert(tokenModel);
                stageinformation.setstageinformation_id(UUID.randomUUID().toString());
                stageinformation.setProjectinformation_id(projectinformation_id);
                stageinformation.setRowindex(rowundex);
                stageinformationMapper.insertSelective(stageinformation);
            }
        }


    @Override
    public void insertProjectInformationVo(ProjectInformationVo projectinformationVo, TokenModel tokenModel) throws Exception {
        String projectinformation_id = UUID.randomUUID().toString();
        ProjectInformation projectinformation = new ProjectInformation();
        BeanUtils.copyProperties(ProjectInformationVo.getProjectinformation(), projectinformation);
        projectinformation.preInsert(tokenModel);
        projectinformation.setProjectinformation_id(projectinformation_id);
        projectinformationMapper.insertSelective(projectinformation);
        List<StageInformation> stageinformationlist = projectinformationVo.getStageinformation();
        if (stageinformationlist != null) {
            int rowundex = 0;
            for (StageInformation stageinformation : stageinformationlist) {
                rowundex = rowundex + 1;
                stageinformation.preInsert(tokenModel);
                stageinformation.setStageinformation_id(UUID.randomUUID().toString());
                stageinformation.setProjectinformation_id(projectinformation_id);
                stageinformation.setRowindex(rowundex);
                stageinformationMapper.insertSelective(stageinformation);
            }
        }

    }

}
