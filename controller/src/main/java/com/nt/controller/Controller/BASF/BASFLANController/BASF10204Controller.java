package com.nt.controller.Controller.BASF.BASFLANController;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Config.BASF.MultiThreadScheduleTask;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.dao_BASF.MhInfo;
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
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request, @RequestBody VehicleManagement vehicleManagement) throws Exception {
        return ApiResult.success(vehicleManagementServices.list(vehicleManagement));
    }

    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(HttpServletRequest request, @RequestBody VehicleManagement vehicleManagement) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        int result = vehicleManagementServices.insert(vehicleManagement, tokenModel);
        if (result > 0) {
            return ApiResult.success();
        } else {
            return ApiResult.fail("创建消防车数据失败！");
        }
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
    public ApiResult update(@RequestBody VehicleManagement vehicleManagement, HttpServletRequest request) throws Exception {
        if (vehicleManagement == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        vehicleManagementServices.update(vehicleManagement, tokenModel);
        return ApiResult.success();
    }

    /**
     * 根据id更新车辆gps信息
     *
     * @param vehicleManagement
     * @return
     */
    @RequestMapping(value = "/sendGps", method = {RequestMethod.POST})
    public ApiResult sendGps(@RequestBody VehicleManagement vehicleManagement) throws Exception {
        vehicleManagementServices.update(vehicleManagement, null);
        // 查询所有消防车最后的gps信息返回给前端
        List<VehicleManagement> vehicleManagements = vehicleManagementServices.list(vehicleManagement);
        List<MhInfo> mhInfos = new ArrayList<>();
        for (VehicleManagement v : vehicleManagements) {
            MhInfo mhInfo = new MhInfo();
            mhInfo.setVehicleManagement(v);
            mhInfo.setEnable(v.getEnable());
            //因为现在没有app和后台的心跳线，所以返回默认所有车都是在线状态
            mhInfo.setRunStatus(1);
            mhInfo.setLat(Double.valueOf(v.getLat()));
            mhInfo.setLng(Double.valueOf(v.getLng()));
            mhInfos.add(mhInfo);
        }
        MultiThreadScheduleTask.webSocketVo.setCarSet(mhInfos);
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        return ApiResult.success();
    }
}
