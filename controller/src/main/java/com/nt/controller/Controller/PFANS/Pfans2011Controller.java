package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.service_pfans.PFANS2000.OvertimeService;
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
@RequestMapping("/overtime")
public class Pfans2011Controller {
    //查找
    @Autowired
    private OvertimeService overtimeService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getOvertime(HttpServletRequest request) throws Exception{
            TokenModel tokenModel = tokenService.getToken(request);
            Overtime overtime = new Overtime();
            overtime.setStatus(AuthConstants.DEL_FLAG_NORMAL);
            overtime.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(overtimeService.getOvertime(overtime));
        }






    @RequestMapping(value ="/one",method = { RequestMethod.POST} )
    public ApiResult one(@RequestBody Overtime overtime,HttpServletRequest request) throws Exception{
        if (overtime == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(overtimeService.One(overtime.getOvertimeid()));
    }

    @RequestMapping(value="/create",method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Overtime overtime, HttpServletRequest request) throws Exception{
        if (overtime == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        overtimeService.insertOvertime(overtime,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateOvertime(@RequestBody Overtime overtime, HttpServletRequest request) throws Exception{
        if (overtime == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        overtimeService.updateOvertime(overtime,tokenModel);
        return ApiResult.success();
    }

}
