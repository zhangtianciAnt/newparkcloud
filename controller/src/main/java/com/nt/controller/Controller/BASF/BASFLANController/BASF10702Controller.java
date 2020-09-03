package com.nt.controller.Controller.BASF.BASFLANController;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Config.BASF.MultiThreadScheduleTask;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.dao_BASF.BlackList;
import com.nt.dao_BASF.Vehicleinformation;
import com.nt.service_BASF.BlackListServices;
import com.nt.service_BASF.VehicleinformationServices;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

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
     * @param driverIdNo
     * @Method list
     * @Author SKAIXX
     * @Version 1.0
     * @Description 添加黑名单
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/28 9:37
     */
    @RequestMapping(value = "/createBlack", method = {RequestMethod.POST})
    public ApiResult createBlack(String drivername,
                                 String driveridnumber, String violationtype) throws Exception {
        BlackList blackList = new BlackList();
        blackList.setDriveridnumber(driveridnumber);
        blackList.setViolationstime(String.valueOf(new Date()));
        blackList.setViolationtype(violationtype);
        blackList.setDrivername(drivername);
        blackList.setBlacklistid(UUID.randomUUID().toString());
        blackList.preInsert();
        blackListServices.createBlack(blackList);
        return ApiResult.success();
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
     * @param
     * @param
     * @Method create
     * @Author Sun
     * @Version 1.0
     * @Description 创建车辆进出厂信息
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/4 19:39
     */
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(
            String vehiclenumber,
            String driver,
            String driverid,
            String escortname,
            String escortid,
            String vehicletype,
            String supplier,
            String transporter,
            String transtype,
            String linkdepartment,
            String goodsname,
            String weight,
            String ishazardous
    ) throws Exception {

        Vehicleinformation vehicleinformation = new Vehicleinformation();
        vehicleinformation.setVehiclenumber(vehiclenumber);
        vehicleinformation.setDriver(driver);
        vehicleinformation.setDriverid(driverid);
        vehicleinformation.setEscortname(escortname);
        vehicleinformation.setEscortid(escortid);
        vehicleinformation.setVehicletype(vehicletype);
        vehicleinformation.setSupplier(supplier);
        vehicleinformation.setTransporter(transporter);
        vehicleinformation.setTranstype(transtype);
        vehicleinformation.setLinkdepartment(linkdepartment);
        vehicleinformation.setGoodsname(goodsname);
        vehicleinformation.setWeight(weight);
        ishazardous = ishazardous.equals("是") ? "0" : "1";
        vehicleinformation.setIshazardous(ishazardous);
        vehicleinformation.setIntime(new Date());

        String result = vehicleinformationServices.insert(vehicleinformation);

        // websocket推送信息给大屏，改变在厂车辆，车辆查询，实时车辆清单，本周车辆概况，本月车辆概况数据
        // 获取 大屏车辆定位-在厂车辆 个类型数量
        MultiThreadScheduleTask.webSocketVo.setInsideVehicleTypeVoList(vehicleinformationServices.getInsideVehicleType());
        // 获取 大屏车辆定位-实时车辆清单 数据
        MultiThreadScheduleTask.webSocketVo.setVehicleinformationList(vehicleinformationServices.getDailyVehicleInfo());
        // 获取 大屏车辆定位-车辆查询 数据
        MultiThreadScheduleTask.webSocketVo.setInsideVehicleinformationVoList(vehicleinformationServices.getInsideList());
        // 获取 本月车辆概况 数据
        MultiThreadScheduleTask.webSocketVo.setVehicleAccessStatisticsVoList(vehicleinformationServices.getAccessStatistics());
        // 获取 本周车辆概况 数据
        MultiThreadScheduleTask.webSocketVo.setVehicleWeekAccessStatisticsVoList(vehicleinformationServices.getWeekAccessStatistics());
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));

        return ApiResult.success(result);
    }

    /**
     * @param
     * @param
     * @Method updategps
     * @Author Sun
     * @Version 1.0
     * @Description 更新车辆信息中的GPS
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/4 19:39
     */
    @RequestMapping(value = "/updategps", method = {RequestMethod.POST})
    public ApiResult updategps(String vehicleinformationid, String gps, String speed) throws Exception {
        if (StringUtils.isNotEmpty(vehicleinformationid) && StringUtils.isNotEmpty(speed)) {
            vehicleinformationServices.updategps(vehicleinformationid, gps, speed);
        }
        return ApiResult.success();
    }


    /**
     * @param
     * @param
     * @Method updateouttime
     * @Author Sun
     * @Version 1.0
     * @Description 更新车辆信息中出场时间
     * @Return com.nt.utils.ApiResult
     * @Date 2019/11/4 19:39
     */
    @RequestMapping(value = "/updateouttime", method = {RequestMethod.POST})
    public ApiResult updateouttime(String vehicleinformationid) throws Exception {

        Vehicleinformation vehicleinformation = new Vehicleinformation();
        vehicleinformation.setVehicleinformationid(vehicleinformationid);
        vehicleinformationServices.updateouttime(vehicleinformation);
        return ApiResult.success();
    }
}
