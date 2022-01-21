package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS2000.AbNormal;
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
//    PJ起案 现场管理添加筛选条件 ztc fr
//    List<CompanyProjectsVo2> getList5(@Param("owners")List<String> owners);
    List<CompanyProjectsVo2> getList5(CompanyProjectsVo2 companyProjects);
//    PJ起案 现场管理添加筛选条件 ztc to
    List<CompanyProjectsVo2> getList2(@Param("owners")List<String> owners);

    List<CompanyProjectsVo2> getListPL2(@Param("user") String user);

    List<CompanyProjectsVo2> getListPL3(@Param("user") String user);
    List<CompanyProjectsVo2> getList3(@Param("owners")List<String> owners);
    List<CompanyProjectsVo2> getListVo2();
    List<CompanyProjectsVo3> getCompanyProject(@Param("SyspName") String SyspName);
    List<CompanyProjectsVo2> getList4(@Param("user")String user);

//    PJ起案 现场管理添加筛选条件 ztc fr
//    List<CompanyProjectsVo2> getListPL4(@Param("user") String user);
    List<CompanyProjectsVo2> getListPL4(CompanyProjectsVo2 companyProjects);
//    PJ起案 现场管理添加筛选条件 ztc to
    //zy start 报表追加 2021/06/13
    // 获取报表信息及实际出勤时间
    List<CompanyProjectsReport> getRepotBaseData(Map<String, String> params);

    // 获取项目每个月经费
    List<Monthly> getMoneysByProject(@Param("companyprojectsId") String companyprojectsId);
    //zy end 报表追加 2021/06/13
    List<Contractapplication> selectCont(@Param("contractNo") String contractNo,
                                         @Param("centerId") String centerId,
                                         @Param("groupId") String groupId);
//    PJ起案 现场管理添加筛选条件 ztc fr
    List<CompanyProjects> selectList(CompanyProjects companyprojects);
//    PJ起案 现场管理添加筛选条件 ztc to
}
