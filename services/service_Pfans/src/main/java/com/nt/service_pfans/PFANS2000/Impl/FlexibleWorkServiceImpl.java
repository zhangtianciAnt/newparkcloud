package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.FlexibleWork;
import com.nt.service_pfans.PFANS2000.FlexibleWorkService;
import com.nt.service_pfans.PFANS2000.mapper.FlexibleWorkMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class FlexibleWorkServiceImpl implements FlexibleWorkService {

    @Autowired
    private FlexibleWorkMapper flexibleworkMapper;

    @Override
    public List<FlexibleWork> getFlexiblework(FlexibleWork flexiblework ) {
        return flexibleworkMapper.select(flexiblework);
    }
}
