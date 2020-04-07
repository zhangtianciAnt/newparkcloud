package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.PersonalProjects;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.dao_Pfans.PFANS5000.Vo.PersonalProjectsVo;
import com.nt.service_pfans.PFANS5000.LogManagementService;
import com.nt.service_pfans.PFANS5000.CompanyProjectsService;
import com.nt.service_pfans.PFANS5000.PersonalProjectsService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/logmanagement")
public class Pfans5008Controller {

    @Autowired
    private LogManagementService logmanagementService;

    @Autowired
    private CompanyProjectsService companyprojectsService;

    @Autowired
    private PersonalProjectsService personalprojectsService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/createNewUser", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody LogManagement logmanagement, HttpServletRequest request) throws Exception {
        if (logmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        logmanagementService.insert(logmanagement, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getListcheck", method = {RequestMethod.POST})
    public ApiResult getListcheck(@RequestBody LogManagement logmanagement, HttpServletRequest request) throws Exception {
        if (logmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(logmanagementService.getListcheck(logmanagement, tokenModel));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody LogManagement logmanagement, HttpServletRequest request) throws Exception {
        if (logmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        logmanagementService.update(logmanagement, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody LogManagement logmanagement, HttpServletRequest request) throws Exception {
        if (logmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        LogManagement log = logmanagementService.One(logmanagement.getLogmanagement_id());
        return ApiResult.success(log);
    }

    @RequestMapping(value = "/getCompanyProjectList", method = {RequestMethod.POST})
    public ApiResult getCompanyProjectList(@RequestBody CompanyProjects companyprojects, HttpServletRequest request) throws Exception {
        if (companyprojects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(companyprojectsService.getCompanyProjectList(companyprojects, request));
    }

    @RequestMapping(value = "/getProjectList", method = {RequestMethod.POST})
    public ApiResult getProjectList(@RequestBody PersonalProjects personalprojects,  HttpServletRequest request) throws Exception {
        if (personalprojects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        personalprojects.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(personalprojectsService.getProjectList(personalprojects));
    }

    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void download(String type, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        String templateName = null;
        String fileName = null;
        if ( "0".equals(type) ) {
            templateName = "rizhiguanli.xlsx";
            fileName = "日志管理";
        }
        if (templateName != null ) {
            ExcelOutPutUtil.OutPut(fileName,templateName,data,response);
        }
    }

    @RequestMapping(value = "/createProject", method = {RequestMethod.POST})
    public ApiResult createProject(@RequestBody PersonalProjectsVo personalprojectsvo, HttpServletRequest request) throws Exception {
        if (personalprojectsvo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        personalprojectsService.insert(personalprojectsvo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult deleteadditional(@RequestBody PersonalProjects personalprojects, HttpServletRequest request) throws Exception {
        if (personalprojects == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        personalprojectsService.delete(personalprojects, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getDataList", method = {RequestMethod.POST})
    public ApiResult getDataList(@RequestBody LogManagement logmanagement, HttpServletRequest request) throws Exception {
        if (logmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        logmanagement.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(logmanagementService.getDataList(logmanagement));
    }

    @RequestMapping(value = "/CheckList", method = {RequestMethod.POST})
    public ApiResult CheckList(@RequestBody Projectsystem projectsystem, HttpServletRequest request) throws Exception {
        if (projectsystem == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(logmanagementService.CheckList(projectsystem));
    }


    @RequestMapping(value = "/gettlist", method = {RequestMethod.POST})
    public ApiResult gettlist(@RequestBody LogManagement logmanagement, HttpServletRequest request) throws Exception {
        if (logmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(logmanagementService.gettlist());
    }

    @RequestMapping(value = "/importUser",method={RequestMethod.POST})
    public ApiResult importUser(HttpServletRequest request,String flag){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(logmanagementService.importUser(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/downloadUserModel",method={RequestMethod.GET})
    public ApiResult downloadUserModel(){
        try{
            return ApiResult.success(logmanagementService.downloadUserModel());
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        } catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
}

