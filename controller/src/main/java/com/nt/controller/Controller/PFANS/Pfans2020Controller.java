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
public class Pfans2020Controller {
    //查找信息发布
    @Autowired
    private CasgiftApplyService casgiftapplyService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getCasgiftapply(HttpServletRequest request) throws Exception{

        try {
            TokenModel tokenModel = tokenService.getToken(request);
            CasgiftApply Casgiftapply = new CasgiftApply();
            Casgiftapply.setStatus(AuthConstants.DEL_FLAG_NORMAL);
            Casgiftapply.setTenantid(tokenModel.getTenantId());
            Casgiftapply.setOwners(tokenModel.getOwnerList());
            Casgiftapply.setIds(tokenModel.getIdList());
            return ApiResult.success(casgiftapplyService.getCasgiftapply(Casgiftapply));

        } catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }
    }

    @RequestMapping(value="/insert",method = {RequestMethod.POST})
    public ApiResult insertCasgiftapply(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.insertCasgiftapply(casgiftapply,tokenModel);
        return ApiResult.success();
    }
    @RequestMapping(value="/view",method = {RequestMethod.POST})
    public ApiResult viewCasgiftapply(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.insertCasgiftapply(casgiftapply,tokenModel);
        return ApiResult.success();
    }
    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateCasgiftapply(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception{

        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.insertCasgiftapply(casgiftapply,tokenModel);
        return ApiResult.success();
    }
}
