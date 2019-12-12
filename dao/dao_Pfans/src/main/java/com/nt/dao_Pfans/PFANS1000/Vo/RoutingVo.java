package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Routing;
import com.nt.dao_Pfans.PFANS1000.Routingdetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutingVo {

    private Routing routing;

    private List<Routingdetail> routingdetail;
}
