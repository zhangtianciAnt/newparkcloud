package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Environment;
import com.nt.service_BASF.EnvironmentServices;
import com.nt.utils.*;
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
 * @ClassName: BASF10301Controller
 * @Author: WXL
 * @Description: BASF环保工艺数据监控管理模块Controller
 * @Date: 2019/11/14 16：30
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10301")
public class BASF10301Controller {

    @Autowired
    private EnvironmentServices environmentServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author WXL
     * @Version 1.0
     * @Description 获取监控数据列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/14 16：35
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(environmentServices.list());
    }

    /**
     * @param environment
     * @param request
     * @Method create
     * @Author Wxz
     * @Version 1.0
     * @Description 创建接警单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:20
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Environment environment, HttpServletRequest request) throws Exception {
        if (environment == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        environmentServices.insert(environment, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param environment
     * @param request
     * @Method delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除接警单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13：31
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Environment environment, HttpServletRequest request) throws Exception {
        if (environment == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        environment.setStatus(AuthConstants.DEL_FLAG_DELETE);
        environmentServices.delete(environment);
        return ApiResult.success();
    }

    /**
     * @param environmentid
     * @param request
     * @Method selectById
     * @Author Wxz
     * @Version 1.0
     * @Description 获取接警单详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:35
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String environmentid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(environmentid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(environmentServices.one(environmentid));
    }

    /**
     * @param environment
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新接警单详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:38
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Environment environment, HttpServletRequest request) throws Exception {
        if (environment == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        environmentServices.update(environment, tokenModel);
        return ApiResult.success();
    }
}
