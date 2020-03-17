package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS1000.Evection;
import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.Vo.EvectionVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TravelCostVo;
import com.nt.service_pfans.PFANS1000.BusinessService;
import com.nt.service_pfans.PFANS1000.EvectionService;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/evection")
public class Pfans1013Controller {

    @Autowired
    private EvectionService evectionService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private BusinessService businessService;

    @Autowired
    private LoanApplicationService loanapplicationService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Evection evection = new Evection();
        evection.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(evectionService.get(evection));
    }
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String evectionid, HttpServletRequest request) throws Exception {
        if (evectionid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(evectionService.selectById(evectionid));
    }
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody EvectionVo evectionVo, HttpServletRequest request) throws Exception {
        if (evectionVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        evectionService.insertEvectionVo(evectionVo, tokenModel);
        return ApiResult.success();
    }
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody EvectionVo evectionVo, HttpServletRequest request) throws Exception {
        if (evectionVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        evectionService.updateEvectionVo(evectionVo, tokenModel);
        return ApiResult.success();
    }
    @RequestMapping(value="/getBusiness" ,method = {RequestMethod.GET})
    public ApiResult getBusiness(HttpServletRequest request) throws Exception{
        return ApiResult.success(businessService.getBuse());
    }

    @RequestMapping(value="/gettravelcostvo" ,method = {RequestMethod.POST})
    public ApiResult gettravelcostvo(@RequestBody TravelCostVo travelcostvo, HttpServletRequest request) throws Exception{
        if(travelcostvo==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(evectionService.gettravelcost(travelcostvo));
    }

    @RequestMapping(value="/getLoanApplication" ,method = {RequestMethod.GET})
    public ApiResult getLoanApplication(HttpServletRequest request) throws Exception {
        return ApiResult.success(loanapplicationService.getLoapp());
    }
}
