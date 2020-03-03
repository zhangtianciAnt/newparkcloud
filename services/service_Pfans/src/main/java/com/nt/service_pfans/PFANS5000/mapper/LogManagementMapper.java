package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.bytedeco.javacpp.opencv_core;

import java.util.List;

public interface LogManagementMapper extends MyMapper<LogManagement> {
    List<LogManagement> gettlist();
    List<LogManagement> getl(@Param("PROJECT_ID") String project_id);
}
