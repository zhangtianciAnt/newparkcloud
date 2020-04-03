package com.nt.service_BASF.Impl;


import com.nt.dao_BASF.Alarmreceipt;
import com.nt.dao_BASF.VO.AlarmreceiptVo;
import com.nt.service_BASF.AlarmreceiptServices;
import com.nt.service_BASF.mapper.AlarmreceiptMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AlarmreceiptServicesImpl implements AlarmreceiptServices {

    private static Logger log = LoggerFactory.getLogger(AlarmreceiptServicesImpl.class);

    @Autowired
    private AlarmreceiptMapper alarmreceiptMapper;

    @Override
    public List<AlarmreceiptVo> getList(String alarmreceipttype) throws Exception {
        return alarmreceiptMapper.selectAlarmreceiptVoList(alarmreceipttype);
    }

    @Override
    public void insert(Alarmreceipt alarmreceipt, TokenModel tokenModel) throws Exception {
        Alarmreceipt alarmreceipt1 =new Alarmreceipt();
        String yyMMdd = new SimpleDateFormat("yyMMdd").format(new Date()).toString();
        int a = alarmreceiptMapper.selectCount(alarmreceipt1);
        String countno =new DecimalFormat("00").format(a+1);
        String typec = null;
        switch (alarmreceipt.getAlarmreceipttype()){
            case "BC003001": typec = "ENT";//环保检测
                break;
            case "BC003002": typec = "ELF";//电子围栏
                break;
        }
        alarmreceipt.setAlarmreceiptno(yyMMdd+typec+countno);

        alarmreceipt.preInsert(tokenModel);
        alarmreceipt.setAlarmreceiptid(UUID.randomUUID().toString());
        alarmreceiptMapper.insert(alarmreceipt);
    }

    @Override
    //
    public void update(Alarmreceipt alarmreceipt, TokenModel tokenModel) throws Exception {
        alarmreceipt.preUpdate(tokenModel);
        alarmreceiptMapper.updateByPrimaryKeySelective(alarmreceipt);
    }

    @Override
    public AlarmreceiptVo select(String alarmreceiptid) throws Exception {
        return alarmreceiptMapper.selectAlarmreceiptVo(alarmreceiptid);
    }
}