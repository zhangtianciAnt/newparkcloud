package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Wages;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface WagesMapper extends MyMapper<Wages> {

    List<Wages> selectWage();

    void updateWages(@Param("givingId") String givingId,@Param("user_id") String user_id);

    List<Wages> lastWages(@Param("year") int year, @Param("month") int month);

    List<String> lastMonthWage(@Param("year") int year, @Param("month") int month);

//    List<Wages> getWageList(Wages wages);

    List<Wages> getWagesByGivingId(@Param("givingId") String givingId);

    List<Wages> getWagesdepartment(@Param("dates") String givingId);

    List<Wages> getWagecompany();
}
