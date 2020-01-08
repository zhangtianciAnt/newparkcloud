package com.nt.controller.Controller.WebSocket;

import com.nt.dao_BASF.VO.*;
import com.nt.dao_BASF.Vehicleinformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.controller.Controller.WebSocket.WebSocketVo
 * @ClassName: WebSocketVo
 * @Author: QYZ
 * @Description: ERC大屏WebSocket通信用Vo
 * @Date: 2019/12/30 13:40
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketVo {

    // 在厂总人数
    private Integer allUsersCount;

    // 员工人数
    private Integer usersCount;

    // 承包商人数
    private Integer contractorsCount;

    // 访客人数
    private Integer visitorsCount;

    // region BASF90600 ERC大屏-车辆定位模块
    // BASF90600 车辆定位模块 在场车辆信息一览Vo
    private List<InsideVehicleinformationVo> insideVehicleinformationVoList = new ArrayList<>();

    // BASF90600 车辆定位模块 车辆出入统计Vo
    private List<VehicleAccessStatisticsVo> vehicleAccessStatisticsVoList = new ArrayList<>();

    // BASF90600 车辆定位模块 当日车辆入场信息Vo
    private List<Vehicleinformation> vehicleinformationList = new ArrayList<>();

    // BASF90600 车辆定位模块 在场车辆类别统计Vo
    private List<InsideVehicleTypeVo> insideVehicleTypeVoList = new ArrayList<>();
    // endregion

    // region BASF90200 火灾消防模块
    // BASF90200 火灾消防模块 当月接警数据分析Vo
    private List<FireAlarmStatisticsVo> fireAlarmStatisticsVoList = new ArrayList<>();

    // BASF90200 火灾消防模块 接警时间记录
    private List<FireAlarmVo> fireAlarmList = new ArrayList<>();
    // endregion
}
