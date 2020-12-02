package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Trainmail;
import com.nt.dao_BASF.VO.TrainmailVo;
import com.nt.service_BASF.TrainmailServices;
import com.nt.service_BASF.mapper.TrainmailMapper;
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
public class TrainmailServicesImpl implements TrainmailServices {

    private static Logger log = LoggerFactory.getLogger(TrainmailServicesImpl.class);

    @Autowired
    private TrainmailMapper trainmailMapper;

    // 获取培训邮件设置内容
    @Override
    public List<TrainmailVo> getAllList() throws Exception {
        return trainmailMapper.getAllList();
    }

    @Override
    public TrainmailVo getone(String trainmailid) throws Exception {
        return trainmailMapper.getone(trainmailid);
    }

    // 更新培训邮件设置内容
    @Override
    public void update(Trainmail trainmail, TokenModel tokenModel) throws Exception {
        trainmail.preUpdate(tokenModel);
        trainmailMapper.updateByPrimaryKeySelective(trainmail);
    }

    @Override
    public void insert(Trainmail trainmail, TokenModel tokenModel) throws Exception {
        trainmail.setTrainmailid(UUID.randomUUID().toString());
        trainmail.preInsert(tokenModel);
        trainmail.preUpdate(tokenModel);
        trainmailMapper.insertSelective(trainmail);
    }
}
