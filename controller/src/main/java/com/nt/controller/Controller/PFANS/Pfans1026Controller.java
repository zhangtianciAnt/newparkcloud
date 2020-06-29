package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractapplicationVo;
import com.nt.service_pfans.PFANS1000.ContractapplicationService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/contractapplication")
public class Pfans1026Controller {

    @Autowired
    private ContractapplicationService contractapplicationService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.POST})
    public ApiResult selectById(@RequestBody Contractapplication contractapplication,HttpServletRequest request) throws Exception{
        if(contractapplication==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplication.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contractapplicationService.get(contractapplication));
    }

    @RequestMapping(value="/get2",method = {RequestMethod.POST})
    public ApiResult selectById2(@RequestBody Contractapplication contractapplication,HttpServletRequest request) throws Exception{
        if(contractapplication==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
//        TokenModel tokenModel = tokenService.getToken(request);
//        contractapplication.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contractapplicationService.get(contractapplication));
    }

    @RequestMapping(value="/getList",method = {RequestMethod.POST})
    public ApiResult getList(@RequestBody List<Contractapplication> contractapplicationlist  ,HttpServletRequest request) throws Exception{
        if(contractapplicationlist==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(contractapplicationService.getList(contractapplicationlist));
    }



    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody ContractapplicationVo contractapplication, HttpServletRequest request) throws Exception{
        if (contractapplication == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplicationService.update(contractapplication,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody ContractapplicationVo contractapplication, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplicationService.insert(contractapplication,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertBook", method = {RequestMethod.GET})
    public ApiResult insertBook(String contractnumber, String rowindex, String countNumber, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplicationService.insertBook(contractnumber, rowindex, countNumber, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/existCheck",method={RequestMethod.GET})
    public ApiResult existCheck(String contractNumber, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.existCheck(contractNumber));
    }

    @RequestMapping(value = "/getPe", method = {RequestMethod.GET})
    public ApiResult getPe(String claimnumber, HttpServletRequest request) throws Exception {
        if (claimnumber == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(contractapplicationService.getPe(claimnumber));
    }

}
