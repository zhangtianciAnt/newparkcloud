package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.Vo.AttendanceVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AttendanceService {

    List<Attendance> getlist(Attendance attendance) throws Exception;

    //日志使用
    List<Attendance> getAttendancelist(Attendance attendance) throws Exception;

    //考勤使用
    List<Attendance> getAttendancelist1(Attendance attendance) throws Exception;

    void update(AttendanceVo attendancevo, TokenModel tokenModel) throws Exception;

    //add_fjl_05/13   --添加审批正常结束后，自动变成承认状态
    void updStatus(Attendance attendance, TokenModel tokenModel) throws Exception;
    //add_fjl_05/13   --添加审批正常结束后，自动变成承认状态

    // add 0622 ccm --审批被驳回后，当月考勤数据全部变为未承认状态
    void updStatus1(Attendance attendance, TokenModel tokenModel) throws Exception;
    // add 0622 ccm --审批被驳回后，当月考勤数据全部变为未承认状态
}
