package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Emailmessage;
import com.nt.service_BASF.BASF10106Service;
import com.nt.service_BASF.mapper.EmailmessageMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BASF10106ServiceImpl implements BASF10106Service {

    @Autowired
    private EmailmessageMapper emailmessageMapper;

    @Override
    public List<Emailmessage> get(Emailmessage emailmessage) throws Exception {
        return emailmessageMapper.select(emailmessage);
    }

    @Override
    public int insert(TokenModel tokenModel, Emailmessage emailmessage) throws Exception {
        emailmessage.preInsert(tokenModel);
        emailmessage.setEmailmesageid(UUID.randomUUID().toString());
        return emailmessageMapper.insert(emailmessage);
    }

    @Override
    public int update(TokenModel tokenModel, Emailmessage emailmessage) throws Exception {
        emailmessage.preUpdate(tokenModel);
        return emailmessageMapper.updateByPrimaryKeySelective(emailmessage);
    }
}
