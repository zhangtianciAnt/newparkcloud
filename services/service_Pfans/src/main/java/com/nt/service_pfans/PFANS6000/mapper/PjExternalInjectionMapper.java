package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.PjExternalInjection;
import com.nt.dao_Pfans.PFANS6000.Vo.PjExternalInjectionVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PjExternalInjectionMapper extends MyMapper<PjExternalInjection> {
    List<PjExternalInjectionVo> getThemeCompany(@Param("year") String year, @Param("department_id") String department_id);
    int updatepj(@Param("list") List<PjExternalInjection> updatepjExternal);
    int insertpj(@Param("list") List<PjExternalInjection> insertpjExternal);
}

