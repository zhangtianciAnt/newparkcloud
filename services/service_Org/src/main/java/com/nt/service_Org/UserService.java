package com.nt.service_Org;


import com.nt.dao_Org.*;

import java.util.List;

public interface UserService {
    void save(User user) throws Exception;

    List<User> get(User user) throws Exception;

    void up(User user) throws Exception;
}
