package com.nt.utils;

import com.mysql.jdbc.StringUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.impl.TokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Set;

public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent>{
	@Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //login get from browser
        String agentId = sha.getNativeHeader("login").get(0);
        String Userid = "";
        if(!StringUtils.isNullOrEmpty(agentId)){
			TokenServiceImpl tokenService = new TokenServiceImpl();
			TokenModel tokenModel = tokenService.getToken(agentId);
			Userid = tokenModel.getUserId();
		}
        Set<String> sessionIds = webAgentSessionRegistry.getSessionIds(Userid);
        for(String sessionId:sessionIds){
        	webAgentSessionRegistry.unregisterSessionId(Userid, sessionId);
        }

        String sessionId = sha.getSessionId();
        webAgentSessionRegistry.registerSessionId(Userid,sessionId);
    }
}
