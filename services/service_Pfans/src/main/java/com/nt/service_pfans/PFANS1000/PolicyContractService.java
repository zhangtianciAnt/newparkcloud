package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.PolicyContract;

import com.nt.dao_Pfans.PFANS1000.Vo.PolicyContractVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PolicyContractService {

      List<PolicyContract> getPolicyContract(PolicyContract policycontract)throws Exception;

      public PolicyContractVo One(String policycontract_id)throws  Exception;

      public void insert(PolicyContractVo policycontractvo, TokenModel tokenModel)throws  Exception;

      public List<PolicyContract> check(PolicyContract policycontract, TokenModel tokenModel)throws  Exception;

      public void updatePolicyContract(PolicyContractVo policycontractvo, TokenModel tokenModel)throws  Exception;

}
