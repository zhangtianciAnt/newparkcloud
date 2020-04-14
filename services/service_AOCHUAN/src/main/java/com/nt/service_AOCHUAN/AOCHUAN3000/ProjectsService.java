package com.nt.service_AOCHUAN.AOCHUAN3000;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;

import java.util.List;

public interface ProjectsService {
    //获取projectscheduleList
    List<Projects> getProjectList(Projects projects) throws Exception;
    //获取记录表数据
    List<FollowUpRecord> getFollowUpRecordList(FollowUpRecord followUpRecord) throws Exception;
}
