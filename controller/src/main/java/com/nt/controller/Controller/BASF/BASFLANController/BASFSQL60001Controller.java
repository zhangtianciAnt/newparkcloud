package com.nt.controller.Controller.BASF.BASFLANController;



import com.nt.service_SQL.AccessLevelServices;
import com.nt.service_SQL.SqlUserInfoServices;
import com.nt.utils.ApiResult;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/BASFSQL60001")
public class BASFSQL60001Controller {

    @Autowired
    private AccessLevelServices accessLevelServices;
    @Autowired
    private SqlUserInfoServices sqluserInfoServices;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list( HttpServletRequest request) throws Exception {
        return ApiResult.success(accessLevelServices.list());
    }


    @RequestMapping(value = "/sqluserinfolist", method = {RequestMethod.GET})
    public ApiResult sqluserinfolist( HttpServletRequest request) throws Exception {
        return ApiResult.success(sqluserInfoServices.list());
    }


}





