package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.User;
import com.nt.utils.MyMapper;
import org.springframework.stereotype.Component;

@Component(value="UserMapper")
public interface UserMapper extends MyMapper<User> {
}