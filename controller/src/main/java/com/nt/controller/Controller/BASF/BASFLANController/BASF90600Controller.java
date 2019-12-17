package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.service_BASF.VehicleinformationServices;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.BASF.BASFLANController
 * @ClassName: BASF90600Controller
 * @Author: SKIAXX
 * @Description: ERC大屏车辆定位模块接口
 * @Date: 2019/12/4 13:40
 * @Version: 1.0
 */
@RestController
@RequestMapping("/BASF90600")
public class BASF90600Controller {

    @Autowired
    private VehicleinformationServices vehicleinformationServices;

    /**
     * @Method getInsideList
     * @Author SKAIXX
     * @Version  1.0
     * @Description 获取在场车辆信息一览
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/16 13:59
     */
    @RequestMapping(value = "/getInsideList", method = {RequestMethod.GET})
    public ApiResult getInsideList() throws Exception {
        //  获取在场车辆信息一览
        return ApiResult.success(vehicleinformationServices.getInsideList());
    }

    /**
     * @Method getAccessStatistics
     * @Author SKAIXX
     * @Version  1.0
     * @Description 车辆出入统计
     * @param period 统计周期
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/16 14:18
     */
    @RequestMapping(value = "/getAccessStatistics", method = {RequestMethod.GET})
    public ApiResult getAccessStatistics(String period) throws Exception {
        //  车辆出入统计
        return ApiResult.success(vehicleinformationServices.getAccessStatistics());
    }

    /**
     * @Method getDailyVehicleInfo
     * @Author SKAIXX
     * @Version  1.0
     * @Description 获取当日入场车辆信息
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/16 14:21
     */
    @RequestMapping(value = "/getDailyVehicleInfo", method = {RequestMethod.GET})
    public ApiResult getDailyVehicleInfo() throws Exception {
        //  获取当日入场车辆信息
        return ApiResult.success(vehicleinformationServices.getDailyVehicleInfo());
    }
}
