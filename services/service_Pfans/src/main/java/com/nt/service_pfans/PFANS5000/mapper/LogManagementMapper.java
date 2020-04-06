package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementConfirmVo;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementVo2;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementStatusVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.bytedeco.javacpp.opencv_core;

import java.util.List;

public interface LogManagementMapper extends MyMapper<LogManagement> {
    List<LogManagement> gettlist();

    List<LogmanagementConfirmVo> getProjectList(@Param("owners")List<String> owners);

    List<LogmanagementVo2> getcheckList();

    List<LogmanagementVo2> getcheckList2();

    List<LogmanagementVo2> getcheckList3();

    List<LogmanagementConfirmVo> getunProjectList(@Param("StrDate") String StrDate,@Param("owners")List<String> owners);

    List<LogmanagementConfirmVo> getCenterList();

    List<LogmanagementStatusVo> getTimestart(@Param("project_id") String project_id,
                            @Param("starttime") String starttime,@Param("endtime") String endtime);

    List<LogmanagementStatusVo> getGroupTimestart(@Param("createby") List<String> createby,
                                             @Param("starttime") String starttime,@Param("endtime") String endtime);

    void updateTimestart(@Param("createby") String createby,@Param("confirmstatus") String confirmstatus,
                            @Param("starttime") String starttime,@Param("endtime") String endtime);
}
