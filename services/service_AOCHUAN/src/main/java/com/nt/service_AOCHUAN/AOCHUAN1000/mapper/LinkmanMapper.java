package com.nt.service_AOCHUAN.AOCHUAN1000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Linkman;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LinkmanMapper extends MyMapper<Linkman> {
    @Select("select * from linkman WHERE BASEINFOR_ID = #{baseinfor_id} order by createon")
    List<Linkman> selectBybaseinfor_id(@Param("baseinfor_id") String baseinfor_id);

    @Select("delete from linkman WHERE BASEINFOR_ID = #{baseinfor_id}")
    List<Linkman> deleteByByBaseinforId(@Param("baseinfor_id") String baseinfor_id);
}
