package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.dao_Pfans.PFANS2000.Attendance;
import com.nt.dao_Pfans.PFANS2000.Vo.restViewVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnnualLeaveMapper extends MyMapper<AnnualLeave> {

    /**
     * 获取休假列表
     *
     * @return
     */
    List<AnnualLeave> getDataList(@Param("userid") String userid);

    List<restViewVo> getrest(@Param("userid") String userid);

   void updateAnnualYear(@Param("annualLeaves") List<AnnualLeave> annualLeaves);
}
