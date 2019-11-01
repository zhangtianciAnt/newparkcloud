package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.Tickets;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface TicketsService {

    void insert(Tickets tickets, TokenModel tokenModel) throws Exception;

    List<Tickets> list(Tickets tickets) throws Exception;

    void update(Tickets tickets, TokenModel tokenModel) throws Exception;

    Tickets One(String tickets_id) throws Exception;
}
