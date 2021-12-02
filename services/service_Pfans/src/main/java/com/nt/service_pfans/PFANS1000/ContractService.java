package com.nt.service_pfans.PFANS1000;
import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.dao_Pfans.PFANS1000.Vo.ContractVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ContractService {
    List<Contract> get(Contract contract)throws Exception;

    //  add  ml  211130  分页  from
    List<Contract> getPage(Contract contract)throws Exception;
    //  add  ml  211130  分页  to

    public ContractVo One(String contract_id)throws  Exception;

    public void update(Contract contract, TokenModel tokenModel)throws  Exception;

}
