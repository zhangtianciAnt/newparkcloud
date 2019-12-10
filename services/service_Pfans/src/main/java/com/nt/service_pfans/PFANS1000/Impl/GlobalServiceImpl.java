package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Global;
import com.nt.service_pfans.PFANS1000.GlobalService;
import com.nt.service_pfans.PFANS1000.mapper.GlobalMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class GlobalServiceImpl implements GlobalService {

    @Autowired
    private GlobalMapper globalMapper;


    @Override
    public List<Global> getglobal(Global global) throws Exception {
        return globalMapper.select(global);
    }

    @Override
    public Global getglobalApplyOne(String global_id) throws Exception {
        return globalMapper.selectByPrimaryKey(global_id);
    }

    @Override
    public void updateglobalApply(Global global, TokenModel tokenModel) throws Exception {
        globalMapper.updateByPrimaryKeySelective(global);
    }

    @Override
    public void createglobalApply(Global global, TokenModel tokenModel) throws Exception {
        global.preInsert(tokenModel);
        global.setGlobal_id(UUID.randomUUID().toString());
        globalMapper.insert(global);
    }

}
