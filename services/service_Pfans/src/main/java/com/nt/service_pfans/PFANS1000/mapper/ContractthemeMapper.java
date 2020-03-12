package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Contracttheme;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ContractthemeMapper extends MyMapper<Contracttheme> {

    List<Contracttheme> getContractthemeList(@Param("type") String type,@Param("years") String years);

}
