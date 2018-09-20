package com.nt.newparkcloud.services_demo;

import com.nt.newparkcloud.dao.dao_demo.User;

import java.util.List;

public interface UserService {
    void save(User user) throws Exception;

    List<User> get(User user) throws Exception;
}
