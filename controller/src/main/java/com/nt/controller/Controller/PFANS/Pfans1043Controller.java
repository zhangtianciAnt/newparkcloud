package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.ThemeInfor;
import com.nt.service_pfans.PFANS1000.ThemeInforService;
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
@RequestMapping("/themeinfor")
public class Pfans1043Controller {

    @Autowired
    private ThemeInforService themeinforservice;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        ThemeInfor themeinfor = new ThemeInfor();
        //themeinfor.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(themeinforservice.list(themeinfor));
    }

    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody ThemeInfor themeinfor, HttpServletRequest request) throws Exception {
        if (themeinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        themeinforservice.insert(themeinfor, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody ThemeInfor themeinfor, HttpServletRequest request) throws Exception {
        if (themeinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        themeinforservice.upd(themeinfor, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/importUser",method={RequestMethod.POST})
    public ApiResult importUser(HttpServletRequest request,String flag){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(themeinforservice.importUser(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/oneInfo", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody ThemeInfor themeinfor, HttpServletRequest request) throws Exception {
        if (themeinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(themeinforservice.One(themeinfor.getThemeinfor_id()));
    }

}
