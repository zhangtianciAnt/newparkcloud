package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardVo {

    private Award award;

    private ArrayList groupN;

    private List<AwardDetail> awardDetail;

    private List<StaffDetail> staffDetail;

    private List<Contractnumbercount> numbercounts;
//    PSDCD_PFANS_20210525_XQ_054 复合合同决裁书分配金额可修改 ztc fr
    private List<AwardReunite> awardReunites;
//    PSDCD_PFANS_20210525_XQ_054 复合合同决裁书分配金额可修改 ztc to
    //region scc add 10/18 多部门审批 from
    private List<Contractcompound> contractcompound;
    //endregion scc add 10/18 多部门审批 from
}
