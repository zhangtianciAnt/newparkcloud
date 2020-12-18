package com.nt.controller.Controller.PFANS;

import cn.hutool.core.date.DateUtil;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/deleteLog", method = {RequestMethod.POST})
    public ApiResult deleteLog(@RequestBody LogManagement logmanagement, HttpServletRequest request) throws Exception {
        if (logmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        logmanagementService.delete(logmanagement);
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

    @RequestMapping(value = "/getLogDataList", method = {RequestMethod.GET})
    public ApiResult getLogDataList(String startDate ,String endDate , HttpServletRequest request) throws Exception {
        LogManagement logmanagement = new LogManagement();
        SimpleDateFormat sf1ymd = new SimpleDateFormat("yyyy-MM-dd");
        TokenModel tokenModel = tokenService.getToken(request);
        logmanagement.setOwners(tokenModel.getOwnerList());
        List<LogManagement> list = logmanagementService.getLogDataList(logmanagement,startDate,endDate);
        List<LogManagement> list1 = logmanagementService.getDataListPL(tokenModel);
        for (LogManagement item : list1) {
            if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate))
            {
                if(sf1ymd.parse(DateUtil.format(item.getLog_date(),"yyyy-MM-dd")).getTime()>=sf1ymd.parse(startDate).getTime()
                    && sf1ymd.parse(DateUtil.format(item.getLog_date(),"yyyy-MM-dd")).getTime()<=sf1ymd.parse(endDate).getTime())
                {
                    if (list.stream().filter(item2 -> item2.getLogmanagement_id().equals(item.getLogmanagement_id())).count() == 0) {
                        list.add(item);
                    }
                }
            }
            else if(StringUtils.isNotBlank(startDate))
            {
                if(sf1ymd.parse(DateUtil.format(item.getLog_date(),"yyyy-MM-dd")).getTime()>=sf1ymd.parse(startDate).getTime())
                {
                    if (list.stream().filter(item2 -> item2.getLogmanagement_id().equals(item.getLogmanagement_id())).count() == 0) {
                        list.add(item);
                    }
                }
            }
            else if(StringUtils.isNotBlank(endDate))
            {
                if(sf1ymd.parse(DateUtil.format(item.getLog_date(),"yyyy-MM-dd")).getTime()<=sf1ymd.parse(endDate).getTime())
                {
                    if (list.stream().filter(item2 -> item2.getLogmanagement_id().equals(item.getLogmanagement_id())).count() == 0) {
                        list.add(item);
                    }
                }
            }
        }
        return ApiResult.success(list);
    }

    @RequestMapping(value = "/getDataList1", method = {RequestMethod.POST})
    public ApiResult getDataList1(@RequestBody LogManagement conditon, HttpServletRequest request) throws Exception {
        if (conditon == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        LogManagement logmanagement = new LogManagement();
        logmanagement.setOwners(tokenModel.getOwnerList());
        logmanagement.setLog_date(conditon.getLog_date());
        List<LogManagement> list = logmanagementService.getDataListByLog_date(logmanagement);
        //add_fjl_0716_添加PL权限的人查看日志一览  start
        List<LogManagement> list1 = logmanagementService.getDataListPL(tokenModel);
        for (LogManagement item : list1) {
            //upd ccm 1118 日志管理可以看到外注员工的数据 fr
//            if (list.stream().filter(item2 -> item2.getProject_id().equals(item.getProject_id())).count() == 0) {
//                list.add(item);
//            }
            if(DateUtil.format(item.getLog_date(),"yyyy/MM").equals(
                    DateUtil.format(conditon.getLog_date(),"yyyy/MM")))
            {
                if (list.stream().filter(item2 -> item2.getLogmanagement_id().equals(item.getLogmanagement_id())).count() == 0) {
                    list.add(item);
                }
            }
            //upd ccm 1118 日志管理可以看到外注员工的数据 to
        }
        //add_fjl_0716_添加PL权限的人查看日志一览  end
        list = list.stream().sorted(Comparator.comparing(LogManagement::getLog_date).reversed()).collect(Collectors.toList());
        return ApiResult.success(list);
    }
    // add-ws-5/26-No.68
    @RequestMapping(value = "/getDataList2", method = {RequestMethod.POST})
    public ApiResult getDataList2(@RequestBody LogManagement conditon, HttpServletRequest request) throws Exception {
        if (conditon == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        LogManagement logmanagement = new LogManagement();
        logmanagement.setOwners(tokenModel.getOwnerList());
        List<LogManagement> list = logmanagementService.getDataList(logmanagement);
        list = list.stream().filter(item -> item.getCreateby().equals(conditon.getCreateby())).collect(Collectors.toList());
        return ApiResult.success(list);
    }
    // add-ws-5/26-No.68
    @RequestMapping(value = "/getCheckList", method = {RequestMethod.POST})
    public ApiResult getCheckList(@RequestBody LogManagement logmanagement, HttpServletRequest request) throws Exception {
        if (logmanagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(logmanagementService.getCheckList(logmanagement));
    }

    @RequestMapping(value = "/CheckList", method = {RequestMethod.POST})
    public ApiResult CheckList(@RequestBody Projectsystem projectsystem, HttpServletRequest request) throws Exception {
        if (projectsystem == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(logmanagementService.CheckList(projectsystem,tokenModel));
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

