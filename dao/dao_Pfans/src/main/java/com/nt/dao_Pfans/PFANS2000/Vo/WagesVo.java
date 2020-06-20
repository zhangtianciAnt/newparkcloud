package com.nt.dao_Pfans.PFANS2000.Vo;

import com.nt.dao_Pfans.PFANS2000.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WagesVo {
    //预计
    private List<Wages> wagesListestimate;
    //实际
    private List<Wages> wagesListactual;
    //比较
    private List<Wages> wagesListdiff;
}
