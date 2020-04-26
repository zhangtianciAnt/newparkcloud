package com.nt.controller.Controller.AOCHUAN;


import com.nt.dao_AOCHUAN.AOCHUAN7000.Account;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN7000.AccountService;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/docurule")
public class AOCHUAN7002Controller {
    @Autowired
    private DocuruleService docuruleService;

    @Autowired
    private AccountService accountService;


    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getList",method={RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Docurule docurule=new Docurule();
        return ApiResult.success(docuruleService.get(docurule));
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult One(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(docuruleService.One(id));
    }

    @RequestMapping(value = "/selectrule",method={RequestMethod.GET})
    public ApiResult selectrule(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(docuruleService.selectrule(id));
    }



    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody DocuruleVo docuruleVo, HttpServletRequest request) throws Exception {
        if(docuruleVo == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        docuruleService.update(docuruleVo,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult create(@RequestBody DocuruleVo docuruleVo, HttpServletRequest request) throws Exception {
        if(docuruleVo == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        docuruleService.insert(docuruleVo,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delCrerule",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String helprule_id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(helprule_id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        docuruleService.delCrerule(helprule_id);
        return ApiResult.success();
    }

    @RequestMapping(value="/getaccount" ,method = {RequestMethod.POST})
    public ApiResult getaccount( HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Account account = new Account();
        return ApiResult.success(accountService.get(account));

    }

}
