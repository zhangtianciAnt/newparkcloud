package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.utils.MyMapper;

import java.util.List;

public interface AnnualLeaveMapper extends MyMapper<AnnualLeave> {

    /**
     * 获取休假列表
     *
     * @param annualLeave
     * @return
     */
    List<AnnualLeave> getDataList(AnnualLeave annualLeave);
}
