package com.nt.dao_Pfans.PFANS2000.Vo;

import com.nt.dao_Pfans.PFANS2000.Appreciation;
import com.nt.dao_Pfans.PFANS2000.Giving;
import com.nt.dao_Pfans.PFANS2000.OtherFive;
import com.nt.dao_Pfans.PFANS2000.OtherTwo;
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
}
