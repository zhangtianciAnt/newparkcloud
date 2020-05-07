package com.nt.service_pfans.PFANS1000;
import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.dao_Pfans.PFANS1000.CostCarryForward;
import com.nt.dao_Pfans.PFANS1000.Pltab;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

public interface PltabService {

    void inset(List<CostCarryForward> costcarryforward, TokenModel tokenModel) throws Exception;

    List<CostCarryForward> getCostList(String groupid, String year, String month)throws Exception;

    List<Pltab> selectPl(String groupid, String year, String month)throws Exception;
}
