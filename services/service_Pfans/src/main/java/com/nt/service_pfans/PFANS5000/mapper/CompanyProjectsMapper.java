package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo2;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsVo3;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyProjectsMapper extends MyMapper<CompanyProjects> {

    List<CompanyProjectsVo2> getList(@Param("owners")List<String> owners);
    List<CompanyProjectsVo2> getList5(@Param("owners")List<String> owners);
    List<CompanyProjectsVo2> getList2(@Param("owners")List<String> owners);
    List<CompanyProjectsVo2> getList3(@Param("owners")List<String> owners);
    List<CompanyProjectsVo2> getListVo2();
    List<CompanyProjectsVo3> getCompanyProject(@Param("SyspName") String SyspName);
    List<CompanyProjectsVo2> getList4(@Param("user")String user);
}
