package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.service_pfans.PFANS1000.JudgementService;

import com.nt.service_pfans.PFANS1000.PublicExpenseService;
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
@RequestMapping("/publicexpense")
public class Pfans1012Controller {

    @Autowired
    private PublicExpenseService publicExpenseService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JudgementService judgementService;

    /*
    * 列表查看
    * */
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{
        TokenModel tokenModel=tokenService.getToken(request);
        PublicExpense publicExpense=new PublicExpense();
        publicExpense.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(publicExpenseService.get(publicExpense));
    }

    /**
     * 查看一个人
     */
    @RequestMapping(value = "/selectById",method = {RequestMethod.POST})
    public ApiResult selectById(@RequestBody PublicExpense publicExpense, HttpServletRequest request) throws Exception {
        if (publicExpense == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenMode1 = tokenService.getToken(request);
        return ApiResult.success(publicExpenseService.selectById(publicExpense.getPublicexpenseid()));
    }

    /**
     * 新建
     */
    @RequestMapping(value = "/insert",method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody PublicExpense publicExpense, HttpServletRequest request) throws Exception {
        if (publicExpense == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        publicExpenseService.insert(publicExpense,tokenModel);
        return ApiResult.success();
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody  PublicExpense publicExpense, HttpServletRequest request) throws Exception {
        if (publicExpense == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        publicExpenseService.update(publicExpense,tokenModel);
        return ApiResult.success();
    }


/*
* 裁决号
* */

@RequestMapping(value="/getJudgement" ,method = {RequestMethod.POST})
    public ApiResult getJudgement(@RequestBody Judgement judgement,HttpServletRequest request) throws Exception{
    if(judgement==null){
        return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
    }
    TokenModel tokenModel=tokenService.getToken(request);
    judgement.setOwners(tokenModel.getOwnerList());
    return ApiResult.success(judgementService.getJudgement(judgement));
}

}
