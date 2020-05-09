package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThemePlanDetailVo {
    /**
     * theme detail
     */
    private ThemePlanDetail themeplandetail;

    /**
     * theme plan detail list
     */
    private List<ThemePlanDetail> children;
}
