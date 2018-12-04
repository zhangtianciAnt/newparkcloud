package com.nt.service_Org;


import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.ToDoNotice.Notices;

import java.util.List;

public interface ToDoNoticeService {

    void save(ToDoNotice toDoNotice) throws Exception;

    //获取消息
    List<ToDoNotice> get(ToDoNotice toDoNotice) throws Exception;

    void updateNoticesStatus(ToDoNotice toDoNotice) throws Exception;
}
