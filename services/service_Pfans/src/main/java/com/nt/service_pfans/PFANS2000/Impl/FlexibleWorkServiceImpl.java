package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.FlexibleWork;
import com.nt.service_pfans.PFANS2000.FlexibleWorkService;
import com.nt.service_pfans.PFANS2000.mapper.FlexibleWorkMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class FlexibleWorkServiceImpl implements FlexibleWorkService {

   private FlexibleWorkMapper flexibleWorkMapper;

    @Override
    public List<FlexibleWork> getFlexibleWork(FlexibleWork flexibleWork) {

        return flexibleWorkMapper.select(flexibleWork);
    }

    public void insertFlexibleWork(FlexibleWork flexibleWork,TokenModel tokenModel)throws Exception{
        if (!flexibleWork.equals(null)){
            flexibleWork.preInsert(tokenModel);
            flexibleWorkMapper.insertSelective(flexibleWork);
        }
    }

    public void updateFlexibleWork(FlexibleWork flexibleWork,TokenModel tokenModel)throws Exception{
        if (flexibleWork.equals(null)){
            flexibleWork.preInsert(tokenModel);
            flexibleWorkMapper.insertSelective(flexibleWork);
        }
    }
}
