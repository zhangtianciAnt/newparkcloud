package com.nt.controller.Config.BASF;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketVo;
import com.nt.service_BASF.*;
import com.nt.service_SQL.sqlMapper.BasfUserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.time.LocalDateTime;

/**
 * basf多线程定时任务
 */
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class MultiThreadScheduleTask {

    @Autowired
    @SuppressWarnings("all")
    BasfUserInfoMapper basfUserInfoMapper;

    @Autowired
    @SuppressWarnings("all")
    VehicleinformationServices vehicleinformationServices;

    @Autowired
    @SuppressWarnings("all")
    FirealarmServices firealarmServices;

    @Autowired
    @SuppressWarnings("all")
    StartprogramServices startprogramServices;

    @Autowired
    @SuppressWarnings("all")
    TrainjoinlistServices trainjoinlistServices;

    @Autowired
    @SuppressWarnings("all")
    RiskassessmentServices riskassessmentServices;

    @Autowired
    EmergencyplanServices emergencyplanServices;

    @Autowired
    private ResponseinformationServices responseinformationServices;

    // websocket消息推送
    private WebSocket ws = new WebSocket();
    // WebSocketVow
    private WebSocketVo webSocketVo = new WebSocketVo();

    /**
     * @return void
     * @Method selectUsersCount
     * @Author MYT
     * @Description ERC大屏在厂人数统计
     * @Date 2020/1/8 14:39
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void selectUsersCount() throws Exception {
        System.out.println("执行 查询员工人数 定时任务: " + LocalDateTime.now().toLocalTime()
                + "\r\n线程 : " + Thread.currentThread().getName());
        // 查询员工人数
        int usersCount = basfUserInfoMapper.selectUsersCount();
        // 查询承包商人数
        int contractorsCount = basfUserInfoMapper.selectContractorsCount();
        // 查询访客人数
        int visitorsCount = basfUserInfoMapper.selectVisitorsCount();
        // 在厂总人数
        int allUsersCount = usersCount + contractorsCount + visitorsCount;
        webSocketVo.setUsersCount(usersCount);
        webSocketVo.setContractorsCount(contractorsCount);
        webSocketVo.setVisitorsCount(visitorsCount);
        webSocketVo.setAllUsersCount(allUsersCount);
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return void
     * @Method selectDeviceUsersCount
     * @Author MYT
     * @Description ERC大屏8个装置人数统计
     * @Date 2020/1/8 14:39
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void selectDeviceUsersCount() throws Exception {
        // 8个装置人数统计
        webSocketVo.setDeviceUsersCountList(basfUserInfoMapper.selectDeviceUsersCount());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    // region BASF90600 ERC-车辆定位

    /**
     * @return void
     * @Method BASF90600_GetInsideList
     * @Author SKAIXX
     * @Description ERC大屏车辆定位模块获取在场车辆信息一览
     * @Date 2019/12/30 15:47
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90600_GetInsideList() throws Exception {
        // 获取在场车辆信息一览
        webSocketVo.setInsideVehicleinformationVoList(vehicleinformationServices.getInsideList());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return void
     * @Method BASF90600_GetInsideVehicleType
     * @Author SKAIXX
     * @Description ERC大屏车辆定位模块在场车辆类别统计
     * @Date 2019/12/30 16:27
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90600_GetInsideVehicleType() throws Exception {
        // 在场车辆类别统计
        webSocketVo.setInsideVehicleTypeVoList(vehicleinformationServices.getInsideVehicleType());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return
     * @Method BASF90600_GetAccessStatistics
     * @Author SKAIXX
     * @Description ERC大屏车辆定位模块车辆出入统计
     * @Date 2019/12/30 19:09
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90600_GetAccessStatistics() throws Exception {
        // 车辆出入统计
        webSocketVo.setVehicleAccessStatisticsVoList(vehicleinformationServices.getAccessStatistics());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    /**
     * @return
     * @Method BASF90600_GetDailyVehicleInfo
     * @Author SKAIXX
     * @Description ERC大屏车辆定位模块获取当日入场车辆信息
     * @Date 2019/12/31 11:27
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90600_GetDailyVehicleInfo() throws Exception {
        // 获取当日入场车辆信息
        webSocketVo.setVehicleinformationList(vehicleinformationServices.getDailyVehicleInfo());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }
    // endregion

    // region BASF90200 ERC-火灾消防

    /**
     * @return 近30日接警数据分析
     * @Method BASF90200_GetFireAlarmStatistics
     * @Author SKAIXX
     * @Description 获取当月接警数据分析
     * @Date 2020/1/7 14:17
     * @Param
     **/
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90200_GetFireAlarmStatistics() throws Exception {
        // 获取当月接警数据分析
        webSocketVo.setFireAlarmStatisticsVoList(firealarmServices.getFireAlarmStatistics());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90200_GetFireAlarmList() throws Exception {
        // 获取接警事件记录
        webSocketVo.setFireAlarmList(firealarmServices.getFireAlarm());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetMandatoryInfo() throws Exception {
        // 获取培训教育强制的通过/未通过
        webSocketVo.setPassingRateList(startprogramServices.getMandatoryInfo());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetIsMandatoryInfo() throws Exception {
        // 获取培训教育非强制的通过/未通过
        webSocketVo.setPassingIsRateList(startprogramServices.getIsMandatoryInfo());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF10802_GetEmergencyplanInfo() throws Exception {
        // 应急预案列表
        webSocketVo.setEmergencyplanList(emergencyplanServices.list());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }
    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF10803_GetResponseinformationInfo() throws Exception {
        //应急预案工艺处置队列表
        webSocketVo.setResponseinformationList(responseinformationServices.list());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetTrainEducationPerInfo() throws Exception {
        // 获取培训教育人员详细
        webSocketVo.setTrainEducationPerList(startprogramServices.getTrainEducationPerInfo());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetOverduePersonnelList() throws Exception {
        //获取培训到期人员列表
        webSocketVo.setOverduePersonnelListVoList(trainjoinlistServices.overduepersonnellist());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90800_GetFutureProgram() throws Exception {
        //获取未来三个月培训信息
        webSocketVo.setFutureProgramList(startprogramServices.getFutureProgram());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }

    @Async
    @Scheduled(fixedDelay = 30000)
    public void BASF90900_GetRiskassessment() throws Exception {
        //获取风险判研信息
        System.out.println("执行 获取风险判研信息 定时任务: " + LocalDateTime.now().toLocalTime()
                + "\r\n线程 : " + Thread.currentThread().getName());
        webSocketVo.setRiskassessment(riskassessmentServices.getData());
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketVo)));
    }
    // endregion
}
