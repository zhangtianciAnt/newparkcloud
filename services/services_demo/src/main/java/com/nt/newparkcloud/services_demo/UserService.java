package com.nt.newparkcloud.services_demo;

import com.nt.newparkcloud.dao.dao_demo.Org;
import com.nt.newparkcloud.dao.dao_demo.OrgTree;
import com.nt.newparkcloud.dao.dao_demo.User;
import com.nt.newparkcloud.dao.dao_demo.UserAccount;

import java.util.List;

public interface UserService {
    void save(Org org) throws Exception;

    void save(OrgTree orgTree) throws Exception;

    void save(UserAccount userAccount) throws Exception;

    Org get(Org Org) throws Exception;
}
