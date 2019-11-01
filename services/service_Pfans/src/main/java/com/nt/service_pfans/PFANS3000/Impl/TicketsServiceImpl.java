package com.nt.service_pfans.PFANS3000.Impl;

import com.nt.dao_Pfans.PFANS3000.Tickets;
import com.nt.service_pfans.PFANS3000.TicketsService;
import com.nt.service_pfans.PFANS3000.mapper.TicketsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class TicketsServiceImpl implements TicketsService {

    @Autowired
    private TicketsMapper ticketsMapper;

    @Override
    public void insert(Tickets tickets, TokenModel tokenModel) throws Exception {
        tickets.preInsert(tokenModel);
        tickets.setTickets_id(UUID.randomUUID().toString());
        ticketsMapper.insert(tickets);
    }

    @Override
    public List<Tickets> list(Tickets tickets) throws Exception {
        return ticketsMapper.select(tickets);
    }

    @Override
    public void update(Tickets tickets, TokenModel tokenModel) throws Exception {
        tickets.preUpdate(tokenModel);
        ticketsMapper.updateByPrimaryKeySelective(tickets);
    }

    @Override
    public Tickets One(String tickets_id) throws Exception {
        return ticketsMapper.selectByPrimaryKey(tickets_id);
    }

}
