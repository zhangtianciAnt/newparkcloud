package com.nt.service_AOCHUAN.AOCHUAN7000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HelpruleMapper  extends MyMapper<Helprule> {


    void delCrerule(@Param("helprule_id") String helprule_id);
}
