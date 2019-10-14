package com.nt.service_pfans.PFANS5000.Impl;

import com.nt.dao_Pfans.PFANS5000.Logmanagement;
import com.nt.service_pfans.PFANS5000.LogmanagementService;
import com.nt.service_pfans.PFANS5000.mapper.LogmanagementMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@Transactional(rollbackFor=Exception.class)
public class LogmanagementServiceImpl implements LogmanagementService {

    @Autowired
    private LogmanagementMapper logmanagementmapper;

    @Override
    public void insert(Logmanagement logmanagement, TokenModel tokenModel) throws Exception {
        logmanagement.preInsert(tokenModel);
        logmanagement.setLogmanagementid(UUID.randomUUID().toString());
        logmanagementmapper.insert(logmanagement);
    }
    }

