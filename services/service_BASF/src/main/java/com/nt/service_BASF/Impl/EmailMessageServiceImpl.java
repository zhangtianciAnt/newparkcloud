package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Emailmessage;
import com.nt.service_BASF.EmailMessageService;
import com.nt.service_BASF.mapper.EmailmessageMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmailMessageServiceImpl implements EmailMessageService {

    @Autowired
    private EmailmessageMapper emailmessageMapper;

    @Override
    public List<Emailmessage> get(Emailmessage emailmessage) throws Exception {
        return emailmessageMapper.select(emailmessage);
    }

    @Override
    public void insert(TokenModel tokenModel, Emailmessage emailmessage) throws Exception {
        emailmessage.preInsert(tokenModel);
        emailmessage.setEmailmesageid(UUID.randomUUID().toString());
        emailmessageMapper.insert(emailmessage);
    }

    @Override
    public void update(TokenModel tokenModel, Emailmessage emailmessage) throws Exception {
        emailmessage.preUpdate(tokenModel);
        emailmessageMapper.updateByPrimaryKey(emailmessage);
    }

    @Override
    public void del(TokenModel tokenModel, Emailmessage emailmessage) throws Exception {
        emailmessage.preUpdate(tokenModel);
        emailmessageMapper.updateByPrimaryKeySelective(emailmessage);
    }
}
