package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessplanVo;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/businessplan")
public class Pfans1036Controller {
    @Autowired
    private BusinessplanService businessplanService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/importUser")
    public ApiResult importUser(HttpServletRequest request, String flag) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(businessplanService.importUser(request, tokenModel));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Businessplan businessplan = new Businessplan();
        businessplan.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(businessplanService.get(businessplan));
    }

    @RequestMapping(value = "/getgroupA1", method = {RequestMethod.GET})
    public ApiResult getgroupA1(String year,String groupid,HttpServletRequest request) throws Exception {
        return ApiResult.success(businessplanService.getgroupA1(year,groupid));
    }

    @RequestMapping(value = "/getgroupcompanyen", method = {RequestMethod.GET})
    public ApiResult getgroupcompanyen(String year,HttpServletRequest request) throws Exception {
        return ApiResult.success(businessplanService.getgroupcompanyen(year));
    }

    @RequestMapping(value = "/getgroup", method = {RequestMethod.GET})
    public ApiResult getgroup(String year,String type,HttpServletRequest request) throws Exception {
        return ApiResult.success(businessplanService.getgroup(year,type));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String businessplanid, HttpServletRequest request) throws Exception {
        if (businessplanid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessplanService.selectById(businessplanid));
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult insertBusinessplanVo(@RequestBody Businessplan businessplan, HttpServletRequest request) throws Exception {
        if (businessplan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessplanService.insertBusinessplan(businessplan, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateBusinessplanVo(@RequestBody Businessplan businessplan, HttpServletRequest request) throws Exception {
        if (businessplan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessplanService.updateBusinessplanVo(businessplan, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getpersonplan", method = {RequestMethod.GET})
    public ApiResult getPersonPlan(@RequestParam String year,@RequestParam String groupid, HttpServletRequest request) throws Exception {
        if (groupid == "") {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessplanService.getPersonPlan(year, groupid));
    }

}
