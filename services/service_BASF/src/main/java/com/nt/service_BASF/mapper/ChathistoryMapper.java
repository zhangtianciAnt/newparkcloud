package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Chathistory;
import com.nt.utils.MyMapper;

import java.util.List;

public interface ChathistoryMapper extends MyMapper<Chathistory> {

    int insertChathistoryList(List<Chathistory> chathistoryList) throws Exception;
}