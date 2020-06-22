package com.nt.controller.Controller.AOCHUANMENHU;
import com.nt.dao_AOCHUAN.AOCHUANMENHU.Menhuproducts;
import com.nt.service_AOCHUAN.AOCHUANMENHU.MenhuproductsService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/productslist")

public class AOCHUAN005Controller {



    @Autowired
    private MenhuproductsService productsService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/getList",method = {RequestMethod.GET})
    public ApiResult getList(String dtitle,String xtitle,HttpServletRequest request)throws  Exception{

        Menhuproducts menhuproducts = new Menhuproducts();
        menhuproducts.setDtitle(dtitle);
        menhuproducts.setXtitle(xtitle);
        return ApiResult.success(productsService.get(menhuproducts));

    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Menhuproducts menhuproducts, HttpServletRequest request) throws Exception {
        if (menhuproducts == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        productsService.insert(menhuproducts,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult one(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(productsService.One(id));
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        productsService.delete(id);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody Menhuproducts menhuproducts, HttpServletRequest request) throws Exception {
        if(menhuproducts == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        productsService.update(menhuproducts,tokenService.getToken(request));
        return ApiResult.success();
    }




}
