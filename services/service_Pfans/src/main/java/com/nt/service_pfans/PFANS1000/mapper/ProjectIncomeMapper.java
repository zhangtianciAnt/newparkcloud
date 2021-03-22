package com.nt.service_pfans.PFANS1000.mapper;


import com.nt.dao_Pfans.PFANS1000.Evection;
import com.nt.dao_Pfans.PFANS1000.ProjectIncome;
import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProjectIncomeMapper extends MyMapper<ProjectIncome> {

    @Select("select *  from projectcontract where companyprojects_id = #{id}  and  date_format((deliveryfinshdate),'%Y') = #{year}  and  date_format((deliveryfinshdate),'%m') = #{month} ")
    List<ProjectContract> getprojectcontract(@Param("id") String id, @Param("year") String year, @Param("month") String month);

//    @Select("select * from publicexpense where GROUP_ID = #{groupid}  and  date_format((reimbursementdate),'%Y') = #{year}  and  date_format((reimbursementdate),'%m') = #{month} ")
//    List<PublicExpense> getpublicexpense(@Param("groupid") String groupid, @Param("year") String year, @Param("month") String month);
//
//    @Select("select * from evection where GROUP_ID = #{groupid}  and  date_format((reimbursementdate),'%Y') = #{year}  and  date_format((reimbursementdate),'%m') = #{month} ")
//    List<Evection> getevection(@Param("groupid") String groupid, @Param("year") String year, @Param("month") String month);

    @Select("select sum(TIME_START) as TIME_START from logmanagement where PROJECT_ID = #{projectid}  and  date_format((LOG_DATE),'%Y') = #{year}  and  date_format((LOG_DATE),'%m') = #{month} and CREATEBY = #{createby}  GROUP BY PROJECT_ID")
    List<LogManagement> getlogmanagement(@Param("projectid") String projectid, @Param("createby") String createby, @Param("year") String year, @Param("month") String month);

    @Select("select sum(TIME_START) as TIME_START from logmanagement l INNER JOIN expatriatesinfor e on l.CREATEBY = e.account where PROJECT_ID = #{projectid}  and  date_format((LOG_DATE),'%Y') = #{year}  and  date_format((LOG_DATE),'%m') = #{month} and e.EXPATRIATESINFOR_ID = #{createby}  GROUP BY PROJECT_ID")
    List<LogManagement> getlogmanagementBP(@Param("projectid") String projectid, @Param("createby") String createby, @Param("year") String year, @Param("month") String month);

}
