package com.nt.controller.Controller;

import com.nt.dao_Auth.model.SignInInformation;
import com.nt.dao_Auth.model.UserInfo;
import com.nt.dao_Org.ServiceCategory;
import com.nt.service_Auth.SignInInformationSeivice;
import com.nt.service_Auth.mapper.SignInInformationMapper;
import com.nt.service_Org.ServiceCategoryService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/SignIn")
public class SignInController {

    @Autowired
    private SignInInformationSeivice signInInformationSeivice;

    @RequestMapping(value = "/SignIn", method = {RequestMethod.GET})
    public ApiResult saveservicecategory(String No, HttpServletRequest request) throws Exception {
        SignInInformation signInInformation = new SignInInformation();
        signInInformation.setNo(No) ;
        signInInformation.setDate(new Date());
        signInInformationSeivice.insert(signInInformation);
        return ApiResult.success();
    }
}
