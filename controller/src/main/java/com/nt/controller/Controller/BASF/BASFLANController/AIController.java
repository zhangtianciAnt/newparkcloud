package com.nt.controller.Controller.BASF.BASFLANController;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Config.BASF.MultiThreadScheduleTask;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.dao_BASF.AIRequest;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
import com.nt.utils.ApiResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/AI")
public class AIController {

    @Resource
    private DeviceinformationMapper deviceinformationMapper;

    /**
     * ai摄像头报警，消警接口
     *
     * @param aiRequests
     * @return
     */
    @RequestMapping(value = "/sendAIMessage", method = {RequestMethod.POST})
    public ApiResult sendAIMessage(@RequestBody List<AIRequest> aiRequests) {
        Map<String, String> ais = new HashMap<>();
        List<Deviceinformation> deviceinformations = new ArrayList<>();
        Deviceinformation deviceinformation = null;
        for (AIRequest aiRequest : aiRequests) {
            String[] temp = aiRequest.getCamera().split("\\.");
            String no = temp[temp.length - 2] + temp[temp.length - 1];
            ais.put(no, aiRequest.getType());
            deviceinformation = new Deviceinformation();
            deviceinformation.setDeviceno(no);
            deviceinformations.add(deviceinformation);
        }
        // 根据IP地址查询摄像头信息
        deviceinformations = deviceinformationMapper.getAiDeviceInfomation(deviceinformations);
        // 更新状态
        for (Deviceinformation d : deviceinformations) {
            if ("1".equals(ais.get(d.getDeviceno()))) {
                // ai报警
                d.setDevicestatus("BC011005");
            } else {
                // ai消警
                d.setDevicestatus("BC011001");
            }
        }
        int i = deviceinformationMapper.updateAiDeviceInfomation(deviceinformations);
        // 推送
        MultiThreadScheduleTask.webSocketVo.setAis(deviceinformations);
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        return ApiResult.success();
    }
}
