package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Vo.PjExternalInjectionVo;

import java.util.List;


public interface PjExternalInjectionService {

    void saveTableinfo() throws Exception;

    List<PjExternalInjectionVo> getTableinfo(String year, String group_id) throws Exception;
}
