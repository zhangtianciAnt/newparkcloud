package com.nt.service_Org.mapper;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.ToDoNotice;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DictionaryMapper extends MyMapper<ToDoNotice> {

    List<Dictionary> getForSelect(@Param("scode") String scode, @Param("ecode")String ecode) throws Exception;
}
