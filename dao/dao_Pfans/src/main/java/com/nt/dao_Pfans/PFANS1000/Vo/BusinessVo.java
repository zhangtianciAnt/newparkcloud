package com.nt.dao_Pfans.PFANS1000.Vo;


import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS1000.TravelContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessVo {
    /**
     * 出张申请
     */
    private Business business;
    /**
     * 出差内容
     */
    private List<TravelContent> travelcontent;


}