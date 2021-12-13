package com.nt.service_Org.mapper;

import com.nt.dao_Org.PersonScale;
import com.nt.dao_Org.PersonScaleMee;
import com.nt.dao_Org.Vo.ScaleComproject;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.dao_Pfans.PFANS6000.EntrustSupport;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PersonScaleMapper extends MyMapper<PersonScale> {

    List<PersonScaleMee> getMeeInfo(String nowY_Month);

    List<PersonScale> getLogInfo(String nowY_Month);

    @Select("select my_WorkingDays(#{nowYmonth})")
    String getWorktimeger(@Param("nowYmonth") String nowYmonth);

    String getMangernum(@Param("peo_id") String peo_id, @Param("nowY_Month") String nowY_Month) ;

    String getPronum(@Param("peo_id") String peo_id, @Param("nowY_Month") String nowY_Month) ;

    void insetList(@Param("list") List<PersonScale> entrusts);

    void insetMeeList(@Param("list") List<PersonScaleMee> entrusts);

    @Select("SELECT PROJECT_ID,REPORTPEOPLE FROM personscale where REPORTERS = #{REPORTERS} and yearmonth = #{YEARMONTH} GROUP BY PROJECT_ID,REPORTPEOPLE ")
    List<ScaleComproject> getComprojects(@Param("REPORTERS") String reporters, @Param("YEARMONTH") String yearmonth);

    List<Projectsystem> getTypeTwo(@Param("contTime") String contTime);

    List<CompanyProjects> getProInfo();
}
