package com.nt.controller.Controller;

import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nt.dao_Pfans.PFANS2000.Flexiblework;
import com.nt.service_pfans.PFANS2000.FlexibleworkService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/flexiblework")
public class FlexibleworkController {

    //查找信息发布
    @Autowired
    private FlexibleworkService FlexibleworkService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getInformation(HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(FlexibleworkService.getFlexiblework());
    }

}

