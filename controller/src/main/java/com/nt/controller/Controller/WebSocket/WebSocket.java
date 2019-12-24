package com.nt.controller.Controller.WebSocket;

import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/BASFWebSocket/{token}")
public class WebSocket {
    private Logger logger = Logger.getLogger(WebSocket.class);
    //  用来记录当前在线连接数
    //  private static int onlineCount = 0;
    //  记录每个用户与终端的连接 Map<token,webSocket实例>
    private static Map<Long, Session> usersSocket = new HashMap<>();

    @OnOpen
    public void open(@PathParam("token") Long token, Session session) throws IOException {
        if (!usersSocket.containsKey(token)) {
            usersSocket.put(token, session);
        }
        logger.info("用户:" + token + ",已连接websocket");
        logger.info("当前在线用户数为：" + usersSocket.size());
    }

    @OnClose
    public void close(@PathParam("token") Long token) {
        if (usersSocket.size() > 0 && usersSocket.containsKey(token)) {
            usersSocket.remove(token);
        } else {
            logger.info("未发现用户：" + token);
        }
    }

    @OnMessage
    public void receiveMessage(@PathParam("token") Long token, Session session, String message) throws IOException {
        if (session == null) logger.debug("websocket session null");
        logger.debug("收到来自用户：" + token + "的消息：" + message);
    }

    @OnError
    public void error(Throwable throwable) {
        try {
            logger.error(throwable.getMessage());
            throw throwable;
        } catch (Throwable e) {
            logger.error("websocket,未知错误");
        }
    }
}
