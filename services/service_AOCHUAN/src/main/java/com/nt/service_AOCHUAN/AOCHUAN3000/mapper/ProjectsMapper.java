package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectsMapper extends MyMapper<Projects> {
    //获取记录列表
    public List<FollowUpRecord> getFollowUpRecordList(@Param("product_nm") String product_nm, @Param("provider") String provider);

    //删除
    void deleteByDoubleKey(@Param("table_nm") String table_nm,@Param("product_nm") String product_nm,@Param("provider") String provider);
}

