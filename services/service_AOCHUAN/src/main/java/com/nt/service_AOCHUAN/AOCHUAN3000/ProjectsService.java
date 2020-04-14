package com.nt.service_AOCHUAN.AOCHUAN3000;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;

import java.util.List;

public interface ProjectsService {

    //获取projectscheduleList
    List<Projects> getProjectList(Projects projects) throws Exception;
}
