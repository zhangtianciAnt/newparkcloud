package com.nt.controller.Controller.WebSocket;

import com.nt.dao_BASF.VO.InsideVehicleinformationVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // BASF90600 车辆定位模块 在场车辆信息Vo
    private InsideVehicleinformationVo insideVehicleinformationVo;
}
