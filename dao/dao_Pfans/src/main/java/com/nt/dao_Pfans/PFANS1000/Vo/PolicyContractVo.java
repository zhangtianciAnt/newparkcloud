package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyContractVo {

    /*
     * 合同
     * */
    private PolicyContract policycontract;

    /*
     * 觉书*/
    private List<PolicyContractDetails> policycontractdetails;

}
