package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.ThemePlan;
import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThemePlanVo {
    /**
     * theme plan
     */
    private List<ThemePlan> themePlans;

    /**
     * theme plan detail
     */
    private List<ThemePlanDetail> themePlanDetails;
}
