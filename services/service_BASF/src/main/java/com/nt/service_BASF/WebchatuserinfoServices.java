package com.nt.service_BASF;

import com.nt.dao_BASF.Chathistory;
import com.nt.dao_BASF.Webchatuserinfo;
import com.nt.utils.ApiResult;

import java.util.List;


public interface WebchatuserinfoServices {

    //获取报警单信息
    ApiResult checkPwd(Webchatuserinfo webchatuserinfo) throws Exception;

    // 保存聊天记录
    ApiResult saveChatHistory(Chathistory chathistory) throws Exception;

    // 获取聊天记录
    ApiResult getChatHistory(String from, String to) throws Exception;
}
