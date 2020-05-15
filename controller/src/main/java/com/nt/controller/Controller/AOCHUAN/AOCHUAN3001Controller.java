package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Enquiry;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quotations")
public class AOCHUAN3001Controller {

    @Autowired
    private QuotationsService quotationsService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get",method={RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Quotations quotations = new Quotations();
        quotations.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(quotationsService.get(quotations));
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult getOne(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(quotationsService.getOne(id));
    }

    @RequestMapping(value = "/getForSupplier",method={RequestMethod.GET})
    public ApiResult getForSupplier(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(quotationsService.getForSupplier(id));
    }

    @RequestMapping(value = "/getForCustomer",method={RequestMethod.GET})
    public ApiResult getForCustomer(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(quotationsService.getForCustomer(id));
    }
    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody Quotations quotations, HttpServletRequest request) throws Exception {
        if(quotations == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        quotationsService.update(quotations,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody Quotations quotations, HttpServletRequest request) throws Exception {
        if(quotations == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        quotationsService.insert(quotations,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        quotationsService.delete(id);
        return ApiResult.success();
    }

    @RequestMapping(value = "/pdf",method={RequestMethod.POST})
    public void pdf(@RequestBody List<Enquiry> enquiry, HttpServletRequest request, HttpServletResponse response) throws Exception {
            Map<String, Object> data = new HashMap<>();
//            List<Enquiry> en = new ArrayList<>();
//            Enquiry enquiry1 = new Enquiry();
//            enquiry1.setSupplier("123");
//            enquiry1.setQuotedprice("123");
//            enquiry1.setExchangerate("123");
//            en.add(enquiry1);
         data.put("enquiry",enquiry);
         data.put("productus","ao_chuan");
         data.put("producten","奥川");
         ExcelOutPutUtil.OutPut("aochuan.xlsx",data,response);

    }
}
