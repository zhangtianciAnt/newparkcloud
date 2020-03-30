package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.service_pfans.PFANS6000.CustomerinforService;
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
@RequestMapping("/customerinfor")
public class Pfans6002Controller {

    @Autowired
    private CustomerinforService customerinforService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getcustomerinfor(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Customerinfor customerinfor = new Customerinfor();
        customerinfor.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(customerinforService.getcustomerinfor(customerinfor, tokenModel));
    }

    @RequestMapping(value = "/get2", method = {RequestMethod.GET})
    public ApiResult getcustomerinfor2(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Customerinfor customerinfor = new Customerinfor();
        return ApiResult.success(customerinforService.getcustomerinfor(customerinfor, tokenModel));
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult getcustomerinforApplyOne(@RequestBody Customerinfor customerinfor, HttpServletRequest request) throws Exception {
        if (customerinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        customerinfor.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(customerinforService.getcustomerinforApplyOne(customerinfor.getCustomerinfor_id()));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updatecustomerinforApply(@RequestBody Customerinfor customerinfor, HttpServletRequest request) throws Exception {
        if (customerinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        customerinforService.updatecustomerinforApply(customerinfor, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult createcustomerinforApply(@RequestBody Customerinfor customerinfor, HttpServletRequest request) throws Exception {
        if (customerinfor == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        customerinforService.createcustomerinforApply(customerinfor, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/eximport", method = {RequestMethod.POST})
    public ApiResult eximport(HttpServletRequest request) {
        try {
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(customerinforService.eximport(request, tokenModel));
        } catch (LogicalException e) {
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
    @RequestMapping(value = "/download", method = {RequestMethod.POST})
    public void download(HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        ExcelOutPutUtil.OutPut("客户信息","kehuxinxi.xlsx",data,response);
    }




}
