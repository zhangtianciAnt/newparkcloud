package com.nt.service_AOCHUAN.AOCHUAN0000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN0000.WorkPlan;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface WorkPlanMapper extends MyMapper<WorkPlan> {

    int existCheck(@Param("id") String id) throws  Exception;

    void del(@Param("id") String id,@Param("modifyby") String modifyby) throws  Exception;
}
