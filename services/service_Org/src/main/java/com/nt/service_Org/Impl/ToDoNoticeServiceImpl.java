package com.nt.service_Org.Impl;

import com.nt.dao_Org.Log;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.mapper.TodoNoticeMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.SocketSessionRegistry;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
@Transactional(rollbackFor=Exception.class)
public class ToDoNoticeServiceImpl implements ToDoNoticeService {

    @Autowired
    private TodoNoticeMapper todoNoticeMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**session操作类*/
    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Autowired
    private TokenService tokenService;
    /**
     * @方法名：save
     * @描述：消息保存
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[toDoNotice]
     * @返回值：
     */
    @Override
    public void save(ToDoNotice toDoNotice) throws Exception {
        toDoNotice.setNoticeid(UUID.randomUUID().toString());
        todoNoticeMapper.insert(toDoNotice);
        if(webAgentSessionRegistry.getSessionIds(toDoNotice.getOwner()) != null &&
                webAgentSessionRegistry.getSessionIds(toDoNotice.getOwner()).stream().findFirst().isPresent()){
            String sessionId=webAgentSessionRegistry.getSessionIds(toDoNotice.getOwner()).stream().findFirst().get();

            ToDoNotice condition = new ToDoNotice();
            condition.setOwner(toDoNotice.getOwner());
            condition.setStatus(AuthConstants.TODO_STATUS_TODO);
            //condition.setType(toDoNotice.getType());
            List<ToDoNotice> list = todoNoticeMapper.select(condition);

            messagingTemplate.convertAndSendToUser(sessionId,"/topicMessage/subscribe",list,createHeaders(sessionId));
        }
    }

    /**
     * @方法名：get
     * @描述：获取消息列表
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[toDoNotice]
     * @返回值：toDoNotice
     */
    @Override
    public List<ToDoNotice> get(ToDoNotice toDoNotice) throws Exception {

        return todoNoticeMapper.select(toDoNotice);
    }

    /**
     * @方法名：updateNoticesStatus
     * @描述：更新已阅
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[toDoNotice]
     * @返回值：
     */
    @Override
    public void updateNoticesStatus(ToDoNotice toDoNotice) throws Exception {
        todoNoticeMapper.updateByPrimaryKeySelective(toDoNotice);

        if(webAgentSessionRegistry.getSessionIds(toDoNotice.getOwner()) != null &&
                webAgentSessionRegistry.getSessionIds(toDoNotice.getOwner()).stream().findFirst().isPresent()){
            String sessionId=webAgentSessionRegistry.getSessionIds(toDoNotice.getOwner()).stream().findFirst().get();

            ToDoNotice condition = new ToDoNotice();
            condition.setOwner(toDoNotice.getOwner());
            condition.setStatus(AuthConstants.TODO_STATUS_TODO);
            //condition.setType(toDoNotice.getType());
            List<ToDoNotice> list = todoNoticeMapper.select(condition);

            messagingTemplate.convertAndSendToUser(sessionId,"/topicMessage/subscribe",list,createHeaders(sessionId));
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
