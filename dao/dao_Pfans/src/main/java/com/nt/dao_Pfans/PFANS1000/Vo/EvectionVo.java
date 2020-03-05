package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvectionVo {
    /**
     * 日本出張のマンション予約
     */
    private Evection evection;
    /**
     * 交通费明细
     */
    private List<TrafficDetails> trafficdetails;
    /**
     * 住宿费明细
     */
    private List<AccommodationDetails> accommodationdetails;
    /**
     * 其他费用明细
     */
    private List<OtherDetails> otherdetails;

    private List<Invoice> invoice;
}