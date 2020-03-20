package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.dao_Pfans.PFANS2000.Vo.BaseVo;
import com.nt.dao_Pfans.PFANS2000.Wages;
import com.nt.service_pfans.PFANS2000.WagesService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Getwages")
public class Pfans2006Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private WagesService wagesService;

    @RequestMapping(value = "/getTaxestotalList", method = {RequestMethod.GET})
    public ApiResult getList(String userid,HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        wagesService.select(tokenModel);
        Wages wages=new Wages();
        wages.setUser_id(userid);
        return ApiResult.success(wagesService.wagesList(wages));
    }

    @RequestMapping(value = "/getwagesList", method = {RequestMethod.POST})
    public ApiResult getwagesList(@RequestBody BaseVo basevo, HttpServletRequest request) throws Exception {
        if (basevo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(wagesService.selectBase());
    }

    @RequestMapping(value = "/getBonusList", method = {RequestMethod.GET})
    public ApiResult BonusList(String userid,HttpServletRequest request) throws Exception {
        Bonussend bonussend =new Bonussend();
        bonussend.setUser_id(userid);
        return ApiResult.success(wagesService.bonusList(bonussend));
    }


}
