package com.nt.controller.Config.BASF;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketVo;
import com.nt.service_BASF.FirealarmServices;
import com.nt.service_BASF.VehicleinformationServices;
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

    // websocket消息推送
    private WebSocket ws = new WebSocket();
    // WebSocketVow
    private WebSocketVo webSocketVo = new WebSocketVo();

    @Async
    @Scheduled(fixedDelay = 30000)
    public void selectUserCount() throws Exception {
        System.out.println("执行 查询员工人数 定时任务: " + LocalDateTime.now().toLocalTime()
                + "\r\n线程 : " + Thread.currentThread().getName());
        // 查询数据库
        int i = basfUserInfoMapper.selectUserCount();
        webSocketVo.setUsersCount(i);
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
    // endregion
}
