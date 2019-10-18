package com.nt.service_Org.mapper;

import com.nt.dao_Org.Dictionary;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DictionaryMapper extends MyMapper<Dictionary> {

    List<Dictionary> getForSelect(@Param("code") String code);
}
