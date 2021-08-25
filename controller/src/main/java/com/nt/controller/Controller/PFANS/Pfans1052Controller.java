package com.nt.controller.Controller.PFANS;

import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS1000.DepartmentalInsideService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/insert")
    public ApiResult getTableinfo(@RequestParam String year,@RequestParam String group_id, HttpServletRequest request) throws Exception {
        return ApiResult.success(departmentalInsideService.getTableinfo(year, group_id));
    }
}
