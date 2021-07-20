package com.nt.controller.Controller.PFANS;

import com.nt.service_pfans.PFANS6000.PjExternalInjectionService;
import com.nt.utils.ApiResult;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/pjExternalInjection")
public class Pfans6011Controller {

    @Autowired
    private PjExternalInjectionService pjExternalInjectionService;


    @Autowired
    private TokenService tokenService;

//    @RequestMapping(value = "/saveTableinfo", method = {RequestMethod.GET})
//    public ApiResult saveTableinfo(String year, HttpServletRequest request) throws Exception {
//        pjExternalInjectionService.saveTableinfo(year);
//        return ApiResult.success();
//    }

    //查看
    @RequestMapping(value = "/getTableinfo", method = {RequestMethod.GET})
    public ApiResult getTableinfo(String year, String group_id, HttpServletRequest request) throws Exception {
        return ApiResult.success(pjExternalInjectionService.getTableinfo(year, group_id));
    }

    //查看
    @RequestMapping(value = "/getTableinfoReport", method = {RequestMethod.GET})
    public ApiResult getTableinfoReport(String year, String group_id, HttpServletRequest request) throws Exception {
        return ApiResult.success("getTableinfoReport",pjExternalInjectionService.getTableinfo(year, group_id));
    }
}
