package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Vo.UserVo;
import com.nt.dao_Pfans.PFANS2000.AnnualLeave;

import java.util.List;

/**
 *
 */
public interface AnnualLeaveService {

    /**
     * 获取休假列表
     *
     * @return
     * @throws Exception
     */
    List<AnnualLeave> getDataList() throws Exception;

    /**
     * 数据做成
     */
    void insertannualLeave(CustomerInfo customer) throws Exception;

     void insertNewAnnualRest(UserVo userVo,String id) throws Exception;
}
