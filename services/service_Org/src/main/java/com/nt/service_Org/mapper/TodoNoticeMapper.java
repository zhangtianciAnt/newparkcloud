package com.nt.service_Org.mapper;

import com.nt.dao_Org.ToDoNotice;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface TodoNoticeMapper extends MyMapper<ToDoNotice> {

    List<ToDoNotice> getDataList(@Param("STATUS") String STATUS);

}
