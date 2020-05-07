package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Enquiry;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.TransportGoodService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transportgood")
public class AOCHUAN3002Controller {

    @Autowired
    private TransportGoodService transportGoodService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get",method={RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        return ApiResult.success(transportGoodService.get());
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult getOne(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(transportGoodService.getOne(id));
    }

    @RequestMapping(value = "/getForSupplier",method={RequestMethod.GET})
    public ApiResult getForSupplier(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(transportGoodService.getForSupplier(id));
    }

    @RequestMapping(value = "/getForCustomer",method={RequestMethod.GET})
    public ApiResult getForCustomer(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(transportGoodService.getForCustomer(id));
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody TransportGood transportGood, HttpServletRequest request) throws Exception {
        if(transportGood == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        transportGoodService.update(transportGood,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody TransportGood transportGood, HttpServletRequest request) throws Exception {
        if(transportGood == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        transportGoodService.insert(transportGood,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        transportGoodService.delete(id);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertcw",method={RequestMethod.POST})
    public ApiResult insertcw(@RequestBody FinPurchase FinPurchase, HttpServletRequest request) throws Exception {
        if (FinPurchase == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        transportGoodService.insertCW(FinPurchase, tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/inserthk",method={RequestMethod.POST})
    public ApiResult inserthk(@RequestBody FinSales FinSales, HttpServletRequest request) throws Exception {
        if (FinSales == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        transportGoodService.insertHK(FinSales, tokenService.getToken(request));
        return ApiResult.success();
    }
}