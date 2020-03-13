package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Bonussend;
import org.apache.ibatis.annotations.Param;
import com.nt.utils.MyMapper;
import java.util.List;


public interface BonussendMapper extends MyMapper<Bonussend>{


    List<Bonussend> getBonussendList(@Param("years") String years);
}
