package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Departmental;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalVo;

import java.util.List;

public interface DepartmentalService {

      void getExpatureList() throws Exception;

      List<DepartmentalVo> getDepartmental(String years, String group_id) throws Exception;

      Object getTable1050infoReport(String year, String group_id) throws Exception;

}
