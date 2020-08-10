package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.PersonPlanTable;
import com.nt.dao_Pfans.PFANS1000.Vo.ActualPL;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BusinessplanMapper extends MyMapper<Businessplan> {
    @Select("select value2 as code,value3 as money46,value4 as money73,value5 as payhour,value6 as overtimehour from dictionary where value1 = #{groupid}")
    List<PersonPlanTable> selectPersonTable(@Param("groupid") String groupid);

    List<ActualPL> getAcutal(@Param("groupid") String groupid,@Param("startday") String startday,@Param("endday") String endday);

    //gbb add 0810 月度费用统计查询事业计划
    List<Businessplan> getBusinessplan(@Param("years") String years,@Param("groupIdList")List<String> groupIdList);
}
