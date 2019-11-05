package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.service_pfans.PFANS1000.JudgementService;
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
@RequestMapping("/judgement")
public class Pfans1004Controller {
    //查找信息发布
    @Autowired
    private JudgementService judgementService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{

            TokenModel tokenModel = tokenService.getToken(request);
            Judgement judgement = new Judgement();
            judgement.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(judgementService.getJudgement(judgement));
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Judgement judgement, HttpServletRequest request) throws Exception {
        if (judgement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(judgementService.One(judgement.getJudgementid()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateJudgement(@RequestBody Judgement judgement, HttpServletRequest request) throws Exception{
        if (judgement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        judgementService.updateJudgement(judgement,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Judgement judgement, HttpServletRequest request) throws Exception {
        if (judgement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        judgementService.insert(judgement,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getJudgementList", method = {RequestMethod.POST})
    public ApiResult getJudgementList(@RequestBody Judgement judgement, HttpServletRequest request) throws Exception {
        if (judgement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(judgementService.getJudgementList(judgement, request));
    }
}
