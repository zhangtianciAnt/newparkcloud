package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Org.CustomerInfo;

import java.util.List;

public interface PersonnelplanService {
    List<CustomerInfo> SelectCustomer(String id);
}
