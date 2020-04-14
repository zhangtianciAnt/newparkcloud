package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.service_AOCHUAN.AOCHUAN3000.ProjectsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ProjectsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectsServiceImpl implements ProjectsService {

    @Autowired
    private ProjectsMapper projectsMapper;

    //获取projectscheduleList
    @Override
    public List<Projects> getProjectList(Projects projects) throws Exception {
        return projectsMapper.select(projects);
    }

    //获取记录表数据
    @Override
    public List<FollowUpRecord> getFollowUpRecordList(FollowUpRecord followUpRecord) throws Exception {

        String productName = followUpRecord.getProduct_nm();
        String provider = followUpRecord.getProvider();
        return projectsMapper.getFollowUpRecordList(productName,provider);
    }
}
