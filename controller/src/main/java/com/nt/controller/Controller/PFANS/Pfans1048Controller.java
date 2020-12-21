package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.PolicyContract;
import com.nt.dao_Pfans.PFANS1000.ProjectIncome;
import com.nt.service_pfans.PFANS1000.ProjectIncomeService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/projectincome")
public class Pfans1048Controller {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ProjectIncomeService projectincomeservice;

    @PostMapping("/importUser")
    public ApiResult importUser(HttpServletRequest request,String flag){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(projectincomeservice.importUser(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @GetMapping("/selectById")
    public ApiResult selectById(String projectincomeid, HttpServletRequest request) throws Exception {
        if (projectincomeid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(projectincomeservice.selectById(projectincomeid));
    }

    @GetMapping("/get")
    public ApiResult get(HttpServletRequest request) throws Exception {
        ProjectIncome projectincome = new ProjectIncome();
        return ApiResult.success(projectincomeservice.get(projectincome));
    }

    @GetMapping("/getprojects")
    public ApiResult getprojects(String groupid,String userid, String year, String month, HttpServletRequest request) throws Exception {
        return ApiResult.success(projectincomeservice.getprojects(groupid,userid,year,month));
    }

    @PostMapping("/insert")
    public ApiResult insert(@RequestBody ProjectIncome projectincome, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        projectincomeservice.insert(projectincome,tokenModel);
        return ApiResult.success();
    }
}
