package com.nt.controller.Controller.AOCHUAN;


import com.nt.dao_AOCHUAN.AOCHUAN6000.Secrecy;
import com.nt.service_AOCHUAN.AOCHUAN6000.SecrecyService;
import com.nt.service_AOCHUAN.AOCHUAN8000.Impl.ContractNumber;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/secrecy")
public class AOCHUAN6007Controller {

    @Autowired
    private SecrecyService secrecyService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ContractNumber contractNumber;

    @RequestMapping(value="/getList",method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Secrecy secrecy=new Secrecy();
        return ApiResult.success(secrecyService.get(secrecy));

    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Secrecy secrecy, HttpServletRequest request) throws Exception {
        if (secrecy == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        secrecyService.insert(secrecy,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult One(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(secrecyService.One(id));
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody Secrecy secrecy, HttpServletRequest request) throws Exception {
        if(secrecy == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        secrecyService.update(secrecy,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        secrecyService.delete(id);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getNumber",method={RequestMethod.GET})
    public ApiResult getNumber( HttpServletRequest request) throws Exception {
        String number = contractNumber.getContractNumber("PT001008","secrecy");
        Secrecy secrecy=new Secrecy();
        secrecy.setNo(number);
        return ApiResult.success( secrecy);
    }

}
