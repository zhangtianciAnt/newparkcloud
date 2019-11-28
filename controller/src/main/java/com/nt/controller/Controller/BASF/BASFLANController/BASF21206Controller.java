package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;

import com.nt.dao_BASF.Devicetrainer;
import com.nt.service_BASF.DevicetrainerServices;
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
 * @ClassName: BASF21206Controller
 * @Author: WXL
 * @Description: BASF培训人员管理模块Controller
 * @Date: 2019/11/25 14：20
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF21206")
public class BASF21206Controller {

    @Autowired
    private DevicetrainerServices devicetrainerServices;
    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author WXL
     * @Version 1.0
     * @Description 获取培训人员列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(devicetrainerServices.listVo());
    }

    /**
     * @param devicetrainer
     * @Method create
     * @Author WxL
     * @Version 1.0
     * @Description 创建培训人员
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/20 16:24
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Devicetrainer devicetrainer, HttpServletRequest request) throws Exception {
        if (devicetrainer == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        devicetrainerServices.insert(devicetrainer, tokenModel);
        return ApiResult.success();
    }

    /**
     * @param devicetrainer
     * @param request
     * @Method delete
     * @Author WXL
     * @Version 1.0
     * @Description 删除培训人员
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Devicetrainer devicetrainer, HttpServletRequest request) throws Exception {
        if (devicetrainer == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        devicetrainer.setStatus(AuthConstants.DEL_FLAG_DELETE);
        devicetrainerServices.delete(devicetrainer);
        return ApiResult.success();
    }

    /**
     * @param devicetrainerid
     * @param request
     * @Method selectById
     * @Author WXL
     * @Version 1.0
     * @Description 获取培训人员详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String devicetrainerid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(devicetrainerid)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(devicetrainerServices.one(devicetrainerid));
    }

    /**
     * @param devicetrainer
     * @param request
     * @Method update
     * @Author WXL
     * @Version 1.0
     * @Description 更新项目详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/25 14：20
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Devicetrainer devicetrainer, HttpServletRequest request) throws Exception {
        if (devicetrainer == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        devicetrainerServices.update(devicetrainer, tokenModel);
        return ApiResult.success();
    }
}
