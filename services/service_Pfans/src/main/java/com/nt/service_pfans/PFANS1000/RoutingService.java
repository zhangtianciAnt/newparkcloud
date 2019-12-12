package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Routing;
import com.nt.dao_Pfans.PFANS1000.Vo.RoutingVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface RoutingService {

    List<Routing> getRouting(Routing routing)throws Exception;

    public RoutingVo selectById(String routing_id)throws  Exception;

    public void insert(RoutingVo routingVo, TokenModel tokenModel)throws  Exception;

    public void update(RoutingVo routingVo, TokenModel tokenModel)throws  Exception;
}
