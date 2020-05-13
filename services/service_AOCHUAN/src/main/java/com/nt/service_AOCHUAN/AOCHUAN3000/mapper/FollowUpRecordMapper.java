package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.ProAndFoll;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowUpRecordMapper extends MyMapper<FollowUpRecord> {
    //获取-记录列表
    public List<FollowUpRecord> getFollowUpRecordList(@Param("products_id") String products_id, @Param("supplier_id") String supplier_id);
    //删除-记录表
    void deleteFromFollowUpRecordByDoubleKey(@Param("modifyby") String modifyby,@Param("products_id") String products_id, @Param("supplier_id") String supplier_id,@Param("status") String status);
    //存在Check
    public List<FollowUpRecord> existCheck(@Param("id") String id,@Param("status") String status);

    public List<ProAndFoll> getForSupplier(@Param("id") String id);

    public List<ProAndFoll> getForCustomer(@Param("id") String id);
}
