package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OvertimeMapper extends MyMapper<Overtime> {
    List<Overtime> getOvertime();
    Overtime getOvertimeOne( String overtime_id);
    int insert(@Param("overtime") Overtime overtime);
    int updateByPrimaryKey(@Param("overtime") Overtime overtime);
}
