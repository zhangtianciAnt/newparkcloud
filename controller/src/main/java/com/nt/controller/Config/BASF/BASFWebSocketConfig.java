package com.nt.controller.Config.BASF;

import com.nt.controller.Controller.WebSocket.WebSocket;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * basf websocket 配置
 */

@Configuration
//开启注解接收和发送消息
@EnableWebSocket
public class BASFWebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //handler是webSocket的核心，配置webSocket入口
        registry.addHandler(myHandler(), "basfWebSocket/app")
                // 允许跨域
                .setAllowedOrigins("*")
                // websock拦截器
                .addInterceptors(new WebSocketInterceptor())
        ;
    }

    public WebSocketHandler myHandler() {
        return new WebSocket();
    }

}

