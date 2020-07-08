package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS3000.Tickets;
import com.nt.dao_Pfans.PFANS3000.Ticketsdetails;
import com.nt.dao_Pfans.PFANS3000.Vo.TicketsVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface TicketsService {

    void insert(Tickets tickets, TokenModel tokenModel) throws Exception;

    List<Tickets> list(Tickets tickets) throws Exception;

    List<Ticketsdetails> geticketsdetail(Ticketsdetails ticketsdetails) throws Exception;

    void update(Tickets tickets, TokenModel tokenModel) throws Exception;

    Tickets One(String tickets_id) throws Exception;

    public void insert1(TicketsVo ticketsVo, TokenModel tokenModel) throws Exception;

    public void update1(TicketsVo ticketsVo, TokenModel tokenModel) throws Exception;

    public TicketsVo selectById(String ticket_id) throws Exception;
    //add-ws-7/7-禅道153
    List<Tickets> selectById2(String business_id) throws Exception;
    //add-ws-7/7-禅道153
}
