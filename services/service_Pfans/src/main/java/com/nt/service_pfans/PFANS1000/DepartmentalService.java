package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Departmental;

import java.util.List;

public interface DepartmentalService {

      void getExpatureList() throws Exception;

      List<Departmental> getDepartmental(String years, String group_id) throws Exception;

}
