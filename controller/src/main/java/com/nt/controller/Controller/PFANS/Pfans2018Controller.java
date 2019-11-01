package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.AttendanceSetting;
import com.nt.service_pfans.PFANS2000.AttendanceSettingService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/attendancesetting")
public class Pfans2018Controller {

    @Autowired
    private AttendanceSettingService attendanceSettingService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        AttendanceSetting attendanceSetting = new AttendanceSetting();
        attendanceSetting.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(attendanceSettingService.list(attendanceSetting));
    }

    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody AttendanceSetting attendanceSetting, HttpServletRequest request) throws Exception {
        if (attendanceSetting == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendanceSettingService.insert(attendanceSetting, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody AttendanceSetting attendanceSetting, HttpServletRequest request) throws Exception {
        if (attendanceSetting == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        attendanceSettingService.update(attendanceSetting, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/oneInfo", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody AttendanceSetting attendanceSetting, HttpServletRequest request) throws Exception {
        if (attendanceSetting == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(attendanceSettingService.One(attendanceSetting.getAttendancesetting_id()));
    }

}
