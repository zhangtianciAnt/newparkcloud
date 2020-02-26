package com.nt.service_pfans.PFANS1000;
import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ContractService {
    List<Contract> get(Contract contract)throws Exception;

    public Contract One(String contract_id)throws  Exception;

    public void update(Contract contract, TokenModel tokenModel)throws  Exception;
}
