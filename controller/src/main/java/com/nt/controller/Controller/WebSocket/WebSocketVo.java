package com.nt.controller.Controller.WebSocket;

import com.nt.dao_BASF.*;
import com.nt.dao_BASF.VO.*;
import com.nt.dao_SQL.SqlAPBCardHolder;
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

    // BASF90500 人员清点模块 各装置人数集合
    private List<Integer> deviceUsersCountList = new ArrayList<>();

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

    //培训教育 培训强制通过率
    private List<PassingRateVo> passingRateList = new ArrayList<>();
    //培训教育 培训非强制通过率
    private List<PassingRateVo> passingIsRateList = new ArrayList<>();
    //接收机柜传过来的报警信息
    private List<DeviceinformationVo> deviceinformationList = new ArrayList<>();
    //应急预案列表
    private List<Emergencyplan> emergencyplanList = new ArrayList<>();
    //响应信息列表
    private List<Responseinformation> responseinformationList = new ArrayList<>();
    //在厂人员列表
    private List<SqlAPBCardHolder> selectapbcardList = new ArrayList<>();

    //获取培训教育人员详细
    private List<TrainEducationPerVo> trainEducationPerList = new ArrayList<>();
    //获取培训即将到期人员列表
    private List<OverduePersonnelListVo> overduePersonnelListVoList = new ArrayList<>();
    //获取未来三个月培训信息
    private List<Startprogram> futureProgramList = new ArrayList<>();
    //获取风险判研信息
    private Riskassessment riskassessment = new Riskassessment();
    // endregion
}
