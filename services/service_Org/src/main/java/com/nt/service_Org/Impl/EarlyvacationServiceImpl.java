package com.nt.service_Org.Impl;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.Earlyvacation;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.EarlyvacationService;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_Org.mapper.EarlyvacationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class EarlyvacationServiceImpl implements EarlyvacationService {

    @Autowired
    private EarlyvacationMapper earlyvacationMapper;


    @Override
    public void insert(Earlyvacation earlyvacation, TokenModel tokenModel) throws Exception {

        //earlyvacationMapper.delete(earlyvacation);//earlyvacation_id
        earlyvacationMapper.deleteByPrimaryKey(earlyvacation.getEarlyvacation_id());
        earlyvacation.preInsert(tokenModel);
        earlyvacation.setEarlyvacation_id(UUID.randomUUID().toString());
        earlyvacationMapper.insert(earlyvacation);

    }
}

