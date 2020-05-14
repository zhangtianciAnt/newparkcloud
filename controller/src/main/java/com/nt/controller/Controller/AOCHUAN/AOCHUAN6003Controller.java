package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Vacation;
import com.nt.service_AOCHUAN.AOCHUAN6000.VacationService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/vacation")

public class AOCHUAN6003Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private VacationService vacationService;

    @RequestMapping(value="/getList",method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Vacation vacation = new Vacation();
//        fixedassets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(vacationService.get(vacation));

    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Vacation vacation, HttpServletRequest request) throws Exception {
        if (vacation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        vacationService.insert(vacation,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult one(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(vacationService.One(id));
    }
//获取年假结余
    @RequestMapping(value = "/getannualyear",method={RequestMethod.GET})
    public ApiResult getannualyear(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(vacationService.getannualyear(id));
    }

    //保存换休时长
    @RequestMapping(value = "/saveHXlong",method={RequestMethod.POST})
    public ApiResult saveHXlong(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        vacationService.saveHXlong("5e956171e52fa71970c1a097");
        return ApiResult.success();
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody Vacation vacation, HttpServletRequest request) throws Exception {
        if(vacation == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        vacationService.update(vacation,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        vacationService.delete(id);
        return ApiResult.success();
    }

    @RequestMapping(value="/getTJ",method = {RequestMethod.GET})
    public ApiResult getTJ(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);

        return ApiResult.success(vacationService.getVo());

    }

    @RequestMapping(value = "/getVacation",method={RequestMethod.GET})
    public ApiResult getVacation(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(vacationService.getVacation(id));
    }


}
