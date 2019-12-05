package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Routing;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface RoutingService {

    List<Routing> getRouting(Routing routing)throws Exception;

    public Routing One(String routing_id)throws  Exception;

    public void insert(Routing routing, TokenModel tokenModel)throws  Exception;

    public void updateRouting(Routing routing, TokenModel tokenModel)throws  Exception;
}
