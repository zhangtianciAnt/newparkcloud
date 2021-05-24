package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementConfirmVo;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementVo2;
import com.nt.dao_Pfans.PFANS5000.Vo.LogmanagementStatusVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.bytedeco.javacpp.opencv_core;

import java.util.Date;
import java.util.List;

public interface LogManagementMapper extends MyMapper<LogManagement> {
    List<LogManagement> gettlist();

    //add_fjl_0716_添加PL权限的人查看日志一览  start
    List<LogManagement> getListPLlogman(@Param("owner") String owner);
    //add_fjl_0716_添加PL权限的人查看日志一览  end

    //add ccm 1118 日志优化
    List<LogManagement> getDataListByLog_date(@Param("owners") List<String> owners, @Param("log_date") String log_date);
    //add ccm 1118 日志优化

    List<LogManagement> getOrgOwnerList(@Param("group_id") String group_id, @Param("log_date") String log_date);

    List<LogManagement> selectByDate(@Param("owners") List<String> owners, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<LogmanagementConfirmVo> getProjectList(@Param("owners") List<String> owners, @Param("userid") String userid);

    List<LogmanagementVo2> getcheckList();

    List<LogmanagementVo2> getcheckList2();

    List<LogmanagementVo2> getcheckList3();

    List<LogmanagementConfirmVo> getunProjectList(@Param("StrDate") String StrDate, @Param("owners") List<String> owners);

    List<LogmanagementConfirmVo> getCenterList();

    List<LogmanagementStatusVo> getTimestart(@Param("project_id") String project_id,
                                             @Param("starttime") String starttime, @Param("endtime") String endtime);

    List<LogmanagementStatusVo> getGroupTimestart(@Param("createby") List<String> createby,
                                                  @Param("starttime") String starttime, @Param("endtime") String endtime);

    void updateTimestart(@Param("createby") String createby, @Param("confirmstatus") String confirmstatus,
                         @Param("starttime") String starttime, @Param("endtime") String endtime);


    @Select("select time_start from logmanagement where  DATE_FORMAT(LOG_DATE, '%Y-%m') = #{logdate} and createby=#{createby}")
    List<LogManagement> selectsum(@Param("createby") String createby, @Param("logdate") String logdate);


}
