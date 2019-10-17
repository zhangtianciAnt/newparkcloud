package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.service_pfans.PFANS2000.CasgiftApplyService;
import com.nt.service_pfans.PFANS2000.mapper.CasgiftApplyMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class CasgiftApplyServiceImpl implements CasgiftApplyService {

    @Autowired
    private CasgiftApplyMapper casgiftapplyMapper;

    @Override
    public List<CasgiftApply> getCasgiftApply(CasgiftApply casgiftapply ) {
        return casgiftapplyMapper.select(casgiftapply);
    }

    @Override
    public CasgiftApply One(String casgiftapplyid) throws Exception {

        return casgiftapplyMapper.selectByPrimaryKey(casgiftapplyid);
    }

    @Override
    public void updateCasgiftApply(CasgiftApply casgiftapply, TokenModel tokenModel) throws Exception {
        casgiftapplyMapper.updateByPrimaryKeySelective(casgiftapply);
    }

    @Override
    public void insert(CasgiftApply casgiftapply, TokenModel tokenModel) throws Exception {

        casgiftapply.preInsert(tokenModel);
        casgiftapply.setCasgiftapplyid(UUID.randomUUID().toString());
        casgiftapplyMapper.insert(casgiftapply);
    }
}
