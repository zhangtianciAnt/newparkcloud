package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AttendanceService {

    List<Attendance> getlist(Attendance attendance) throws Exception;

    List<Attendance> getAttendancelist(Attendance attendance) throws Exception;

    void update(Attendance attendance, TokenModel tokenModel) throws Exception;

    //add_fjl_05/13   --添加审批正常结束后，自动变成承认状态
    void updStatus(Attendance attendance, TokenModel tokenModel) throws Exception;
    //add_fjl_05/13   --添加审批正常结束后，自动变成承认状态
}
