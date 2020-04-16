package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowUpRecordMapper extends MyMapper<FollowUpRecord> {
    //获取-记录列表
    public List<FollowUpRecord> getFollowUpRecordList(@Param("product_nm") String product_nm, @Param("provider") String provider,@Param("status") String status);
    //删除-记录表
    void deleteFromFollowUpRecordByDoubleKey(@Param("modifyby") String modifyby,@Param("product_nm") String product_nm, @Param("provider") String provider,@Param("status") String status);
    //存在Check
    public List<FollowUpRecord> existCheck(@Param("id") String id,@Param("status") String status);
}
