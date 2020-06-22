package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AttendanceMapper extends MyMapper<Attendance> {

    List<Attendance> getAttendance(Attendance attendance);
    List<Attendance> selectAttendance(AbNormal abNormal);
    Double selectAttenSumSick(Attendance attendance);

    void updStatus(@Param("user_id") String user_id, @Param("years") String years, @Param("months") String months);
    void updStatus1(@Param("user_id") String user_id, @Param("years") String years, @Param("months") String months);
}
