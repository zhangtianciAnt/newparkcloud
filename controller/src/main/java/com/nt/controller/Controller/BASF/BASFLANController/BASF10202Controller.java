package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Application;
import com.nt.service_BASF.ApplicationServices;
import com.nt.utils.ApiResult;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10202Controller
 * @Author: LXY
 * @Description: 消防设备申请审核Controller
 * @Date: 2019/11/12 11.10
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10202")
public class BASF10202Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApplicationServices applicationServices;

    @RequestMapping(value = "/get", method = {RequestMethod.POST})
    public ApiResult get(@RequestBody Application application) throws Exception {
        return ApiResult.success(applicationServices.get(application));
    }

    @RequestMapping(value = "/getApplicationList", method = {RequestMethod.POST})
    public ApiResult getApplicationList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(applicationServices.getList());
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody Application application, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        applicationServices.insert(tokenModel, application);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Application application, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        applicationServices.update(tokenModel, application);
        return ApiResult.success();
    }

    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(@RequestBody Application application, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        application.setStatus(AuthConstants.DEL_FLAG_DELETE);
        applicationServices.del(tokenModel, application);
        return ApiResult.success();
    }
}
