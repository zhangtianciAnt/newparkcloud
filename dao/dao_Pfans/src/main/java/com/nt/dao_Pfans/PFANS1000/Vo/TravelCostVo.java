package com.nt.dao_Pfans.PFANS1000.Vo;


import com.nt.dao_Pfans.PFANS1000.TravelCost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelCostVo {


    private List<TravelCost> travelcost;
}
