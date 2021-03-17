package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Disciplinary;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DisciplinaryMapper extends MyMapper<Disciplinary> {
    List<Disciplinary> getdisciplinary(@Param("givingid") String givingid, @Param("strMonths") String strMonths);
}
