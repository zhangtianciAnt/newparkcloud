package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/companyprojects")
public class Pfans5001Controller {

    @Autowired
    private CompanyProjectsService companyProjectsService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody CompanyProjects companyProjects, HttpServletRequest request) throws Exception {
        if (companyProjects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        CompanyProjects log=companyProjectsService.One(companyProjects.getCompanyprojects_id());
        return ApiResult.success(log);
    }

    @RequestMapping(value="/list", method={RequestMethod.POST})
    public ApiResult List(@RequestBody CompanyProjects companyProjects, HttpServletRequest request) throws Exception {
        if (companyProjects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        companyProjects.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(companyProjectsService.list(companyProjects));
    }


    @RequestMapping(value="/updateInfo",method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody CompanyProjects companyProjects, HttpServletRequest request) throws Exception{
        if (companyProjects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        companyProjectsService.upd(companyProjects,tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/createNewUser",method={RequestMethod.POST})
    public ApiResult create(@RequestBody CompanyProjects companyProjects, HttpServletRequest request) throws Exception {
        if (companyProjects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        companyProjectsService.insert(companyProjects,tokenModel);
        return ApiResult.success();
    }

}
