package com.nt.service_AOCHUAN.AOCHUAN7000.mapper;



import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.All;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AllMapper extends MyMapper<All> {
    List<All> selectrule(@Param("docurule_id") String docurule_id);
}
