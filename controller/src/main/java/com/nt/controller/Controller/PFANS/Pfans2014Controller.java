package com.nt.controller.Controller.PFANS;

import com.nt.service_pfans.PFANS2000.FlexibleWorkService;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nt.dao_Pfans.PFANS2000.FlexibleWork;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/flexiblework")
public class Pfans2014Controller {
    //查找信息发布
    private FlexibleWorkService flexibleWorkService;


    private  TokenService tokenService;


    public ApiResult getFlexibleWork(HttpServletRequest request)throws  Exception{

        try{
            TokenModel tokenModel = tokenService.getToken(request);
            FlexibleWork flexibleWork = new FlexibleWork();
            flexibleWork.setStatus(AuthConstants.DEL_FLAG_NORMAL);
            flexibleWork.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(flexibleWorkService.getFlexibleWork(flexibleWork));
        }catch (LogicalException e){
            return  ApiResult.fail(e.getMessage());
        }
    }


}