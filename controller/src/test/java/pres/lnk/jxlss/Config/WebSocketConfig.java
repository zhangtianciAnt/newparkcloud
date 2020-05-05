package pres.lnk.jxlss.Config;

import com.nt.utils.STOMPConnectEventListener;
import com.nt.utils.SocketSessionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
//注解开启使用STOMP协议来传输基于代理(message broker)的消息,这时控制器支持使用@MessageMapping,就像使用@RequestMapping一样
@EnableWebSocketMessageBroker

public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {//注册STOMP协议的节点(endpoint),并映射指定的url
        //注册一个STOMP的endpoint,并指定使用SockJS协议
        registry.addEndpoint("/endpointLogin").setAllowedOrigins("*").addInterceptors(new HandShkeInceptor()).withSockJS();
       registry.addEndpoint("/endpointMessage").setAllowedOrigins("*").addInterceptors(new HandShkeInceptor()).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代理(Message Broker)
        //广播式应配置一个/topic消息代理
        registry.enableSimpleBroker("/topicLogin","/topicMessage");
        registry.setUserDestinationPrefix("/user");
        registry.setApplicationDestinationPrefixes("/topicLogin");

//        registry.enableSimpleBroker("/topicMessage");
//        registry.setApplicationDestinationPrefixes("/topicMessage");
    }

    @Bean
    public SocketSessionRegistry SocketSessionRegistry(){
        return new SocketSessionRegistry();
    }
    @Bean
    public STOMPConnectEventListener STOMPConnectEventListener(){
        return new STOMPConnectEventListener();
    }
}
