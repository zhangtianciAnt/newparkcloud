package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.dao_Pfans.PFANS6000.CustomerinforPrimary;
import com.nt.service_pfans.PFANS6000.CustomerinforService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customerinfor")
public class Pfans6002Controller {

    @Autowired
    private CustomerinforService customerinforService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getcustomerinforprimary", method = {RequestMethod.GET})
    public ApiResult getcustomerinforprimary(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        CustomerinforPrimary customerinforprimary = new CustomerinforPrimary();
        return ApiResult.success(customerinforService.getcustomerinforPrimary(customerinforprimary, tokenModel));
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
        return ApiResult.success(customerinforService.getcustomerinforApplyOne(customerinfor));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updatecustomerinforApply(@RequestBody List<Customerinfor> customerinforList, HttpServletRequest request) throws Exception {
        if (customerinforList.size() == 0) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        customerinforService.updatecustomerinforApply(customerinforList, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult createcustomerinforApply(@RequestBody List<Customerinfor> customerinforList, HttpServletRequest request) throws Exception {
        if (customerinforList.size() == 0) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        customerinforService.createcustomerinforApply(customerinforList, tokenModel);
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

    /**
     * 导出人员信息Excel
     *  scc add 21/12/2
     */
    @RequestMapping(value = "/downloadExcel", method = { RequestMethod.POST},produces = "text/html;charset=UTF-8")
    public void downloadExcel(@RequestBody List<String> ids,HttpServletRequest request, HttpServletResponse resp) {
        try {
            customerinforService.downloadExcel(ids,request,resp);
        } catch (LogicalException e) {
            e.printStackTrace();
        }
    }


}
