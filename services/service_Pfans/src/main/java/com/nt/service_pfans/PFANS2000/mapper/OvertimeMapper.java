package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;

import java.util.List;

public interface OvertimeMapper extends MyMapper<Overtime> {
    List<Overtime> getOvertimeDay(@Param("overtimet") String overtimet,@Param("reserdate") String reserdate,@Param("userid") String userid);
    List<Overtime> getOvertimeOne(@Param("reserdate") String reserdate,@Param("userid") String userid);

}
