package com.nt.controller.Controller.WebSocket;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class WebSocket implements WebSocketHandler {

    //在线用户列表
    private static final Map<String, WebSocketSession> users;

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
            users.put(token, webSocketSession);
            webSocketSession.sendMessage(new TextMessage("websocket 成功建立连接"));
        }
        System.out.println("当前在线人数：" + users.size());
    }

    //接收客户端发送的Socket信息
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

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
     * 发送信息给指定用户
     */
    public Boolean sendMessageToUser(String clientToken, TextMessage message) {
        if (users.get(clientToken) == null) return false;
        WebSocketSession webSocketSession = users.get(clientToken);
        System.out.println("sendMessage:" + webSocketSession);
        return true;
    }

    /**
     * 发送信息给指定用户
     */
    public Boolean sendMessageToAll(TextMessage message) {
        return true;
    }
}
