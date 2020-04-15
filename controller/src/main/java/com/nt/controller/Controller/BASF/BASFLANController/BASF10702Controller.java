package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.BlackList;
import com.nt.dao_BASF.Vehicleinformation;
import com.nt.service_BASF.VehicleinformationServices;
import com.nt.utils.dao.TokenModel;
import com.nt.service_BASF.BlackListServices;
import com.nt.utils.*;
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
 * @ClassName: BASF10702Controller
 * @Author: Wxz
 * @Description: BASF10702Controller
 * @Date: 2019/11/22 15:16
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF10702")
public class BASF10702Controller {

    @Autowired
    private BlackListServices blackListServices;

    @Autowired
    private VehicleinformationServices vehicleinformationServices;

    @Autowired
    private TokenService tokenService;

    /**
     * @param blackList
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取黑名单列表
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/22 15：22
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody(required = false) BlackList blackList) throws Exception {
        return ApiResult.success(blackListServices.list(blackList));
    }

    /**
     * @param driverIdNo
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 查询是否为黑名单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/28 9:37
     */
    @RequestMapping(value = "/checkblack", method = {RequestMethod.POST})
    public ApiResult checkblack(String driverIdNo) throws Exception {
        return ApiResult.success(blackListServices.checkblack(driverIdNo));
    }

    /**
     * @param blackList
     * @param request
     * @Method delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除驾驶员黑名单信息
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/29 10：15
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody BlackList blackList, HttpServletRequest request) throws Exception {
        if (blackList == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        blackList.setStatus(AuthConstants.DEL_FLAG_DELETE);
        blackListServices.delete(blackList);
        return ApiResult.success();
    }

    /**
     * @param vehicleinformation
     * @param
     * @Method create
     * @Author Sun
     * @Version 1.0
     * @Description 创建车辆进出厂信息
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/4 19:39
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(String vehiclenumber,String driver) throws Exception {

        Vehicleinformation vehicleinformation = new Vehicleinformation();
        vehicleinformation.setVehiclenumber(vehiclenumber);
        vehicleinformation.setDriver(driver);
        vehicleinformationServices.insert(vehicleinformation);


        return ApiResult.success();
    }
}
