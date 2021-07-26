package com.nt.dao_Pfans.PFANS1000;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Moneyavg {

    private static final long serialVersionUID = 1L;
    private String summerplanpc;
    private String winterplanpc;
    //update gbb 20210415 事业计划-外驻计划-新建时统计值用【Unitprice】 start
    private String unitprice;
    //update gbb 20210415 事业计划-外驻计划-新建时统计值用【Unitprice】 end
}
