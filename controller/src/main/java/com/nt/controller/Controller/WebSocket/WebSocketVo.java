package com.nt.controller.Controller.WebSocket;

import com.nt.dao_BASF.VO.InsideVehicleTypeVo;
import com.nt.dao_BASF.VO.InsideVehicleinformationVo;
import com.nt.dao_BASF.VO.VehicleAccessStatisticsVo;
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
    // 员工人数
    private Integer usersCount;

    // BASF90600 车辆定位模块 在场车辆信息一览Vo
    private List<InsideVehicleinformationVo> insideVehicleinformationVoList = new ArrayList<>();

    // BASF90600 车辆定位模块 车辆出入统计Vo
    private List<VehicleAccessStatisticsVo> vehicleAccessStatisticsVoList = new ArrayList<>();

    // BASF90600 车辆定位模块 当日车辆入场信息Vo
    private List<Vehicleinformation> vehicleinformationList = new ArrayList<>();

    // BASF90600 车辆定位模块 在场车辆类别统计Vo
    private List<InsideVehicleTypeVo> insideVehicleTypeVoList = new ArrayList<>();
}
