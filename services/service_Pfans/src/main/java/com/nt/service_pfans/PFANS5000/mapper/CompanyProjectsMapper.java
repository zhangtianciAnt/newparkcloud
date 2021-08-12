package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsReport;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo2;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo3;
import com.nt.dao_Pfans.PFANS5000.Vo.Monthly;
import com.nt.utils.MyMapper;
import org.apache.commons.collections.KeyValue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CompanyProjectsMapper extends MyMapper<CompanyProjects> {

    List<CompanyProjectsVo2> getList(@Param("owners")List<String> owners);
    List<CompanyProjectsVo2> getList5(@Param("owners")List<String> owners);
    List<CompanyProjectsVo2> getList2(@Param("owners")List<String> owners);

    List<CompanyProjectsVo2> getListPL2(@Param("user") String user);

    List<CompanyProjectsVo2> getListPL3(@Param("user") String user);
    List<CompanyProjectsVo2> getList3(@Param("owners")List<String> owners);
    List<CompanyProjectsVo2> getListVo2();
    List<CompanyProjectsVo3> getCompanyProject(@Param("SyspName") String SyspName);
    List<CompanyProjectsVo2> getList4(@Param("user")String user);

    List<CompanyProjectsVo2> getListPL4(@Param("user") String user);
    //zy start 报表追加 2021/06/13
    // 获取报表信息及实际出勤时间
    List<CompanyProjectsReport> getRepotBaseData(Map<String, String> params);

    // 获取项目每个月经费
    List<Monthly> getMoneysByProject(@Param("companyprojectsId") String companyprojectsId);
    //zy end 报表追加 2021/06/13

    @Select("SELECT COMPANYPROJECTS_ID FROM `companyprojects` WHERE STARTDATE < '2021-04-01' and ENDDATE > '2021-04-01'")
    List<String> getComprojectIdZ();

    @Select("SELECT COMPANYPROJECTS_ID from projectsystem pro where pro.COMPANYPROJECTS_ID in " +
            "( SELECT COMPANYPROJECTS_ID FROM `companyprojects` WHERE ENDDATE <'2021-04-01' ) and pro.EXITTIME > '2021-03-31'")
    List<String> getComprojectIdO();

    @Select("SELECT value1 from dictionary where code = #{code}")
    String getDicInfo(@Param("code") String code);
}
