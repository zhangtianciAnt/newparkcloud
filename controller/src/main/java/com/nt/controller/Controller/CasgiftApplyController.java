package com.nt.controller.Controller;

import com.nt.dao_Pfans.PFANS2000.Casgiftapply;
import com.nt.service_pfans.PFANS2000.CasgiftapplyService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/casgiftapply")
public class CasgiftApplyController {
    //查找信息发布
    @Autowired
    private CasgiftapplyService casgiftapplyService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getCasgiftapply(HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
       return ApiResult.success(casgiftapplyService.getCasgiftapply());
    }

    @RequestMapping(value="/insert",method = {RequestMethod.POST})
    public ApiResult insertCasgiftapply(@RequestBody Casgiftapply casgiftapply, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.insertCasgiftapply(casgiftapply,tokenModel);
        return ApiResult.success();
    }
    @RequestMapping(value="/view",method = {RequestMethod.POST})
    public ApiResult viewCasgiftapply(@RequestBody Casgiftapply casgiftapply, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.insertCasgiftapply(casgiftapply,tokenModel);
        return ApiResult.success();
    }
    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateCasgiftapply(@RequestBody Casgiftapply casgiftapply, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.insertCasgiftapply(casgiftapply,tokenModel);
        return ApiResult.success();
    }
}
