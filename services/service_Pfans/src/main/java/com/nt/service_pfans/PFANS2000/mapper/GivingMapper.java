package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Giving;
import com.nt.dao_Pfans.PFANS2000.Lackattendance;
import com.nt.dao_Pfans.PFANS2000.OtherTwo2;
import com.nt.dao_Pfans.PFANS2000.Residual;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface GivingMapper extends MyMapper<Giving> {

    List<OtherTwo2> selectOthertwo(@Param("givingid")String givingid);

    List<Lackattendance> selectLackattendance(@Param("givingid")String giving_id);

    List<Residual> selectResidual(@Param("givingid")String giving_id);

}
