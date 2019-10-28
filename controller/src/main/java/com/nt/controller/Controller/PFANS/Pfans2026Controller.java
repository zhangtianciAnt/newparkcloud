package com.nt.controller.Controller.PFANS;


import com.nt.dao_Pfans.PFANS2000.Leaveoffice;
import com.nt.service_pfans.PFANS2000.LeaveofficeService;
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
@RequestMapping("/leaveoffice")
public class Pfans2026Controller {

    @Autowired
    private LeaveofficeService leaveofficeService;

    @Autowired
    private TokenService tokenService;

    /*
     * 列表查看
     * */
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Leaveoffice leaveoffice = new Leaveoffice();
        // leaveoffice.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(leaveofficeService.get(leaveoffice));
    }

    /**
     * 查看一个人
     */
    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody Leaveoffice leaveoffice, HttpServletRequest request) throws Exception {
        if (leaveoffice == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenMode1 = tokenService.getToken(request);
        return ApiResult.success(leaveofficeService.one(leaveoffice.getLeaveoffice_id()));
    }


    /**
     * 新建
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Leaveoffice leaveoffice, HttpServletRequest request) throws Exception {
        if (leaveoffice == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        leaveofficeService.create(leaveoffice, tokenModel);
        return ApiResult.success();
    }

    /**
     *
     * 修改
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Leaveoffice leaveoffice, HttpServletRequest request) throws Exception {
        if(leaveoffice==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        leaveofficeService.update(leaveoffice,tokenModel);
        return ApiResult.success();

    }


}
