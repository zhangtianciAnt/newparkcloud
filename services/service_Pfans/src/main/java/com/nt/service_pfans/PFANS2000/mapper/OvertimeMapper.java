package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

import java.util.List;

public interface OvertimeMapper extends MyMapper<Overtime> {
    List<Overtime> getOvertimeDay(@Param("overtimet") String overtimet,@Param("reserdate") String reserdate,@Param("userid") String userid);
    List<Overtime> getOvertimeOne(@Param("reserdate") String reserdate,@Param("userid") String userid);

    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认
    List<Overtime> selectOvertimeBystatusandUserid(@Param("userid") String userid);
    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认

}
