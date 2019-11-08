package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Recruit;
import com.nt.service_pfans.PFANS2000.RecruitService;
import com.nt.service_pfans.PFANS2000.mapper.RecruitMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    @Override
    public List<Recruit> getRecruit(Recruit recruit ) {
        return recruitMapper.select(recruit);
    }

    @Override
    public Recruit One(String recruitid) throws Exception {

        return recruitMapper.selectByPrimaryKey(recruitid);
    }

    @Override
    public void updateRecruit(Recruit recruit, TokenModel tokenModel) throws Exception {
        recruitMapper.updateByPrimaryKey(recruit);
    }

    @Override
    public void insert(Recruit recruit, TokenModel tokenModel) throws Exception {

        recruit.preInsert(tokenModel);
        recruit.setRecruitid(UUID.randomUUID().toString());
        recruitMapper.insert(recruit);
    }

    @Override
    public List<Recruit> getRecruitList(Recruit recruit, HttpServletRequest request) throws Exception {

        return recruitMapper.select(recruit) ;
    }
}
