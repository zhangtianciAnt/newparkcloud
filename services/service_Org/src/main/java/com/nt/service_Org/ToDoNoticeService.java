package com.nt.service_Org;


import com.nt.dao_Org.ToDoNotice;

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
}
