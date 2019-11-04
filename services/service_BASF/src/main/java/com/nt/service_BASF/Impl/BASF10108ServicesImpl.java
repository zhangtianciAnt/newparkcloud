package com.nt.service_BASF.Impl;


import com.nt.dao_BASF.Alarmreceipt;
import com.nt.service_BASF.BASF10108Services;
import com.nt.service_BASF.mapper.AlarmreceiptMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BASF10108ServicesImpl implements BASF10108Services {

    private static Logger log = LoggerFactory.getLogger(BASF10108ServicesImpl.class);

    @Autowired
    private AlarmreceiptMapper alarmreceiptMapper;

    @Override
    public List<Alarmreceipt> getList(Alarmreceipt alarmreceipt) throws Exception {
        return alarmreceiptMapper.select(alarmreceipt);
    }
}
