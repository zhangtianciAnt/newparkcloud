package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS1000.Ruling;
import com.nt.dao_Pfans.PFANS6000.EntrustSupport;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EntrustSupportMapper extends MyMapper<EntrustSupport> {

    void insetList(@Param("list") List<EntrustSupport> entrusts);

    void updateList(@Param("list") List<EntrustSupport> entrusts);

}
