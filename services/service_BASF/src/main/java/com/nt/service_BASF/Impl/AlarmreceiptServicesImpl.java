package com.nt.service_BASF.Impl;


import com.nt.dao_BASF.Alarmreceipt;
import com.nt.service_BASF.AlarmreceiptServices;
import com.nt.service_BASF.mapper.AlarmreceiptMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AlarmreceiptServicesImpl implements AlarmreceiptServices {

    private static Logger log = LoggerFactory.getLogger(AlarmreceiptServicesImpl.class);

    @Autowired
    private AlarmreceiptMapper alarmreceiptMapper;

    @Override
    public List<Alarmreceipt> getList(Alarmreceipt alarmreceipt) throws Exception {
        return alarmreceiptMapper.select(alarmreceipt);
    }

    @Override
    public void insert(Alarmreceipt alarmreceipt, TokenModel tokenModel) throws Exception {
        alarmreceipt.preInsert(tokenModel);
        alarmreceipt.setAlarmreceiptid(UUID.randomUUID().toString());
        alarmreceiptMapper.insert(alarmreceipt);
    }

    @Override
    public Alarmreceipt select(String alarmreceiptid) throws Exception {
        return alarmreceiptMapper.selectByPrimaryKey(alarmreceiptid);
    }
}
