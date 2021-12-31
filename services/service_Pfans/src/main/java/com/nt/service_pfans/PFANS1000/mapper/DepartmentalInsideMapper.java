package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import com.nt.dao_Pfans.PFANS1000.DepartmentalInside;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartMonthPeo;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideBaseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.StaffWorkMonthInfoVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemeContract;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentalInsideMapper extends MyMapper<DepartmentalInside> {

    List<DepartmentalInsideBaseVo> getBaseInfo(@Param("YEAR") String year);

    List<StaffWorkMonthInfoVo> getWorkInfo(@Param("LOG_DATE") String log_date, @Param("departList") List<String> departList,@Param("projectList") List<String> projectList);

    //部门别表批量插入数据
    void insertDepInsAll(@Param("list") List<DepartmentalInside> departmentalInsideListInsert);
    //部门别表批量更新数据
    void updateDepInsAll(@Param("list") List<DepartmentalInside> departmentalInsideListUnpdate);

    List<ThemeContract> getContList(@Param("years") String years, @Param("yearmonth") String yearmonth, @Param("depart") String depart);

    List<ThemeContract> getContListAnt(@Param("years") String years, @Param("yearmonth") String yearmonth, @Param("depart") String depart);

    //共通工数
    List<DepartMonthPeo> getComm(@Param("depart") String depart,@Param("LOG_DATE") String LOG_DATE);

    //当月本部门社内人员总数（取自考勤管理）
    List<DepartMonthPeo> getTotalPeo(@Param("years") String years, @Param("departmental") String depart);

    ThemeContract sumContractIn(@Param("conber") String conber, @Param("years") String years, @Param("departmental") String depart);

    ThemeContract sumContractOut(@Param("conber") String conber, @Param("years") String years, @Param("departmental") String depart);
}
