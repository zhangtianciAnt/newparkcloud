package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.AnnualLeave;
import com.nt.dao_Pfans.PFANS3000.Tickets;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 *
 */
public interface AnnualLeaveService {

    /**
     * 获取休假列表
     *
     * @param annualLeave
     * @return
     * @throws Exception
     */
    List<AnnualLeave> getDataList(AnnualLeave annualLeave) throws Exception;

    /**
     * 数据做成
     */
    void insert(TokenModel tokenModel) throws Exception;
}
