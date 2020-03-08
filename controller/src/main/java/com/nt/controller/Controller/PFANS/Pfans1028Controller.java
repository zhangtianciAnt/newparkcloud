package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.NonJudgment;
import com.nt.service_pfans.PFANS1000.NonJudgmentService;
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
@RequestMapping("/nonjudgment")
public class Pfans1028Controller {

    @Autowired
    private NonJudgmentService nonJudgmentService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request)throws Exception{
        TokenModel tokenModel=tokenService.getToken(request);
        NonJudgment nonJudgment =new NonJudgment();
        nonJudgment.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(nonJudgmentService.get(nonJudgment));
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody NonJudgment nonJudgment, HttpServletRequest request) throws Exception{
        if(nonJudgment==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(nonJudgmentService.one(nonJudgment.getNonjudgment_id()));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody NonJudgment nonJudgment,HttpServletRequest request) throws Exception{
        if(nonJudgment==null){
            return  ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        nonJudgmentService.update(nonJudgment,tokenModel);
        return ApiResult.success();
    }



}
