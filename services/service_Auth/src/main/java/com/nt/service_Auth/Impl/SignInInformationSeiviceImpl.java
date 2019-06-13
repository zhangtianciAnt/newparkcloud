package com.nt.service_Auth.Impl;

import com.nt.dao_Auth.model.SignInInformation;
import com.nt.service_Auth.SignInInformationSeivice;
import com.nt.service_Auth.mapper.SignInInformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor=Exception.class)
public class SignInInformationSeiviceImpl implements SignInInformationSeivice {
    @Resource
    private SignInInformationMapper signInInformationMapper;
    @Override
    public void insert(SignInInformation signInInformation) throws Exception {

    }
}
