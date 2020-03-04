package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementStatusVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.bytedeco.javacpp.opencv_core;

import java.util.List;

public interface LogManagementMapper extends MyMapper<LogManagement> {
    List<LogManagement> gettlist();

    List<LogmanagementStatusVo> getTimestart(@Param("project_id") String project_id);

    void updateTimestart(@Param("createby") String createby,@Param("confirmstatus") String confirmstatus,
                            @Param("starttime") String starttime,@Param("endtime") String endtime);
}
