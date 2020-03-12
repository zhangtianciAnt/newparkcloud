package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.dao_Pfans.PFANS2000.Wages;
import com.nt.service_pfans.PFANS2000.LunarbonusService;
import com.nt.service_pfans.PFANS2000.WagesService;
import com.nt.utils.ApiResult;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/getList", method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        wagesService.select(tokenModel);
        Lunarbonus lunarbonus = new Lunarbonus();
        return ApiResult.success(lunarbonusService.getList(lunarbonus));
    }



}