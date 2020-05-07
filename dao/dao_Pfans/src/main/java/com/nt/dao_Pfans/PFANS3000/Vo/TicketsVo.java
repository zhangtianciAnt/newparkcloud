package com.nt.dao_Pfans.PFANS3000.Vo;

import com.nt.dao_Pfans.PFANS3000.Tickets;
import com.nt.dao_Pfans.PFANS3000.Ticketsdetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketsVo {
    /**
     * 机票主表
     */
    private Tickets tickets;
    /**
     * 机票附表
     */
    private List<Ticketsdetails> ticketsdetails;

}
