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

    private List<OtherOne> otherOne;

    private List<OtherTwo> otherTwo;

    private List<OtherFive> otherFive;

    private List<OtherFour> otherFour;

    private List<Appreciation> appreciation;

    private List<Lackattendance> lackattendance;

    private List<Residual> residual;

    private List<Base> base;

    private List<Contrast> contrast;

    private List<AccumulatedTaxVo> accumulatedTaxVo;

    private List<DisciplinaryVo> DisciplinaryVo;

    private List<DutyfreeVo> dutyfreeVo;

    private List<ComprehensiveVo> comprehensiveVo;

    private List<Additional> addiTional;

    // 2020/03/11 add by myt start
    private List<Induction> entryVo;

    private List<Retire> retireVo;

    private String yearOfLastMonth;

    private String monthOfLastMonth;

    private String yearOfThisMonth;

    private String monthOfThisMonth;
    // 2020/03/11 add by myt end

    private String strFlg;
}
