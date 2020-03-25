package com.nt.service_pfans.PFANS1000;
import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.dao_Pfans.PFANS1000.Pltab;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PltabService {

    List<Pltab> selectPl(String groupid,String year,String month)throws Exception;


}
