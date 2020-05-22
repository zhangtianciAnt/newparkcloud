package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.TalentPlan;
import com.nt.dao_Pfans.PFANS2000.Vo.TalentPlanVo;
import com.nt.service_pfans.PFANS2000.TalentPlanService;
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
@RequestMapping("/talentplan")
public class Pfans2024Controller {

    @Autowired
    private TalentPlanService talentplanService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody TalentPlan talentPlan, HttpServletRequest request) throws Exception {
        if (talentPlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        TalentPlan log=talentplanService.One(talentPlan.getTalentplan_id());
        return ApiResult.success(log);
    }

    @RequestMapping(value="/list", method={RequestMethod.POST})
    public ApiResult List(@RequestBody TalentPlan talentPlan, HttpServletRequest request) throws Exception {
        if (talentPlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        talentPlan.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(talentplanService.list(talentPlan));
    }


    @RequestMapping(value="/updateInfo",method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody TalentPlan talentPlan, HttpServletRequest request) throws Exception{
        if (talentPlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        talentplanService.upd(talentPlan,tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/createNewUser",method={RequestMethod.POST})
    public ApiResult create(@RequestBody TalentPlan talentPlan, HttpServletRequest request) throws Exception {
        if (talentPlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        talentplanService.insert(talentPlan,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult createByOrg(@RequestBody TalentPlanVo TalentPlanVo, HttpServletRequest request) throws Exception {
        if (TalentPlanVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        talentplanService.insertByOrg(TalentPlanVo,tokenModel);
        return ApiResult.success();
    }
}
