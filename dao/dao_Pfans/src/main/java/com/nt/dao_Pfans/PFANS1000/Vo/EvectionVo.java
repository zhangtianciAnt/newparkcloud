package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Evection;
//import com.nt.dao_Pfans.PFANS1000.HotelExpense;
import com.nt.dao_Pfans.PFANS1000.OtherDetails;
import com.nt.dao_Pfans.PFANS1000.TrafficDetails;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
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
    //private List<HotelExpense> hotelexpense;
    /**
     * 其他费用明细
     */
    private List<OtherDetails> otherdetails;


}