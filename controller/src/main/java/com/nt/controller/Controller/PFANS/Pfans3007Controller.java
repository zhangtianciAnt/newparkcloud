package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS3000.JapanCondominium;
import com.nt.service_pfans.PFANS3000.JapanCondominiumService;
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
@RequestMapping("/japancondominium")
public class Pfans3007Controller {
    //查找
    @Autowired
    private JapanCondominiumService japancondominiumService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getJapanCondominium(HttpServletRequest request) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        JapanCondominium japancondominium = new JapanCondominium();
        japancondominium.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(japancondominiumService.getJapanCondominium(japancondominium));
    }

    @RequestMapping(value="/getJapanCondominiumlist",method = {RequestMethod.POST})
    public ApiResult getJapanCondominiumlist(@RequestBody JapanCondominium japancondominium, HttpServletRequest request) throws Exception{
        if (japancondominium == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        japancondominium.setStatus(AuthConstants.APPROVED_FLAG_YES);
        japancondominium.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(japancondominiumService.getJapanCondominiumlist(japancondominium));
    }

    @RequestMapping(value ="/one",method = { RequestMethod.POST} )
    public ApiResult one(@RequestBody AppointmentCar appointmentcar,HttpServletRequest request) throws Exception{
        if (appointmentcar == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(appointmentcarService.One(appointmentcar.getAppointmentcarid()));
    }

    @RequestMapping(value="/create",method = {RequestMethod.POST})
    public ApiResult create(@RequestBody AppointmentCar appointmentcar, HttpServletRequest request) throws Exception{
        if (appointmentcar == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        appointmentcarService.insertAppointmentCar(appointmentcar,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateAppointmentCar(@RequestBody AppointmentCar appointmentcar, HttpServletRequest request) throws Exception{
        if (appointmentcar == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        appointmentcarService.updateAppointmentCar(appointmentcar,tokenModel);
        return ApiResult.success();
    }

}
