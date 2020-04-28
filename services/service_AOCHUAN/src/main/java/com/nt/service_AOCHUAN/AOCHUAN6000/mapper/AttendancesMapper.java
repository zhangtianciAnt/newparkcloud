package com.nt.service_AOCHUAN.AOCHUAN6000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Attendance;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AttendancesMapper extends MyMapper<Attendance> {
    public List<Attendance> getNowMon(@Param("attendancetim") String attendancetim);

    public List<Attendance> getByUserId(@Param("id") String userId,@Param("nowMons") String nowMons,@Param("lastMons") String lastMons);
}
