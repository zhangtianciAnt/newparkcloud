package com.nt.service_BASF.Impl;


import com.nt.dao_BASF.Chathistory;
import com.nt.dao_BASF.Webchatuserinfo;
import com.nt.service_BASF.WebchatuserinfoServices;
import com.nt.service_BASF.mapper.ChathistoryMapper;
import com.nt.service_BASF.mapper.WebchatuserinfoMapper;
import com.nt.utils.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class WebchatuserinfoServicesImpl implements WebchatuserinfoServices {

    private static Logger log = LoggerFactory.getLogger(WebchatuserinfoServicesImpl.class);

    @Autowired
    private WebchatuserinfoMapper webchatuserinfoMapper;

    @Autowired
    private ChathistoryMapper chathistoryMapper;

    @Override
    public ApiResult checkPwd(Webchatuserinfo webchatuserinfo) throws Exception {
        // 用户输入的密码
        String inputPwd = webchatuserinfo.getPassword();
        Webchatuserinfo userInfo = webchatuserinfoMapper.selectPwd(webchatuserinfo.getLoginid());
        if (inputPwd.equals(userInfo.getPassword())) {
            return ApiResult.success();
        } else {
            return ApiResult.fail();
        }
    }

    /**
     * @return com.nt.utils.ApiResult
     * @Method saveChatHistory
     * @Author SKAIXX
     * @Description 保存聊天记录
     * @Date 2021/1/6 10:18
     * @Param [chathistoryList]
     **/
    @Override
    public ApiResult saveChatHistory(Chathistory chathistory) throws Exception {
        chathistory.setId(UUID.randomUUID().toString());
        chathistory.preInsert();
        chathistoryMapper.insert(chathistory);
        return ApiResult.success();
    }

    /**
     * @return com.nt.utils.ApiResult
     * @Method getChatHistory
     * @Author SKAIXX
     * @Description 获取聊天记录
     * @Date 2021/1/6 10:19
     * @Param [from, to]
     **/
    @Override
    public ApiResult getChatHistory(String from, String to) throws Exception {
        Chathistory chathistory = new Chathistory();
        chathistory.setFrom(from);
        List<Chathistory> fromChathistoryList = chathistoryMapper.select(chathistory);
        chathistory.setFrom(null);
        chathistory.setTo(from);
        List<Chathistory> toChathistoryList = chathistoryMapper.select(chathistory);
        fromChathistoryList.addAll(toChathistoryList);
        return ApiResult.success(fromChathistoryList.stream().sorted(Comparator.comparing(Chathistory::getTime)).collect(Collectors.toList()));
    }
}
