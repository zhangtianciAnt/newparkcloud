package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ContractVo extends Contract {

    private Contract contract;
    List<Contractnumbercount> numberCount;
}
