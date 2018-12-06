package com.nt.controller.Controller;

import com.nt.dao_Org.CustomerInfo;
import com.nt.service_Org.PersonalCenterVoService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/PersonalCenter")

public class PersonalCenterVoController {
    @Autowired
    private PersonalCenterVoService personalCenterService;

    @Autowired
    private TokenService tokenService;

    //获取基本信息
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(personalCenterService.get(tokenModel.getUserId()));
    }

       //更新基本信息
       @RequestMapping(value = "/save", method = {RequestMethod.POST})
       public ApiResult save(@RequestBody CustomerInfo customerInfo, HttpServletRequest request) throws Exception {
           if (customerInfo == null || StringUtils.isEmpty(customerInfo)) {
               return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
           }
           TokenModel tokenModel = tokenService.getToken(request);
           if(customerInfo.getCreateby() == null || customerInfo.getCreateon() == null){
               customerInfo.preInsert(tokenModel);
           }
           customerInfo.preUpdate(tokenModel);
           personalCenterService.save(customerInfo);
           return ApiResult.success();
       }

}
