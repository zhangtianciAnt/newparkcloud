package com.nt.controller.Controller.PFANS;



import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
import com.nt.service_pfans.PFANS2000.StaffexitprocedureService;
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
@RequestMapping("/staffexitprocedure")
public class Pfans2026Controller {

    @Autowired
    private StaffexitprocedureService staffexitprocedureService;
    @Autowired
    private TokenService tokenService;

    /*
     * 列表查看
     * */
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Staffexitprocedure staffexitprocedure=new Staffexitprocedure();
        staffexitprocedure.setOwners(tokenModel.getOwnerList());
       return ApiResult.success(staffexitprocedureService.get(staffexitprocedure));
    }

    /**
     * 查看一个人
     */
    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody Staffexitprocedure staffexitprocedure, HttpServletRequest request) throws Exception {
        if(staffexitprocedure==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(staffexitprocedureService.one(staffexitprocedure.getStaffexitprocedure_id()));
    }


    /**
     * 新建
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Staffexitprocedure staffexitprocedure, HttpServletRequest request) throws Exception {
        if(staffexitprocedure==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        staffexitprocedureService.create(staffexitprocedure,tokenModel);
        return ApiResult.success();
    }

    /**
     *
     * 修改
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Staffexitprocedure staffexitprocedure, HttpServletRequest request) throws Exception {
        if(staffexitprocedure==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        staffexitprocedureService.update(staffexitprocedure,tokenModel);
        return ApiResult.success();

    }

}
