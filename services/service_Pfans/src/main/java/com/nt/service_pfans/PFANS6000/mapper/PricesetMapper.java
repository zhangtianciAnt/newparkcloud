package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.ExpatriatesinforDetail;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PricesetMapper extends MyMapper<Priceset> {
    List<Priceset> selectByYear(@Param("year") String year);
    List<Priceset> gettlist();
    List<Priceset> selectBygroupid(@Param("years") int years,@Param("groupid") String groupid);

    @Select("SELECT a.* FROM (SELECT p.* FROM priceset p WHERE PRICESETGROUP_ID = ( SELECT PRICESETGROUP_ID FROM pricesetgroup WHERE Pd_date = #{Pd_date} ) ) a " +
            "INNER JOIN ( SELECT EXPATRIATESINFOR_ID, WHETHERENTRY FROM expatriatesinfor WHERE EXITS = '1' and GROUP_ID = #{groupid}) b  ON a.USER_ID = b.EXPATRIATESINFOR_ID ")
    List<Priceset> selectThismonth(@Param("Pd_date") String Pd_date,@Param("groupid") String groupid);

    @Select("SELECT EXPATRIATESINFOR_ID, WHETHERENTRY FROM expatriatesinfor WHERE EXITS = '1' and GROUP_ID = #{groupid} and  EXPATRIATESINFOR_ID not in (SELECT p.user_id FROM priceset p WHERE PRICESETGROUP_ID = ( SELECT PRICESETGROUP_ID FROM pricesetgroup WHERE Pd_date =#{Pd_date} )) ")
    List<String> selectBpeople(@Param("Pd_date") String Pd_date,@Param("groupid") String groupid);

}
