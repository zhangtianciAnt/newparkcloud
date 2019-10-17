package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @name 年度休假一览
 */
@RestController
@RequestMapping("/annualLeave")
public class Pfans2013Controller {

    @Autowired
    private AnnualLeaveService annualLeaveService;

    @Autowired
    private TokenService tokenService;

    /**
     * @方法名：getDataList
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getDataList", method={RequestMethod.GET})
    public ApiResult getDataList(HttpServletRequest request) throws Exception{
        AnnualLeave annualLeave = new AnnualLeave();
        TokenModel tokenModel = tokenService.getToken(request);
        annualLeave.setOwners(tokenModel.getOwnerList());

        return ApiResult.success(annualLeaveService.getDataList(annualLeave));
    }
}
