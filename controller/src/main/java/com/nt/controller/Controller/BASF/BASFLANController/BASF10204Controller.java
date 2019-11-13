package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.VehicleManagement;
import com.nt.service_BASF.VehicleManagementServices;
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

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF10204Controller
 * @Author: Wxz
 * @Description: BASF车辆状态管理模块Controller
 * @Date: 2019/11/13 16：25
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10204")
public class BASF10204Controller {
    @Autowired
    private VehicleManagementServices vehicleManagementServices;

    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取车辆状态列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/13 16：33
     */
    @RequestMapping(value = "/list",method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request)throws Exception{
        return ApiResult.success(vehicleManagementServices.list());
    }

    /**
     * @param vehicleManagement
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新车辆状态详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/13 16:34
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody VehicleManagement vehicleManagement , HttpServletRequest request) throws Exception {
        if (vehicleManagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        vehicleManagementServices.update(vehicleManagement,tokenModel);
        return ApiResult.success();
    }
}
