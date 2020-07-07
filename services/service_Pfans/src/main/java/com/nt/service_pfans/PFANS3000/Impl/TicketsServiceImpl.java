package com.nt.service_pfans.PFANS3000.Impl;

import com.nt.dao_Pfans.PFANS3000.Tickets;
import com.nt.dao_Pfans.PFANS3000.Ticketsdetails;
import com.nt.dao_Pfans.PFANS3000.Vo.TicketsVo;
import com.nt.service_pfans.PFANS3000.TicketsService;
import com.nt.service_pfans.PFANS3000.mapper.TicketsMapper;
import com.nt.service_pfans.PFANS3000.mapper.TicketsdetailsMapper;
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
public class TicketsServiceImpl implements TicketsService {

    @Autowired
    private TicketsMapper ticketsMapper;
    @Autowired
    private TicketsdetailsMapper ticketsdetailsMapper;

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
//---wxl--获取导出明细数据
    @Override
    public List<Ticketsdetails> geticketsdetail(Ticketsdetails ticketsdetails) throws Exception {
        return ticketsdetailsMapper.select(ticketsdetails);
    }

    @Override
    public void update(Tickets tickets, TokenModel tokenModel) throws Exception {
        tickets.preUpdate(tokenModel);
        ticketsMapper.updateByPrimaryKey(tickets);
    }

    @Override
    public Tickets One(String tickets_id) throws Exception {
        return ticketsMapper.selectByPrimaryKey(tickets_id);
    }

    @Override
    public void insert1(TicketsVo ticketsVo, TokenModel tokenModel) throws Exception {
        String tickets_id = UUID.randomUUID().toString();
        Tickets tickets = new Tickets();
        BeanUtils.copyProperties(ticketsVo.getTickets(), tickets);
        tickets.preInsert(tokenModel);
        tickets.setTickets_id(tickets_id);
        ticketsMapper.insertSelective(tickets);
        List<Ticketsdetails> ticketsdetailsList = ticketsVo.getTicketsdetails();
        if (ticketsdetailsList != null) {
            int rowindex = 0;
            for (Ticketsdetails ticketsdetails : ticketsdetailsList) {
                rowindex = rowindex + 1;
                ticketsdetails.preInsert(tokenModel);
                ticketsdetails.setTicketsdetailid(UUID.randomUUID().toString());
                ticketsdetails.setTickets_id(tickets_id);
                ticketsdetails.setRowindex(rowindex);
                ticketsdetailsMapper.insertSelective(ticketsdetails);
            }
        }
    }

    @Override
    public void update1(TicketsVo ticketsVo, TokenModel tokenModel) throws Exception {
        Tickets tickets = new Tickets();
        BeanUtils.copyProperties(ticketsVo.getTickets(), tickets);
        tickets.preInsert(tokenModel);
        ticketsMapper.updateByPrimaryKey(tickets);
        String tickets_id = tickets.getTickets_id();
        Ticketsdetails ticketsdetails = new Ticketsdetails();
        ticketsdetails.setTickets_id(tickets_id);
        ticketsdetailsMapper.delete(ticketsdetails);
        List<Ticketsdetails> ticketsdetailsList = ticketsVo.getTicketsdetails();
        if (ticketsdetailsList != null) {
            int rowindex = 0;
            for (Ticketsdetails ticketsdetails1 : ticketsdetailsList) {
                ticketsdetails1.preInsert(tokenModel);
                ticketsdetails1.setTicketsdetailid(UUID.randomUUID().toString());
                ticketsdetails1.setTickets_id(tickets_id);
                ticketsdetails1.setRowindex(rowindex);
                ticketsdetailsMapper.insertSelective(ticketsdetails1);
            }
        }
    }

    @Override
    public TicketsVo selectById(String ticket_id) throws Exception {
        TicketsVo ticketsVo = new TicketsVo();
        Ticketsdetails ticketsdetails = new Ticketsdetails();
        ticketsdetails.setTickets_id(ticket_id);
        List<Ticketsdetails> ticketsdetailsList = ticketsdetailsMapper.select(ticketsdetails);
        ticketsdetailsList = ticketsdetailsList.stream().sorted(Comparator.comparing(Ticketsdetails::getRowindex)).collect(Collectors.toList());
        Tickets tickets = ticketsMapper.selectByPrimaryKey(ticket_id);
        ticketsVo.setTickets(tickets);
        ticketsVo.setTicketsdetails(ticketsdetailsList);
        return ticketsVo;
    }
}