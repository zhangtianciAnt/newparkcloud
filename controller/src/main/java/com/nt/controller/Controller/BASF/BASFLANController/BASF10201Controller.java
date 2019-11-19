package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Firealarm;
import com.nt.service_BASF.FirealarmServices;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10201Controller
 * @Author: Wxz
 * @Description: BASF接警单管理模块Controller
 * @Date: 2019/11/12 11：39
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10201")
public class BASF10201Controller {

    @Autowired
    private FirealarmServices firealarmServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取接警单列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13：10
     */
    @RequestMapping(value = "/list",method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request)throws Exception{
        return ApiResult.success(firealarmServices.list());
    }

    /**
     * @param firealarm
     * @param request
     * @Method create
     * @Author Wxz
     * @Version 1.0
     * @Description 创建接警单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:20
     */
    @RequestMapping(value = "/create",method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Firealarm firealarm, HttpServletRequest request)throws Exception{
        if (firealarm == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        firealarmServices.insert(firealarm,tokenModel);
        return ApiResult.success(firealarmServices.insert(firealarm, tokenModel));
    }

    /**
     * @param firealarm
     * @param request
     * @Method delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除接警单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13：31
     */
    @RequestMapping(value = "/delete",method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Firealarm firealarm,HttpServletRequest request)throws Exception{
        if (firealarm == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        firealarm.setStatus(AuthConstants.DEL_FLAG_DELETE);
        firealarmServices.delete(firealarm);
        return ApiResult.success();
    }

    /**
     * @param firealarmid
     * @param request
     * @Method selectById
     * @Author Wxz
     * @Version 1.0
     * @Description 获取接警单详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:35
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String firealarmid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(firealarmid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(firealarmServices.one(firealarmid));
    }

    /**
     * @param firealarm
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新接警单详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/12 13:38
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody  Firealarm firealarm , HttpServletRequest request) throws Exception {
        if (firealarm == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        firealarmServices.update(firealarm,tokenModel);
        return ApiResult.success();
    }
}
