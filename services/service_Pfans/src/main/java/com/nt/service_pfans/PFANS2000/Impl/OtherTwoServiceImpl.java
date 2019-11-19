package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.OtherTwo;
import com.nt.service_pfans.PFANS2000.OtherTwoService;
import com.nt.service_pfans.PFANS2000.mapper.OtherTwoMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class OtherTwoServiceImpl implements OtherTwoService {

    @Autowired
    private OtherTwoMapper othertwoMapper;

    @Override
    public List<OtherTwo> list(OtherTwo othertwo) throws Exception {
        return othertwoMapper.select(othertwo);
    }

    @Override
    public void insert(OtherTwo othertwo, TokenModel tokenModel) throws Exception {
        othertwo.preInsert(tokenModel);
        othertwo.setOthertwo_id(UUID.randomUUID().toString());
        othertwoMapper.insert(othertwo);
    }


}
