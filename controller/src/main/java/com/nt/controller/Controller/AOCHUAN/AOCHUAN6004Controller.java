package com.nt.controller.Controller.AOCHUAN;

import com.mysql.fabric.xmlrpc.base.Data;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Attendance;
import com.nt.service_AOCHUAN.AOCHUAN6000.AttendancesService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/attendance")

public class AOCHUAN6004Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AttendancesService attendanceService;

    @RequestMapping(value="/getList",method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Attendance attendance = new Attendance();
        attendance.setOwners(tokenModel.getOwnerList());
//        attendance.setNames(id);
//        fixedassets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(attendanceService.get(attendance));

    }

    @RequestMapping(value="/getYICHANG",method = {RequestMethod.GET})
    public ApiResult getYICHANG(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Attendance attendance = new Attendance();
        attendance.setOwners(tokenModel.getOwnerList());
//        attendance.setNames(id);
//        fixedassets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(attendanceService.getYICHANG(attendance));

    }

//    @RequestMapping(value = "/getNowday",method={RequestMethod.GET})
//    public ApiResult one(@RequestParam String attendancetim, HttpServletRequest request) throws Exception {
//        if (attendancetim == null) {
//            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
//        }
//        TokenModel tokenModel = tokenService.getToken(request);
//        Attendance attendance = new Attendance();
//        attendance.setAttendancetim(attendancetim);
//
//        return ApiResult.success(attendanceService.getNow(attendance));
//    }

    @RequestMapping(value = "/getNowMon",method={RequestMethod.GET})
    public ApiResult getNowMon(@RequestParam String nowmons,@RequestParam String id, HttpServletRequest request) throws Exception {
        if (nowmons == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);

        return ApiResult.success(attendanceService.getNowMons(id,nowmons));
    }

    @RequestMapping(value = "/getNowMonYC",method={RequestMethod.GET})
    public ApiResult getNowMonYC(@RequestParam String nowmons,@RequestParam String id, HttpServletRequest request) throws Exception {
        if (nowmons == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);

        return ApiResult.success(attendanceService.getNowMonYC(id,nowmons));
    }


    @RequestMapping(value = "/importUser",method={RequestMethod.POST})
    public ApiResult importUser(HttpServletRequest request,String flag){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(attendanceService.importUser(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/getByUserId",method={RequestMethod.GET})
    public ApiResult getByUserId(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (!StringUtils.isNotBlank(id)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(attendanceService.getByUserId(id));
    }

}
