package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS1000.Vo.ReportBusinessVo;
import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.Vo.AttendanceReport;
import com.nt.dao_Pfans.PFANS2000.Vo.AttendanceVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public interface AttendanceService {

    List<Attendance> getlist(Attendance attendance) throws Exception;

    //日志使用
    List<Attendance> getAttendancelist(Attendance attendance) throws Exception;

    //考勤使用
    List<Attendance> getAttendancelist1(Attendance attendance) throws Exception;

    //获取离职考勤对比
    List<Attendance> getAttendancelistCompared(Attendance attendance) throws Exception;
    void disclickUpdateStates(Attendance attendance, TokenModel tokenModel) throws Exception;

    void update(AttendanceVo attendancevo, TokenModel tokenModel) throws Exception;

    //add_fjl_05/13   --添加审批正常结束后，自动变成承认状态
    void updStatus(Attendance attendance, TokenModel tokenModel) throws Exception;
    //add_fjl_05/13   --添加审批正常结束后，自动变成承认状态

    // add 0622 ccm --审批被驳回后，当月考勤数据全部变为未承认状态
    void updStatus1(Attendance attendance, TokenModel tokenModel) throws Exception;
    // add 0622 ccm --审批被驳回后，当月考勤数据全部变为未承认状态

    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认
    List<Date> selectAbnomalandOvertime(AttendanceVo attendancevo) throws Exception;
    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认

    //add ccm 0812 考情管理查看当天的异常申请数据
    List<AbNormal> getabnormalByuseridandDate(Attendance attendance) throws Exception;
    //add ccm 0812 考情管理查看当天的异常申请数据
    //考勤导出 1125 ztc fr
    void exportReported(String status, String year, String month, HttpServletRequest request, HttpServletResponse resp) throws Exception;
    //考勤导出 1125 ztc to
}
