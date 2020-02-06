package com.nt.controller.Controller.WebSocket;

import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * basf websocket 主类
 */
public class WebSocket implements WebSocketHandler {

    //在线用户列表
    private static final Map<String, Set<WebSocketSession>> users;

    static {
        users = new HashMap<>();
    }

    //建立新的socket连接后回调的方法
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        System.out.println("websocket 成功建立连接");
        String token = webSocketSession.getUri().toString().split("token=")[1];
        System.out.println(token);
        if (token != null && !"".equals(token)) {
            // 因为可以多点登录，所以要判断当前users集合里是否有这个token
            if (users.containsKey("token")) {
                users.get(token).add(webSocketSession);
            } else {
                Set<WebSocketSession> webSocketSessionSet = new HashSet<>();
                webSocketSessionSet.add(webSocketSession);
                users.put(token, webSocketSessionSet);
            }
            webSocketSession.sendMessage(new TextMessage("websocket 成功建立连接"));
        }
        System.out.println("当前在线人数：" + users.size());
    }

    //接收客户端发送的Socket信息
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        try {
            String message = (String) webSocketMessage.getPayload();
        } catch (Exception e) {
            System.out.println("接收客户端信息异常");
            e.printStackTrace();
        }
    }

    //连接出错时，回调的方法
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        System.out.println("websocket 连接出错");
        if (webSocketSession.isOpen())
            webSocketSession.close();
        users.remove(getClientToken(webSocketSession));
    }

    //连接关闭时，回调的方法
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("websocket 连接关闭:" + closeStatus);
        users.remove(getClientToken(webSocketSession));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 获取用户标识token
     */
    private String getClientToken(WebSocketSession session) {
        try {
            return (String) session.getAttributes().get("WEBSOCKET_TOKEN");
        } catch (Exception e) {
            System.out.println("websocket 获取用户标识token出错");
            return null;
        }
    }

    /**
     * 发送信息给指定token用户
     */
    public Boolean sendMessageToUser(String clientToken, TextMessage message) {
        if (users.get(clientToken) == null) return false;
        Set<WebSocketSession> webSocketSessionSet = users.get(clientToken);
        for (WebSocketSession ws : webSocketSessionSet) {
            if (ws.isOpen()) {
                try {
                    synchronized (ws) {
                        ws.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 发送信息给所有用户
     */
    public Boolean sendMessageToAll(TextMessage message) {
        Boolean sendState = true;
        Set<String> clientTokens = users.keySet();
        for (String token : clientTokens) {
            sendState = sendMessageToUser(token, message);
        }
        return sendState;
    }
}
