package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnnualLeaveMapper {
    List<AnnualLeave> getDataList(@Param("THIS_YEAR") String this_year, @Param("LAST_YEAR") String last_year);
}
