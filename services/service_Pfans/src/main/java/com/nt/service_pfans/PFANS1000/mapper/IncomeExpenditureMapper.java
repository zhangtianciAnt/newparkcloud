package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.IncomeExpenditure;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IncomeExpenditureMapper extends MyMapper<IncomeExpenditure> {

    @Select("select ifnull(sum(contractamount),0) AS contractamount  from projectcontract where theme = #{theme}  and  date_format((deliveryfinshdate),'%Y') = #{year}  and  date_format((deliveryfinshdate),'%m') = #{month} ")
    List<ProjectContract> getprojectcontractlist(@Param("theme") String theme, @Param("year") String year, @Param("month") String month);
}
