package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
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

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult selectById(Contractapplication contractapplication,HttpServletRequest request) throws Exception{
        if(contractapplication==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(contractapplicationService.get(contractapplication));
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

    @RequestMapping(value = "/insertBook",method={RequestMethod.POST})
    public ApiResult insertBook(@RequestBody Contractapplication contractapplication, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        contractapplicationService.insertBook(contractapplication,tokenModel);
        return ApiResult.success();
    }

}
