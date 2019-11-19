package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;


import com.nt.dao_BASF.Emergencytemplate;
import com.nt.service_BASF.EmergencytemplateServices;
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
 * @ClassName: BASF10801Controller
 * @Author: Wxz
 * @Description: BASF接警单管理模块Controller
 * @Date: 2019/11/18 18：03
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10801")
public class BASF10801Controller {

    @Autowired
    private EmergencytemplateServices emergencytemplateServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取接警单列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18：03
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(emergencytemplateServices.list());
    }

    /**
     * @param emergencytemplate
     * @param request
     * @Method create
     * @Author Wxz
     * @Version 1.0
     * @Description 创建接警单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:03
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Emergencytemplate emergencytemplate, HttpServletRequest request) throws Exception {
        if (emergencytemplate == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        emergencytemplateServices.insert(emergencytemplate, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param emergencytemplate
     * @param request
     * @Method delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除模板
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18：02
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Emergencytemplate emergencytemplate, HttpServletRequest request) throws Exception {
        if (emergencytemplate == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        emergencytemplate.setStatus(AuthConstants.DEL_FLAG_DELETE);
        emergencytemplateServices.delete(emergencytemplate);
        return ApiResult.success();
    }

    /**
     * @param templateid
     * @param request
     * @Method selectById
     * @Author Wxz
     * @Version 1.0
     * @Description 获取接警单详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:01
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String templateid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(templateid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(emergencytemplateServices.one(templateid));
    }

    /**
     * @param emergencytemplate
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新模板详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/18 18:01
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Emergencytemplate emergencytemplate, HttpServletRequest request) throws Exception {
        if (emergencytemplate == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        emergencytemplateServices.update(emergencytemplate, tokenModel);
        return ApiResult.success();
    }
}
