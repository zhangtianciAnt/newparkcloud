package com.nt.newparkcloud.services_demo;

import com.nt.newparkcloud.dao.dao_demo.*;

import java.util.List;

public interface UserService {
    void save(Org org) throws Exception;

    void save(OrgTree orgTree) throws Exception;

    void save(UserAccount userAccount) throws Exception;

    void save(Tenant tenant) throws Exception;

    void save(CustomerInfo customerInfo) throws Exception;

    Org get(Org Org) throws Exception;
}
