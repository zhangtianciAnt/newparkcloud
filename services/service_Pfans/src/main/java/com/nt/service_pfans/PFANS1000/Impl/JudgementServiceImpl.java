package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.service_pfans.PFANS1000.JudgementService;
import com.nt.service_pfans.PFANS1000.mapper.JudgementMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class JudgementServiceImpl implements JudgementService {

    @Autowired
    private JudgementMapper judgementMapper;


    @Override
    public List<Judgement> getJudgement(Judgement judgement) throws Exception {
        return judgementMapper.select(judgement);
    }

    @Override
    public List<Judgement> selectJudgement() throws Exception {
        return judgementMapper.selectJudgement();
    }

    @Override
    public Judgement One(String judgementid) throws Exception {
        return judgementMapper.selectByPrimaryKey(judgementid);
    }

    @Override
    public void update(Judgement judgement, TokenModel tokenModel) throws Exception {
        judgementMapper.updateByPrimaryKeySelective(judgement);
    }

    @Override
    public void insert(Judgement judgement, TokenModel tokenModel) throws Exception {
        String judgementid = UUID.randomUUID().toString();
        Integer rowindex = judgementMapper.selectCount(judgement) + 1;
        judgement.preInsert(tokenModel);
        judgement.setJudgementid(judgementid);
        judgement.setEquipment("0");
        judgementMapper.insertSelective(judgement);
    }

    @Override
    public List<Judgement> getJudgementList(Judgement judgement, HttpServletRequest request) throws Exception {
        return judgementMapper.select(judgement);
    }
}
