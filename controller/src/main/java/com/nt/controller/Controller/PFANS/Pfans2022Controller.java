package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.service_pfans.PFANS2000.CasgiftApplyService;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
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
public class Pfans2022Controller {
    //查找信息发布
    @Autowired
    private CasgiftApplyService casgiftapplyService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getCasgiftApply(HttpServletRequest request) throws Exception{

        try {
            TokenModel tokenModel = tokenService.getToken(request);
            CasgiftApply casgiftapply = new CasgiftApply();
            casgiftapply.setStatus(AuthConstants.DEL_FLAG_NORMAL);
            casgiftapply.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(casgiftapplyService.getCasgiftApply(casgiftapply));

        } catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }
    }

    @RequestMapping(value="/insert",method = {RequestMethod.POST})
    public ApiResult insertCasgiftApply(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.insertCasgiftApply(casgiftapply,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateCasgiftApply(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.updateCasgiftApply(casgiftapply,tokenModel);
        return ApiResult.success();
    }
}
