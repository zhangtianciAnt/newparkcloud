package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.service_pfans.PFANS2000.AttendanceService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
        attendance.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(attendanceService.getlist(attendance));
    }

    @RequestMapping(value = "/getAttendancelist", method = {RequestMethod.POST})
    public ApiResult getAttendancelist(@RequestBody Attendance attendance, HttpServletRequest request) throws Exception {
        if (attendance == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendance.setStatus(AuthConstants.DEL_FLAG_NORMAL);
        attendance.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(attendanceService.getAttendancelist(attendance));
    }
}
