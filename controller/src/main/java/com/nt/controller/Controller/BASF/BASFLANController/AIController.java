package com.nt.controller.Controller.BASF.BASFLANController;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Config.BASF.MultiThreadScheduleTask;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.dao_BASF.AIRequest;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.ServerInfo;
import com.nt.dao_BASF.VehicleManagement;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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

    @Autowired
    private RestTemplate restTemplate;

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
        // 存放 linkagelist报警接口 数据
        List<ServerInfo> serverInfos = new ArrayList<>();
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
                // 添加报警数据
                ServerInfo serverInfo = new ServerInfo();
                serverInfo.setFactoryname(d.getFactoryname());
                serverInfo.setDevrow(d.getDevrow());
                serverInfo.setDevnum(d.getDetailedlocation());
                serverInfo.setEventname(d.getDevicename());
                serverInfos.add(serverInfo);
            } else {
                // ai消警
                d.setDevicestatus("BC011001");
            }
        }
        // 调用报警接口，按照火警流程执行
        if (serverInfos.size() > 0) {
            String url = "http://127.0.0.1:5556/BASF10105/linkagelist";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> formEntity = new HttpEntity<String>(JSONObject.toJSONString(serverInfos), headers);
            restTemplate.postForObject(url, formEntity, String.class);
        }
        int i = deviceinformationMapper.updateAiDeviceInfomation(deviceinformations);
        // 推送
        MultiThreadScheduleTask.webSocketVo.setAis(deviceinformations);
        WebSocket.sendMessageToAll(new TextMessage(JSONObject.toJSONString(MultiThreadScheduleTask.webSocketVo)));
        return ApiResult.success();
    }
}
