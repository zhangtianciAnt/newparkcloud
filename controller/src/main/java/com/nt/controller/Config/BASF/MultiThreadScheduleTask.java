package com.nt.controller.Config.BASF;

import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Controller.WebSocket.MessageVo;
import com.nt.controller.Controller.WebSocket.WebSocket;
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

    @Async
    @Scheduled(fixedDelay = 30000)
    public void selectUserCount() throws InterruptedException {
        System.out.println("执行 查询员工人数 定时任务: " + LocalDateTime.now().toLocalTime()
                + "\r\n线程 : " + Thread.currentThread().getName());
        // 查询数据库
        int i = basfUserInfoMapper.selectUserCount();
        // websocket消息推送
        WebSocket ws = new WebSocket();
        MessageVo messageVo = new MessageVo();
        messageVo.setUsersCount(i);
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(messageVo)));
    }
}
