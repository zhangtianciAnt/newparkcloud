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

    private List<Appreciation> appreciation;

    private List<Base> base;

    private List<Contrast> contrast;

    private List<DisciplinaryVo> DisciplinaryVo;
}
