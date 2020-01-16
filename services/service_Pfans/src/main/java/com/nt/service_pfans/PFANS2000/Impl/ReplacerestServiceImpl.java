package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Replacerest;
import com.nt.service_pfans.PFANS2000.ReplacerestService;
import com.nt.service_pfans.PFANS2000.mapper.ReplacerestMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class ReplacerestServiceImpl implements ReplacerestService {

    @Autowired
    private ReplacerestMapper replacerestMapper;


    @Override
    public List<Replacerest> getReplacerest(Replacerest replacerest) throws Exception {
        return replacerestMapper.select(replacerest);
    }

}
