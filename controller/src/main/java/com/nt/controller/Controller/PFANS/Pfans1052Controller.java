package com.nt.controller.Controller.PFANS;

import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS1000.DepartmentalInsideService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/departmentalinside")
public class Pfans1052Controller {

    @Autowired
    DepartmentalInsideService departmentalInsideService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/insert")
    public ApiResult insert(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        departmentalInsideService.insert();
        return ApiResult.success();
    }
}
