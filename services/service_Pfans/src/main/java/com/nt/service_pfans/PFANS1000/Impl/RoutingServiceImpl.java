package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Routing;
import com.nt.service_pfans.PFANS1000.RoutingService;
import com.nt.service_pfans.PFANS1000.mapper.RoutingMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoutingServiceImpl implements RoutingService {

    @Autowired
    private RoutingMapper routingMapper;

    @Override
    public List<Routing> getRouting(Routing routing) {
        return routingMapper.select(routing);
    }

    @Override
    public Routing One(String routing_id) throws Exception {
        return routingMapper.selectByPrimaryKey(routing_id);
    }

    @Override
    public void updateRouting(Routing routing, TokenModel tokenModel) throws Exception {
        routing.preUpdate(tokenModel);
        routingMapper.updateByPrimaryKey(routing);
    }

    @Override
    public void insert(Routing routing, TokenModel tokenModel) throws Exception {
        routing.preInsert(tokenModel);
        routing.setRouting_id(UUID.randomUUID().toString());
        routingMapper.insert(routing);
    }

}
