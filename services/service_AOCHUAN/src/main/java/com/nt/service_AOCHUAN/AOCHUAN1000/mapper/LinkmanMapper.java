package com.nt.service_AOCHUAN.AOCHUAN1000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Linkman;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LinkmanMapper extends MyMapper<Linkman> {
    @Select("select LINKMAN_ID,BASEINFOR_ID,NAME,SEX,FIXEDTELEPHONE,MOBILEPHONE,POSITION,EMAIL from linkman WHERE BASEINFOR_ID = #{baseinfor_id} order by createon")
    List<Linkman> selectBybaseinfor_id(@Param("baseinfor_id") String baseinfor_id);
}
