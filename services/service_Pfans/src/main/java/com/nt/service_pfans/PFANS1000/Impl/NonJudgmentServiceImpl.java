package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.NonJudgment;
import com.nt.service_pfans.PFANS1000.NonJudgmentService;
import com.nt.service_pfans.PFANS1000.mapper.NonJudgmentMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class NonJudgmentServiceImpl implements NonJudgmentService {


    @Autowired
    private NonJudgmentMapper nonJudgmentMapper;



    @Override
    public List<NonJudgment> get(NonJudgment nonJudgment) throws Exception {
        return nonJudgmentMapper.select(nonJudgment);
    }

    @Override
    public NonJudgment one(String nonjumend_id) throws Exception {
        return nonJudgmentMapper.selectByPrimaryKey(nonjumend_id);
    }

    @Override
    public void update(NonJudgment nonJudgment, TokenModel tokenModel) throws Exception {
         nonJudgment.preUpdate(tokenModel);
         nonJudgmentMapper.updateByPrimaryKeySelective(nonJudgment);
    }
}
