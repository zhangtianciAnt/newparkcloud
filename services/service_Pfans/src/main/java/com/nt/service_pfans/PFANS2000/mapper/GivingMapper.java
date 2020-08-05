package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.BaseVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface GivingMapper extends MyMapper<Giving> {

    List<OtherTwo2> selectOthertwo(@Param("givingid")String givingid);

    List<Attendance> selectAttendance(@Param("user_id")String user_id, @Param("years") String years, @Param("months") String months);

    List<BaseVo> selectBase(@Param("dates")String dates);

    //add gbb 0805 生成工资时check考试数据(当月离职人员)
    Integer getAttendanceRetireCount(@Param("userIdList")List<String> userIdList);
    //add gbb 0805 生成工资时check考试数据(上月所有员工)
    Integer getAttendanceStatus();
}
