package com.nt.service_Org;


import com.nt.dao_Org.*;

public interface UserService {
    void save(Org org) throws Exception;

    void save(OrgTree orgTree) throws Exception;

    void save(UserAccount userAccount) throws Exception;

    void save(Tenant tenant) throws Exception;

    void save(CustomerInfo customerInfo) throws Exception;

    Org get(Org Org) throws Exception;
}
