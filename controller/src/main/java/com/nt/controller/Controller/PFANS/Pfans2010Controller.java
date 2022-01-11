package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Vo.ReportBusinessVo;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.Vo.AttendanceReport;
import com.nt.dao_Pfans.PFANS2000.Vo.AttendanceVo;
import com.nt.service_pfans.PFANS2000.AttendanceService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class Pfans2010Controller {

    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getlist", method = {RequestMethod.POST})
    public ApiResult getlist(@RequestBody Attendance attendance, HttpServletRequest request) throws Exception {
        if (attendance == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        //attendance.setOwner(tokenModel.getUserId());
        attendance.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(attendanceService.getlist(attendance));
    }

    //日志使用
    @RequestMapping(value = "/getAttendancelist", method = {RequestMethod.POST})
    public ApiResult getAttendancelist(@RequestBody Attendance attendance, HttpServletRequest request) throws Exception {
        if (attendance == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
//        attendance.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(attendanceService.getAttendancelist(attendance));
    }
    //考勤使用
    @RequestMapping(value = "/getAttendancelist1", method = {RequestMethod.POST})
    public ApiResult getAttendancelist1(@RequestBody Attendance attendance, HttpServletRequest request) throws Exception {
        if (attendance == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
//        attendance.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(attendanceService.getAttendancelist1(attendance));
    }

    //获取离职考勤对比
    @RequestMapping(value = "/getAttendancelistCompared", method = {RequestMethod.POST})
    public ApiResult getAttendancelistCompared(@RequestBody Attendance attendance, HttpServletRequest request) throws Exception {
        if (attendance == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        return ApiResult.success(attendanceService.getAttendancelistCompared(attendance));
    }
    @RequestMapping(value = "/disclickUpdateStates", method = {RequestMethod.POST})
    public ApiResult disclickUpdateStates(@RequestBody Attendance attendance, HttpServletRequest request) throws Exception {
        if (attendance == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendanceService.disclickUpdateStates(attendance, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody AttendanceVo attendancevo, HttpServletRequest request) throws Exception {
        if (attendancevo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendanceService.update(attendancevo, tokenModel);
        return ApiResult.success();
    }

    //add_fjl_05/13   --添加审批正常结束后，自动变成承认状态
    @RequestMapping(value = "/updStatus", method = {RequestMethod.POST})
    public ApiResult updStatus(@RequestBody Attendance attendance, HttpServletRequest request) throws Exception {
        if (attendance == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendanceService.updStatus(attendance, tokenModel);
        return ApiResult.success();
    }
    //add_fjl_05/13   --添加审批正常结束后，自动变成承认状态

    // add 0622 ccm --审批被驳回后，当月考勤数据全部变为未承认状态
    @RequestMapping(value = "/updStatus1", method = {RequestMethod.POST})
    public ApiResult updStatus1(@RequestBody Attendance attendance, HttpServletRequest request) throws Exception {
        if (attendance == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendanceService.updStatus1(attendance, tokenModel);
        return ApiResult.success();
    }
    // add 0622 ccm --审批被驳回后，当月考勤数据全部变为未承认状态

    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认
    @RequestMapping(value = "/selectAbnomalandOvertime", method = {RequestMethod.POST})
    public ApiResult selectAbnomalandOvertime(@RequestBody AttendanceVo attendancevo, HttpServletRequest request) throws Exception {
        if (attendancevo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(attendanceService.selectAbnomalandOvertime(attendancevo));
    }
    //add ccm 2020729 考勤异常加班审批中的日期，考勤不允许承认

    //add ccm 0812 考情管理查看当天的异常申请数据
    @RequestMapping(value = "/getabnormalByuseridandDate", method = {RequestMethod.POST})
    public ApiResult getabnormalByuseridandDate(@RequestBody Attendance attendance, HttpServletRequest request) throws Exception {
        if (attendance == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(attendanceService.getabnormalByuseridandDate(attendance));
    }
    //add ccm 0812 考情管理查看当天的异常申请数据

    //考勤导出 1125 ztc fr
    @RequestMapping(value = "/exportReported", method = {RequestMethod.POST})
    public void export(@RequestParam String status, String year,String month, HttpServletRequest request, HttpServletResponse response) throws Exception {
        attendanceService.exportReported(status,year,month,request,response);
    }
    //考勤导出 1125 ztc to
}
