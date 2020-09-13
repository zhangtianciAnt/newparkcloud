package com.nt.controller.Controller.WebSocket;

import com.nt.dao_BASF.*;
import com.nt.dao_BASF.VO.*;
import com.nt.dao_SQL.APBCardHolderVo;
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
    private List<APBCardHolderVo> usersCount;

    // 承包商人数
    private List<APBCardHolderVo> contractorsCount;

    // 访客人数
    private List<APBCardHolderVo> visitorsCount;

    // BASF90500 人员清点模块 各装置人数集合
//    private List<SqlAPBCardHolder> deviceUsersCountList = new ArrayList<>();
    private APBCardHolder apbCardHolder;

    // region BASF90600 ERC大屏-车辆定位模块
    // BASF90600 车辆定位-车辆查询
    private List<InsideVehicleinformationVo> insideVehicleinformationVoList = new ArrayList<>();

    // BASF90600 车辆定位-本月车辆概况
    private List<VehicleAccessStatisticsVo> vehicleAccessStatisticsVoList = new ArrayList<>();

    // BASF90600 车辆定位-本周车辆概况
    private List<VehicleAccessStatisticsVo> vehicleWeekAccessStatisticsVoList = new ArrayList<>();

    // BASF90600 车辆定位-实时车辆清单
    private List<Vehicleinformation> vehicleinformationList = new ArrayList<>();

    // BASF90600 车辆定位-在厂车辆
    private List<InsideVehicleTypeVo> insideVehicleTypeVoList = new ArrayList<>();

    // BASF90600 车辆定位-Gis地图上显示实时所有在场车辆位置
    private List<VehicleinformationGpsArrVo> QueryVehiclesRegularlyInfoList = new ArrayList<>();
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
//    private List<DeviceinformationVo> deviceinformationList = new ArrayList<>();

    //紧急集合点
    private List<DeviceAndSqlUserinfoVo> deviceAndSqlUserinfoVoList = new ArrayList<>();

    //大屏报警数据
//    private List<Firealarm> topfirealarmList = new ArrayList<>();

    //应急预案列表
    private List<Emergencyplan> emergencyplanList = new ArrayList<>();
    //化学品SDS列表
    private List<Chemicalsds> chemicalsdsList = new ArrayList<>();
    //响应信息列表
    private List<Responseinformation> responseinformationList = new ArrayList<>();
    //在厂人员列表
    private List<APBCardHolderVo> selectapbcardList = new ArrayList<>();

    //获取培训即将到期人员列表
    private List<OverduePersonnelListVo> overduePersonnelListVoList = new ArrayList<>();
    //大屏培训信息推送列表
    private List<Startprogram> futureProgramList = new ArrayList<>();

    //获取风险研判信息（MongoDB）
    private Riskassessment riskassessment = new Riskassessment();
    ///获取风险研判信息（MySql）
    private List<Riskassessments> riskassessmentsList = new ArrayList<>();

    // BASF90905 今日事件列表
    private List<FireAlarmVo> sameDayFireAlarm = new ArrayList<>();
    // BASF90906 本周事件列表
    private List<FireAlarmVo> weekFireAlarm = new ArrayList<>();

    // BASF90921 高风险作业清单
    private List<Highriskarea> highriskareaList = new ArrayList<>();
    //获取危化品车辆列表
    private List<VehicleinformationVo> dangerousgoodsList = new ArrayList<>();
    //获取危化品车辆数量
    private List<Vehicleinformation> countdangerousgoods;
    // endregion

    //道路占用/临时封闭区域列表
    private List<Application> roadClosed = new ArrayList<>();

    //
    private List<Switchnotifications> switchList = new ArrayList<>();

    private List<MhInfo> carSet;

    //大屏报警数据
    private List<Firealarm> topfirealarmList = new ArrayList<>();

    //接收机柜传过来的报警信息
    private List<DeviceinformationVo> deviceinformationList = new ArrayList<>();

    // 电子围栏报警
    private Deviceinformation electricShield;

    // PimsData
    private List<PimsVo> pimsVoList;

    // pims 正常数据推送
    private List<Pimspoint> pimspoints;

    //pims 报警状态发生改变推送
    private Pimsalarm pimsalarm;

}
