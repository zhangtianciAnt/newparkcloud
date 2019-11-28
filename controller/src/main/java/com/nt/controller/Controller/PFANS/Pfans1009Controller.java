package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Fixedassets;
import com.nt.service_pfans.PFANS1000.FixedassetsService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/fixedassets")
public class Pfans1009Controller {

    @Autowired
    private FixedassetsService fixedassetsService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getFixedassets(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Fixedassets fixedassets = new Fixedassets();
        fixedassets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(fixedassetsService.getFixedassets(fixedassets));

    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Fixedassets fixedassets, HttpServletRequest request) throws Exception {
        if (fixedassets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(fixedassetsService.One(fixedassets.getFixedassets_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateFixedassets(@RequestBody Fixedassets fixedassets, HttpServletRequest request) throws Exception{
        if (fixedassets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        fixedassetsService.updateFixedassets(fixedassets,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Fixedassets fixedassets, HttpServletRequest request) throws Exception {
        if (fixedassets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        fixedassetsService.insert(fixedassets,tokenModel);
        return ApiResult.success();
    }

}

