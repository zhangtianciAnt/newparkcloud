package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Ruling;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RulingMapper extends MyMapper<Ruling> {

    void insetList(@Param("list") List<Ruling> rulings);

}
