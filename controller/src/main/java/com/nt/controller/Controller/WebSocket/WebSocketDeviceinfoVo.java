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
public class WebSocketDeviceinfoVo {

    //大屏报警数据
    private List<Firealarm> topfirealarmList = new ArrayList<>();

    //接收机柜传过来的报警信息
    private List<DeviceinformationVo> deviceinformationList = new ArrayList<>();

}
