package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.service_AOCHUAN.AOCHUAN3000.ProjectsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ProjectsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectsServiceImpl implements ProjectsService {

    private ProjectsMapper projectsMapper;
    private Projects projects;

    //获取projectscheduleList
    @Override
    public List<Projects> getProjectList(Projects projects) throws Exception {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx123456");
        return projectsMapper.select(projects);
    }
}
