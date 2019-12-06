package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface GivingMapper extends MyMapper<Giving> {

    List<OtherTwo2> selectOthertwo(@Param("givingid")String givingid);

    List<Attendance> selectAttendance(@Param("user_id")String user_id, @Param("years") String years, @Param("months") String months);


}
