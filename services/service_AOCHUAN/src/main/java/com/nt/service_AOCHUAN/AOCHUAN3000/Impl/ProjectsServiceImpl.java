package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.service_AOCHUAN.AOCHUAN3000.ProjectsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.FollowUpRecordMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ProjectsMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectsServiceImpl implements ProjectsService {

    @Autowired
    private ProjectsMapper projectsMapper;

    @Autowired
    private FollowUpRecordMapper followUpRecordMapper;

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

    //新建
    @Override
    public void insert(Object object) throws Exception {
        //随机主键
        String id = UUID.randomUUID().toString();

        if (object instanceof Projects){
            //PROJECTS
            Projects projects = new Projects();
            BeanUtils.copyProperties(projects,object);
            projects.setProjects_id(id);
            projectsMapper.insert(projects);

        } else if(object instanceof FollowUpRecord){
            //FOLLOWUPRECORD
            FollowUpRecord followUpRecord = new FollowUpRecord();
            BeanUtils.copyProperties(followUpRecord,object);
            followUpRecord.setFollowuprecord_id(id);
            followUpRecordMapper.insert(followUpRecord);
        }
    }

    //更新
    @Override
    public void update(Object object) throws Exception {

        if (object instanceof Projects){

            Projects projects = new Projects();
            BeanUtils.copyProperties(projects,object);
            projectsMapper.updateByPrimaryKeySelective(projects);

        } else if(object instanceof FollowUpRecord){

            FollowUpRecord followUpRecord = new FollowUpRecord();
            BeanUtils.copyProperties(followUpRecord,object);
            followUpRecordMapper.updateByPrimaryKeySelective(followUpRecord);
        }
    }

    //删除
    @Override
    public void delete(String productName, String provider) throws Exception {
        //TABLE:PROJECTS
        projectsMapper.deleteByDoubleKey("projects",productName,provider);
        //TABLE:FOLLOWUPRECORD
        projectsMapper.deleteByDoubleKey("followuprecord",productName,provider);
    }
}
