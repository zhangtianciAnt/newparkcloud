package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.AnnualLeave;

import java.util.List;

/**
 *
 */
public interface AnnualLeaveService {

    //获取休假列表
    List<AnnualLeave> getDataList(AnnualLeave annualLeave) throws Exception;
}
