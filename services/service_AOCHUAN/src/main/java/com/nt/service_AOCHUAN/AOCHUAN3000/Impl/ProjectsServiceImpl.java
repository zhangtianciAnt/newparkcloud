package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.service_AOCHUAN.AOCHUAN3000.ProjectsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.FollowUpRecordMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ProjectsMapper;
import com.nt.utils.dao.TokenModel;
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
    public List<Projects> getProjectList() throws Exception {
        return projectsMapper.getProjectList();
    }

    //获取记录表数据
    @Override
    public List<FollowUpRecord> getFollowUpRecordList(FollowUpRecord followUpRecord) throws Exception {

        String products_id = followUpRecord.getProducts_id();
        String supplier_id = followUpRecord.getSupplier_id();
        return followUpRecordMapper.getFollowUpRecordList(products_id,supplier_id,"0");
    }

    //新建
    @Override
    public void insert(Object object, TokenModel tokenModel) throws Exception {

        if (object instanceof Projects){
            //PROJECTS
            Projects projects = new Projects();
            BeanUtils.copyProperties(projects,object);

            projects.preInsert(tokenModel);
            projectsMapper.insert(projects);

        } else if(object instanceof FollowUpRecord){
            //FOLLOWUPRECORD
            FollowUpRecord followUpRecord = new FollowUpRecord();
            BeanUtils.copyProperties(followUpRecord,object);

            followUpRecord.preInsert(tokenModel);
            followUpRecordMapper.insert(followUpRecord);
        }
    }

    //更新
    @Override
    public void update(Object object, TokenModel tokenModel) throws Exception {

        if (object instanceof Projects){

            Projects projects = new Projects();
            BeanUtils.copyProperties(projects,object);
            projects.preUpdate(tokenModel);
            projectsMapper.updateByPrimaryKeySelective(projects);

        } else if(object instanceof FollowUpRecord){

            FollowUpRecord followUpRecord = new FollowUpRecord();
            BeanUtils.copyProperties(followUpRecord,object);
            followUpRecord.preUpdate(tokenModel);
            followUpRecordMapper.updateByPrimaryKeySelective(followUpRecord);
        }
    }

    //删除
    @Override
    public void delete(Object object, TokenModel tokenModel) throws Exception {

        if (object instanceof Projects){

            Projects projects = new Projects();
            BeanUtils.copyProperties(projects,object);
            projects.preUpdate(tokenModel);
            projectsMapper.deleteFromProjectsByDoubleKey(projects.getModifyby(),projects.getProducts_id(),projects.getSupplier_id(),"1");

        } else if(object instanceof FollowUpRecord){

            FollowUpRecord followUpRecord = new FollowUpRecord();
            BeanUtils.copyProperties(followUpRecord,object);
            followUpRecord.preUpdate(tokenModel);
            followUpRecordMapper.deleteFromFollowUpRecordByDoubleKey(followUpRecord.getModifyby(),followUpRecord.getProducts_id(),followUpRecord.getSupplier_id(),"1");
        }
    }

    //存在Check
    @Override
    public Boolean existCheck(Object object) throws Exception {

        if (object instanceof Projects){
            Projects projects = new Projects();
            BeanUtils.copyProperties(projects,object);
           List<Projects> resultLst =  projectsMapper.existCheck(projects.getProjects_id(),"0");
           if (resultLst.isEmpty()){
               return false;
           }

        } else if(object instanceof FollowUpRecord){
            FollowUpRecord followUpRecord = new FollowUpRecord();
            BeanUtils.copyProperties(followUpRecord,object);
            List<FollowUpRecord> resultLst =  followUpRecordMapper.existCheck(followUpRecord.getFollowuprecord_id(),"0");
            if (resultLst.isEmpty()){
                return false;
            }
        }

        return true;
    }

    //唯一性Check
    @Override
    public Boolean uniqueCheck(Projects projects) throws Exception {
        List<Projects> resultLst = projectsMapper.uniqueCheck(projects.getProjects_id(), projects.getProducts_id(),projects.getSupplier_id());

        if (resultLst.isEmpty()){
            return false;
        }

        return true;
    }

}
