package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS1000.Contractcompound;
import com.nt.dao_Pfans.PFANS1000.Contractnumbercount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractapplicationVo {
    /**
     * 契约番号申请
     */
    private List<Contractapplication> contractapplication;

    /**
     * 契约番号回数
     */
    private List<Contractnumbercount> contractnumbercount;

    /**
     * 复合合同金额分配
     */
    private List<Contractcompound> contractcompound;
}
