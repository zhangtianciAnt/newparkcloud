package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Flexiblework;
import com.nt.service_pfans.PFANS2000.FlexibleworkService;
import com.nt.service_pfans.PFANS2000.mapper.FlexibleworkMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public abstract class FlexibleworkServiceImpl implements FlexibleworkService {

    @Autowired
    private FlexibleworkMapper flexibleworkMapper;

    @Override
    public List<Flexiblework> getFlexiblework() {
        if(flexibleworkMapper.getFlexiblework().isEmpty()){
            return null;
        }
        return flexibleworkMapper.getFlexiblework();
    }
}
