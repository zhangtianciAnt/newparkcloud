package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AttendanceService {

    List<Attendance> getlist(Attendance attendance) throws Exception;

    List<Attendance> getAttendancelist(Attendance attendance) throws Exception;
}
