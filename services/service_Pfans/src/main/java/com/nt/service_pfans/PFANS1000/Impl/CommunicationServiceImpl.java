package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Communication;
import com.nt.service_pfans.PFANS1000.CommunicationService;
import com.nt.service_pfans.PFANS1000.mapper.CommunicationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommunicationServiceImpl implements CommunicationService {

    @Autowired
    private CommunicationMapper communicationMapper;

    @Override
    public List<Communication> selectCommunication() throws Exception {
        return communicationMapper.selectCommunication();
    }
    @Override
    public List<Communication> getCommunication(Communication communication) {

        return communicationMapper.select(communication);
    }

    @Override
    public Communication One(String communication_id) throws Exception {

        return communicationMapper.selectByPrimaryKey(communication_id);
    }

    @Override
    public void updateCommunication(Communication communication, TokenModel tokenModel) throws Exception {
        communication.preUpdate(tokenModel);
        communicationMapper.updateByPrimaryKey(communication);
    }

    @Override
    public void insert(Communication communication, TokenModel tokenModel) throws Exception {

        communication.preInsert(tokenModel);
        communication.setCommunication_id(UUID.randomUUID().toString()) ;
        communicationMapper.insert(communication);
    }
}
