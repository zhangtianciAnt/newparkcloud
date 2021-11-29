package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.ExpatriatesinforDetail;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS6000.ExpatriatesinforService;
import com.nt.service_pfans.PFANS6000.SupplierinforService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expatriatesinfor")
public class Pfans6004Controller {

    @Autowired
    private ExpatriatesinforService expatriatesinforService;

    @Autowired
    private SupplierinforService supplierinforService;

    @Autowired
    private TokenService tokenService;

    //增加分页 ztc fr
    @RequestMapping(value = "/getexpatrFliter", method = {RequestMethod.GET})
    public ApiResult getexpatrFliter(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        //expatriatesinfor.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(expatriatesinforService.getexpatrFliter(expatriatesinfor));
    }
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getexpatriatesinfor(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
        //expatriatesinfor.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(expatriatesinforService.getexpatriatesinfor(expatriatesinfor));
    }
    //增加分页 ztc to

    @RequestMapping(value = "/getWithoutAuth", method = {RequestMethod.GET})
    public ApiResult getWithoutAuth(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
//        expatriatesinfor.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(expatriatesinforService.getexpatriatesinfor(expatriatesinfor));
    }

    //    dialog优化分页 ztc fr
    @RequestMapping(value = "/getforSysDiaLog", method = {RequestMethod.GET})
    public ApiResult getforSysDiaLog(@RequestParam(defaultValue = "1") int currentPage,
                                    @RequestParam(defaultValue = "20") int pageSize,
                                    HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(expatriatesinforService.getforSysDiaLog(currentPage,pageSize));
    }
    //    dialog优化分页 ztc to

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult getexpatriatesinforApplyOne(@RequestBody Expatriatesinfor expatriatesinfor, HttpServletRequest request) throws Exception {
        if (expatriatesinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        expatriatesinfor.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(expatriatesinforService.getexpatriatesinforApplyOne(expatriatesinfor.getExpatriatesinfor_id()));
    }

    @RequestMapping(value = "/getGroupexpDetail", method = {RequestMethod.POST})
    public ApiResult getGroupexpDetail(@RequestBody Expatriatesinfor expatriatesinfor, HttpServletRequest request) throws Exception {
        if (expatriatesinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);

        return ApiResult.success(expatriatesinforService.getGroupexpDetail(expatriatesinfor.getExpatriatesinfor_id()));
    }

    @RequestMapping(value = "/updateinfor", method = {RequestMethod.POST})
    public ApiResult updateinforApply(@RequestBody Expatriatesinfor expatriatesinfor, HttpServletRequest request) throws Exception {
        if (expatriatesinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        expatriatesinforService.updateinforApply(expatriatesinfor, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateexpatriatesinforApply(@RequestBody Expatriatesinfor expatriatesinfor, HttpServletRequest request) throws Exception {
        if (expatriatesinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        expatriatesinforService.updateexpatriatesinforApply(expatriatesinfor, tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult createexpatriatesinforApply(@RequestBody Expatriatesinfor expatriatesinfor, HttpServletRequest request) throws Exception {
        if (expatriatesinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        expatriatesinforService.createexpatriatesinforApply(expatriatesinfor, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/expimport", method = {RequestMethod.POST})
    public ApiResult expimport(HttpServletRequest request) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(expatriatesinforService.expimport(request, tokenModel));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
    @RequestMapping(value = "/download", method = {RequestMethod.POST})
    public void download(HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        ExcelOutPutUtil.OutPut("外驻人员登记表","waizhu.xlsx",data,response);
    }

    @RequestMapping(value = "/crAccount", method = {RequestMethod.POST})
    public ApiResult crAccount(@RequestBody List<Expatriatesinfor> expatriatesinfor,HttpServletRequest request) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            expatriatesinforService.crAccount(expatriatesinfor, tokenModel);
            return ApiResult.success();
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/crAccount2", method = {RequestMethod.POST})
    public ApiResult crAccount2(@RequestBody List<Expatriatesinfor> expatriatesinfor,HttpServletRequest request) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            expatriatesinforService.crAccount2(expatriatesinfor, tokenModel);
            return ApiResult.success();
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
}
