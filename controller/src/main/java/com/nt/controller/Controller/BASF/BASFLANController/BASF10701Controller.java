package com.nt.controller.Controller.BASF.BASFLANController;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_BASF.Vehicleinformation;
import com.nt.service_BASF.VehicleinformationServices;
import com.nt.service_BASF.VehicletrajectoryServices;
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
 * @ClassName: BASF10701Controller
 * @Author: Wxz
 * @Description: BASF10701Controller
 * @Date: 2019/11/14 13:46
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10701")
public class BASF10701Controller {

    @Autowired
    private VehicleinformationServices vehicleinformationServices;

    @Autowired
    private VehicletrajectoryServices vehicletrajectoryServices;

    @Autowired
    private TokenService tokenService;

    /**
     * @param request
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取车辆详情列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/14 13：49
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(vehicleinformationServices.list());
    }

    /**
     * @param vehicleinformationid
     * @param request
     * @Method selectById
     * @Author Wxz
     * @Version 1.0
     * @Description 获取车辆轨迹信息详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/14 14：01
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String vehicleinformationid, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(vehicleinformationid)){
            return ApiResult.success(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(vehicletrajectoryServices.one(vehicleinformationid));
    }

    /**
     * @param vehicleinformation
     * @param request
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新车辆信息详情
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/14 14:04
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody  Vehicleinformation vehicleinformation , HttpServletRequest request) throws Exception {
        if (vehicleinformation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        vehicleinformationServices.update(vehicleinformation,tokenModel);
        return ApiResult.success();
    }
}
