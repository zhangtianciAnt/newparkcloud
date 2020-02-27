package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS1000.Vo.ExternalVo;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PersonnelplanService {
    List<CustomerInfo> SelectCustomer(String id);
    List<Supplierinfor> getExternal();
    List<ExternalVo> getExpatriatesinfor();
    List<PersonnelPlan> getAll();
    PersonnelPlan getOne(String id);
    void update(PersonnelPlan personnelPlan, TokenModel tokenModel);
    void insert(PersonnelPlan personnelPlan, TokenModel tokenModel);
}
