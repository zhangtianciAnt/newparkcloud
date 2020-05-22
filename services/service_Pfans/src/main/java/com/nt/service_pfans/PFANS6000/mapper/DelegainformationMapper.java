package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DelegainformationMapper extends MyMapper<Delegainformation> {
   List<DelegainformationVo> getYears(@Param("year") String year,@Param("group_id") String group_id,
                                      @Param("april") int april,@Param("may") int may,
                                      @Param("june") int june,@Param("july") int july,
                                      @Param("august") int august,@Param("september") int september,
                                      @Param("october") int october,@Param("november") int november,
                                      @Param("december") int december,@Param("january") int january,
                                      @Param("february") int february,@Param("march") int march);

}
