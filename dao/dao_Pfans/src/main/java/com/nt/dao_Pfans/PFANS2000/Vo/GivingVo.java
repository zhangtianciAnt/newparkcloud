package com.nt.dao_Pfans.PFANS2000.Vo;

import com.nt.dao_Pfans.PFANS2000.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GivingVo {

    private Giving giving;

    private List<OtherTwo> otherTwo;

    private List<OtherFive> otherFive;

    private List<OtherFour> otherFour;

    private List<Appreciation> appreciation;

    private List<Base> base;

    private List<Contrast> contrast;

    private List<AccumulatedTaxVo> accumulatedTaxVo;

    private List<DisciplinaryVo> DisciplinaryVo;

    private List<DutyfreeVo> dutyfreeVo;
}
