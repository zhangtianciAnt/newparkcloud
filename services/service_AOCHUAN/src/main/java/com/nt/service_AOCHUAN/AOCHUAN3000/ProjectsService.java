package com.nt.service_AOCHUAN.AOCHUAN3000;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;

import java.util.List;

public interface ProjectsService {
    //获取projectscheduleList
    List<Projects> getProjectList(Projects projects) throws Exception;
    //获取记录表数据
    List<FollowUpRecord> getFollowUpRecordList(FollowUpRecord followUpRecord) throws Exception;
    //新建
    void insert(Object object)throws Exception;
    //更新
    void update(Object object) throws Exception;
    //删除
    void delete(String productName,String provider) throws Exception;
}
