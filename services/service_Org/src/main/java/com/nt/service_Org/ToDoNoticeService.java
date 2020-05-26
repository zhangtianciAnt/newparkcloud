package com.nt.service_Org;


import com.nt.dao_Org.ToDoNotice;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ToDoNoticeService {



    List<ToDoNotice> list(ToDoNotice todonotice) throws Exception;

    List<ToDoNotice> getDataList(String status)throws Exception;
    //保存消息
    void save(ToDoNotice toDoNotice) throws Exception;

    //获取消息
    List<ToDoNotice> get(ToDoNotice toDoNotice) throws Exception;

    //更新已阅
    void updateNoticesStatus(ToDoNotice toDoNotice) throws Exception;

    //    ADD_FJL_05/25  -- 删除驳回之后无用代办
    void delToDoNotice(String todonoticeid, TokenModel tokenModel) throws Exception;
    //    ADD_FJL_05/25  -- 删除驳回之后无用代办
}
