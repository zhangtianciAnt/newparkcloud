package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.dao_Workflow.Vo.WorkflowVo;
import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.service_pfans.PFANS2000.CasgiftApplyService;
import com.nt.utils.*;
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
            casgiftapply.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(casgiftapplyService.getCasgiftApply(casgiftapply));

        } catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception {
        if (casgiftapply == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(casgiftapplyService.One(casgiftapply.getCasgiftapplyid()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateCasgiftApply(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception{
        if (casgiftapply == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.updateCasgiftApply(casgiftapply,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody CasgiftApply casgiftapply, HttpServletRequest request) throws Exception {
        if (casgiftapply == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        casgiftapplyService.insert(casgiftapply,tokenModel);
        return ApiResult.success();
    }
}
