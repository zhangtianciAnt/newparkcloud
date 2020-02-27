package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;

import java.util.List;

public interface PersonnelplanService {
    List<CustomerInfo> SelectCustomer(String id);
    List<Supplierinfor> getExternal();
    List<Expatriatesinfor> getExpatriatesinfor();
    List<PersonnelPlan> getAll();
    PersonnelPlan getOne(String id);
}
