package com.nt.controller.Controller;

import Vo.PersonalCenter;
import com.nt.service_Org.PersonalCenterService;
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

public class PersonalCenterController {
    @Autowired
    private PersonalCenterService personalCenterService;

    @Autowired
    private TokenService tokenService;


       //更新基本信息
       @RequestMapping(value = "/save", method = {RequestMethod.POST})
       public ApiResult save(@RequestBody PersonalCenter personalCenter, HttpServletRequest request) throws Exception {
           if (personalCenter == null || StringUtils.isEmpty(personalCenter)) {
               return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
           }
           TokenModel tokenModel = tokenService.getToken(request);
           if(personalCenter.getCreateby() == null || personalCenter.getCreateon() == null){
               personalCenter.preInsert(tokenModel);
           }
           personalCenter.preUpdate(tokenModel);
           personalCenterService.save(personalCenter);
           return ApiResult.success();
       }

}
