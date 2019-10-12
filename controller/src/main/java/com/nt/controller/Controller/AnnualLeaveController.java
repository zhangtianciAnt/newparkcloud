package com.nt.controller.Controller;

import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.service_pfans.PFANS2000.AnnualLeaveService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @name 年度休假一览
 * @author lin
 */
@RestController
@RequestMapping("/annualLeave")
public class AnnualLeaveController {

    @Autowired
    private AnnualLeaveService annualLeaveService;

    @Autowired
    private TokenService tokenService;

    /**
     * @方法名：getDataList
     * @参数：[annual_leave, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getDataList", method={RequestMethod.GET})
    public ApiResult getDataList(HttpServletRequest request) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(annualLeaveService.getDataList());
    }
}
