package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Contracttheme;
import com.nt.service_pfans.PFANS1000.ContractthemeService;
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
import java.util.List;

@RestController
@RequestMapping("/contracttheme")
public class Pfans1040Controller {

    //销售テーマ
    @Autowired
    private ContractthemeService contractthemeService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Contracttheme contracttheme = new Contracttheme();
        contracttheme.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contractthemeService.get(contracttheme));

    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Contracttheme contracttheme, HttpServletRequest request) throws Exception {
        if (contracttheme == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractthemeService.one(contracttheme));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody List<Contracttheme> contracttheme, HttpServletRequest request) throws Exception{
        if (contracttheme == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        contractthemeService.update(contracttheme,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody List<Contracttheme> contracttheme, HttpServletRequest request) throws Exception {
        if (contracttheme == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        contractthemeService.insert(contracttheme,tokenModel);
        return ApiResult.success();
    }

}
