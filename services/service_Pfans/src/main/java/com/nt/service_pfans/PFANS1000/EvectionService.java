package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Evection;
import com.nt.dao_Pfans.PFANS1000.TravelCost;
import com.nt.dao_Pfans.PFANS1000.Vo.EvectionVo;
import com.nt.dao_Pfans.PFANS1000.Vo.TravelCostVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface EvectionService {

    List<Evection> get(Evection evection) throws Exception;

    List<TravelCost> gettravelcost(TravelCostVo travelcostvo) throws Exception;
     Map<String, Object> exportjs(String evectionid, HttpServletRequest request) throws Exception;
     EvectionVo selectById(String evectionid) throws Exception;

    void insertEvectionVo(EvectionVo evectionvo, TokenModel tokenModel)throws Exception;

    void updateEvectionVo(EvectionVo evectionvo, TokenModel tokenModel)throws Exception;

}
