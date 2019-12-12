package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Routing;
import com.nt.dao_Pfans.PFANS1000.Routingdetail;
import com.nt.dao_Pfans.PFANS1000.Vo.RoutingVo;
import com.nt.service_pfans.PFANS1000.RoutingService;
import com.nt.service_pfans.PFANS1000.mapper.RoutingMapper;
import com.nt.service_pfans.PFANS1000.mapper.RoutingdetailMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoutingServiceImpl implements RoutingService {

    @Autowired
    private RoutingMapper routingMapper;

    @Autowired
    private RoutingdetailMapper routingdetailMapper;

    @Override
    public List<Routing> getRouting(Routing routing)  throws Exception{
        return routingMapper.select(routing);
    }

    @Override
    public RoutingVo selectById(String routing_id) throws Exception {
        RoutingVo asseVo = new RoutingVo();
        Routingdetail routingdetail = new Routingdetail();
        routingdetail.setRouting_id(routing_id);
        List<Routingdetail> routingdetaillist = routingdetailMapper.select(routingdetail);
        routingdetaillist = routingdetaillist.stream().sorted(Comparator.comparing(Routingdetail::getRowindex)).collect(Collectors.toList());
        Routing secur = routingMapper.selectByPrimaryKey(routing_id);
        asseVo.setRouting(secur);
        asseVo.setRoutingdetail(routingdetaillist);
        return asseVo;
    }

    @Override
    public void update(RoutingVo routingVo, TokenModel tokenModel) throws Exception {
        Routing routing = new Routing();
        BeanUtils.copyProperties(routingVo.getRouting(), routing);
        routing.preUpdate(tokenModel);
        routingMapper.updateByPrimaryKey(routing);
        String routing_id = routing.getRouting_id();
        Routingdetail tail = new Routingdetail();
        tail.setRouting_id(routing_id);
        routingdetailMapper.delete(tail);
        List<Routingdetail> routingdetaillist = routingVo.getRoutingdetail();
        if (routingdetaillist != null) {
            int rowindex = 0;
            for (Routingdetail routingdetail : routingdetaillist) {
                rowindex = rowindex + 1;
                routingdetail.preInsert(tokenModel);
                routingdetail.setRoutingdetail_id(UUID.randomUUID().toString());
                routingdetail.setRouting_id(routing_id);
                routingdetail.setRowindex(rowindex);
                routingdetail.setSourceipaddress(routingdetail.getSourceipaddress());
                routingdetail.setDestinationipaddress(routingdetail.getDestinationipaddress());
                routingdetail.setProtocol(routingdetail.getProtocol());
                routingdetailMapper.insertSelective(routingdetail);
            }
        }
    }
    @Override
    public void insert(RoutingVo routingVo, TokenModel tokenModel) throws Exception {
        String routing_id = UUID.randomUUID().toString();
        Routing routing = new Routing();
        BeanUtils.copyProperties(routingVo.getRouting(), routing);
        routing.preInsert(tokenModel);
        routing.setRouting_id(routing_id);
        routingMapper.insertSelective(routing);
        List<Routingdetail> routingdetaillist = routingVo.getRoutingdetail();
        if (routingdetaillist != null) {
            int rowindex = 0;
            for (Routingdetail routingdetail : routingdetaillist) {
                rowindex = rowindex + 1;
                routingdetail.preInsert(tokenModel);
                routingdetail.setRoutingdetail_id(UUID.randomUUID().toString());
                routingdetail.setRouting_id(routing_id);
                routingdetail.setRowindex(rowindex);
                routingdetail.setSourceipaddress(routingdetail.getSourceipaddress());
                routingdetail.setDestinationipaddress(routingdetail.getDestinationipaddress());
                routingdetail.setProtocol(routingdetail.getProtocol());
                routingdetailMapper.insertSelective(routingdetail);
            }
        }
    }

}
