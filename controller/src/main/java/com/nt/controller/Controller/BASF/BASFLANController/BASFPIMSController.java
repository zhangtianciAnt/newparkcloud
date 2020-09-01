package com.nt.controller.Controller.BASF.BASFLANController;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketDeviceinfoVo;
import com.nt.dao_BASF.Pimsdata;
import com.nt.dao_BASF.Pimspoint;
import com.nt.service_BASF.mapper.PimsPointMapper;
import com.nt.service_BASF.mapper.PimsdataMapper;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Classname BASFTestController
 * @Description PIMS接口类
 * @Date 2020/8/31 14:32
 * @Author skaixx
 */
@RestController
@RequestMapping("/pims")
public class BASFPIMSController {

    @Autowired
    private PimsPointMapper pimsPointMapper;

    @Autowired
    private PimsdataMapper pimsdataMapper;

    private WebSocketDeviceinfoVo webSocketDeviceinfoVo = new WebSocketDeviceinfoVo();

    @RequestMapping(value = "/getData", method = {RequestMethod.POST})
    public ApiResult getData(@RequestBody List<Object> data, HttpServletRequest request) throws Exception {
        List<Pimsdata> pimsdataList = new ArrayList<>();
        for (Object info : data){
            String name = ((LinkedHashMap) info).get("key").toString();
            String value = ((LinkedHashMap) info).get("value").toString();
            // 通过name获取pimsponitname
            Pimspoint pimspoint = new Pimspoint();
            pimspoint.setPimspointname(name);
            pimspoint = pimsPointMapper.selectOne(pimspoint);
            //将Pims数据插入到Pimsdata
            Pimsdata pimsdata = new Pimsdata();
            pimsdata.setPimsid(UUID.randomUUID().toString());
            pimsdata.setMonitoringpoint(pimspoint.getId());
            pimsdata.setPimsdata(BigDecimal.valueOf(Long.parseLong(value)));
            pimsdata.preInsert();
            pimsdataList.add(pimsdata);
        }
        pimsdataMapper.insertPimsDataList(pimsdataList);

        // websocket推送数据到大屏
        webSocketDeviceinfoVo.setPimsdataList(pimsdataList);
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketDeviceinfoVo)));
        return ApiResult.success();
    }
}
