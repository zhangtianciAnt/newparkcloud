package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import com.nt.dao_Pfans.PFANS2000.Wages;
import com.nt.service_pfans.PFANS2000.LunarbonusService;
import com.nt.service_pfans.PFANS2000.LunardetailService;
import com.nt.service_pfans.PFANS2000.WagesService;
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
@RequestMapping("/Getlunarbonus")
public class Pfans2027Controller {
	
    @Autowired
    private TokenService tokenService;

    @Autowired
    private LunarbonusService lunarbonusService;

    @Autowired
    private LunardetailService lunardetailService;

    @RequestMapping(value = "/getList", method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        wagesService.select(tokenModel);
        Lunarbonus lunarbonus = new Lunarbonus();
        return ApiResult.success(lunarbonusService.getList(lunarbonus));
    }

    //新建
    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Lunarbonus lunarbonus, HttpServletRequest request) throws Exception {
        if (lunarbonus == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        lunarbonusService.insert(lunarbonus,tokenModel);
        return ApiResult.success();
    }

    //编辑
    @RequestMapping(value = "/getLunardetail", method = {RequestMethod.POST})
    public ApiResult selectById(@RequestBody Lunardetail lunardetail, HttpServletRequest request) throws Exception {
        if(lunardetail==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(lunardetailService.getLunardetail(lunardetail));
    }



}