package com.nt.service_pfans.PFANS5000.Impl;


import com.nt.dao_Pfans.PFANS5000.Logmanagement;
import com.nt.service_pfans.PFANS5000.LogmanagementService;
import com.nt.service_pfans.PFANS5000.mapper.LogmanagementMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;


@Service
@Transactional(rollbackFor=Exception.class)
public class LogmanagementServiceImpl implements LogmanagementService {

    @Autowired
    private LogmanagementMapper logmanagementmapper;

    @Override
    public void insert(Logmanagement logmanagement, TokenModel tokenModel) throws Exception {
        logmanagement.preInsert(tokenModel);
        logmanagement.setLogmanagement_id(UUID.randomUUID().toString());
        logmanagementmapper.insert(logmanagement);
    }

    @Override
    public List<Logmanagement> getDataList() {
        Calendar cal = Calendar.getInstance();
        String this_year = String.valueOf(cal.get(cal.YEAR));
        String last_year = String.valueOf(cal.get(cal.YEAR) - 1);
        List<Logmanagement> lstData = logmanagementmapper.getDataList(this_year, last_year);
        if (lstData.isEmpty()) {
            return null;
        }
        return lstData;
    }
    }

