package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.utils.MyMapper;

import java.util.List;


public interface AttendanceMapper extends MyMapper<Attendance> {

    List<Attendance> getAttendance(Attendance attendance);
}
