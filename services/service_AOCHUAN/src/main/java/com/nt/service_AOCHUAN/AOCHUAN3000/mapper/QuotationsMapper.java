package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

public interface QuotationsMapper extends MyMapper<Quotations> {
    @Select("select count(*) from quotations")
     int select_Count();
}
