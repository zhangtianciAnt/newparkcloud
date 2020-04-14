package com.nt.dao_Pfans.PFANS6000.Vo;

import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.PricesetGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricesetVo {
    private PricesetGroup main;
    private List<Priceset> detail;


}
