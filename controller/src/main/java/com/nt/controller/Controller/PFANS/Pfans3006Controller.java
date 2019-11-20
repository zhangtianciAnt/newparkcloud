package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS3000.AppointmentCar;
import com.nt.service_pfans.PFANS3000.AppointmentCarService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/appointmentcar")
public class Pfans3006Controller {

    @Autowired
    private AppointmentCarService appointmentcarService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getAppointmentCar(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        AppointmentCar appointmentcar = new AppointmentCar();
        appointmentcar.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(appointmentcarService.getAppointmentCar(appointmentcar));
    }

    @RequestMapping(value = "/getAppointmentCarlist", method = {RequestMethod.POST})
    public ApiResult getAppointmentCarlist(@RequestBody AppointmentCar appointmentcar, HttpServletRequest request) throws Exception {
        if (appointmentcar == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        appointmentcar.setStatus(AuthConstants.APPROVED_FLAG_YES);
        appointmentcar.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(appointmentcarService.getAppointmentCarlist(appointmentcar));
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody AppointmentCar appointmentcar, HttpServletRequest request) throws Exception {
        if (appointmentcar == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(appointmentcarService.One(appointmentcar.getAppointmentcarid()));
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody AppointmentCar appointmentcar, HttpServletRequest request) throws Exception {
        if (appointmentcar == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        appointmentcarService.insertAppointmentCar(appointmentcar, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateAppointmentCar(@RequestBody AppointmentCar appointmentcar, HttpServletRequest request) throws Exception {
        if (appointmentcar == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        appointmentcarService.updateAppointmentCar(appointmentcar, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/download", method = {RequestMethod.POST})
    public void download(HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        ExcelOutPutUtil.OutPut("jiejipai","jiejipai.xlsx",data,response);
    }
}
