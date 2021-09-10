package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideReturnVo;
import com.nt.dao_Pfans.PFANS6000.Vo.PjExternalInjectionVo;

import java.util.List;

public interface DepartmentalInsideService {
    public void insert() throws Exception;

    List<DepartmentalInsideReturnVo> getTableinfo(String year, String group_id) throws Exception;

    //region scc add 21/8/31 部门项目报表 from
    Object getTableinfoReport(String year, String group_id) throws Exception;
    //endregion scc add 21/8/31 部门项目报表 to
}
