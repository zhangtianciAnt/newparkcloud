package com.nt.service_pfans.PFANS5000.Impl;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.service_pfans.PFANS5000.LogManagementService;
import com.nt.service_pfans.PFANS5000.mapper.LogManagementMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;


@Service
@Transactional(rollbackFor=Exception.class)
public class LogManagementServiceImpl implements LogManagementService {

    @Autowired
    private LogManagementMapper logmanagementmapper;

    @Override
    public void insert(LogManagement logmanagement, TokenModel tokenModel) throws Exception {
        logmanagement.preInsert(tokenModel);
        logmanagement.setLogmanagement_id(UUID.randomUUID().toString());
        logmanagementmapper.insert(logmanagement);
    }

    @Override
    public List<LogManagement> getDataList() {
        Calendar cal = Calendar.getInstance();
        String this_year = String.valueOf(cal.get(cal.YEAR));
        String last_year = String.valueOf(cal.get(cal.YEAR) - 1);
        List<LogManagement> lstData = logmanagementmapper.getDataList(this_year, last_year);
        if (lstData.isEmpty()) {
            return null;
        }
        return lstData;
    }
    @Override
    public void update(LogManagement logmanagement, TokenModel tokenModel) throws Exception{
            logmanagement.preUpdate(tokenModel);
            logmanagementmapper.updateByPrimaryKeySelective(logmanagement);
    }
    @Override
    public LogManagement One(String logmanagement_id) throws Exception {
        LogManagement log   =logmanagementmapper.selectByPrimaryKey(logmanagement_id);
        return log;
    }
}

