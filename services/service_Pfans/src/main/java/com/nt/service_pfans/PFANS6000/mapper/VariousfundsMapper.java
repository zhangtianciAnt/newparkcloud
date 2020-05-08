package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.Variousfunds;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VariousfundsMapper extends MyMapper<Variousfunds> {
    List<Variousfunds> selectBygroupid(@Param("groupid") String groupid);
}
