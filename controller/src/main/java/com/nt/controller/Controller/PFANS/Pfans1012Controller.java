package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.dao_Pfans.PFANS1000.TotalCost;
import com.nt.dao_Pfans.PFANS1000.Vo.PublicExpenseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TotalCostVo;
import com.nt.service_pfans.PFANS1000.JudgementService;

import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.PublicExpenseService;
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
@RequestMapping("/publicexpense")
public class Pfans1012Controller {

    @Autowired
    private PublicExpenseService publicExpenseService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JudgementService judgementService;

    @Autowired
    private LoanApplicationService loanapplicationService;

    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{
        TokenModel tokenModel=tokenService.getToken(request);
        PublicExpense publicExpense=new PublicExpense();
        publicExpense.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(publicExpenseService.get(publicExpense));
    }
    @RequestMapping(value = "/selectById",method = {RequestMethod.GET})
    public ApiResult selectById(String publicexpenseid, HttpServletRequest request) throws Exception {
        if (publicexpenseid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenMode1 = tokenService.getToken(request);
        return ApiResult.success(publicExpenseService.selectById(publicexpenseid));
    }
    @RequestMapping(value = "/insert",method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody PublicExpenseVo publicExpenseVo, HttpServletRequest request) throws Exception {
        try{
            if (publicExpenseVo == null) {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
            TokenModel tokenModel = tokenService.getToken(request);
            publicExpenseService.insert(publicExpenseVo,tokenModel);
            return ApiResult.success();
        }catch (Exception e) {
            return ApiResult.fail(e.getMessage());
        }

    }

    @RequestMapping(value = "/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody PublicExpenseVo publicExpenseVo, HttpServletRequest request) throws Exception {
        try{
            if (publicExpenseVo == null) {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
            TokenModel tokenModel = tokenService.getToken(request);
            publicExpenseService.update(publicExpenseVo,tokenModel);
            return ApiResult.success();
        }catch (Exception e) {
            return ApiResult.fail(e.getMessage());
        }
    }
    @RequestMapping(value="/gettotalcost" ,method = {RequestMethod.POST})
    public ApiResult gettotalcost(@RequestBody TotalCostVo totalcostvo, HttpServletRequest request) throws Exception{
    if(totalcostvo==null){
        return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
    }
    return ApiResult.success(publicExpenseService.gettotalcost(totalcostvo));
    }



    @RequestMapping(value="/getJudgement" ,method = {RequestMethod.POST})
    public ApiResult getJudgement(@RequestBody Judgement judgement,HttpServletRequest request) throws Exception{
        if(judgement==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(judgementService.getJudgement(judgement));
    }



    @RequestMapping(value="/getLoanApplication" ,method = {RequestMethod.POST})
    public ApiResult getLoanApplication(@RequestBody LoanApplication loanapplication, HttpServletRequest request) throws Exception {
        if (loanapplication==null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
//        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(loanapplicationService.getLoanApplication(loanapplication));
    }


}
