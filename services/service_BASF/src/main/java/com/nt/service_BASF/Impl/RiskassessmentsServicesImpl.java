package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Riskassessments;
import com.nt.service_BASF.RiskassessmentsServices;
import com.nt.service_BASF.mapper.RiskassessmentsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: RiskassessmentsServicesImpl
 * @Author: 王哲
 * @Description: 风险研判（MySql表）Servicelmpl
 * @Date: 2020/3/25 15:43
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RiskassessmentsServicesImpl implements RiskassessmentsServices {

    @Autowired
    private RiskassessmentsMapper riskassessmentsMapper;

    //获取风险研判数据
    @Override
    public List<Riskassessments> getAll(Riskassessments riskassessments) throws Exception {
        return riskassessmentsMapper.select(riskassessments);
    }

    //更新风险研判数据
    @Override
    public void updataRiskassessments(Riskassessments riskassessments, TokenModel tokenModel) throws Exception {
        riskassessments.preUpdate(tokenModel);
        riskassessmentsMapper.updateByPrimaryKeySelective(riskassessments);
    }

    //增加风险研判数据
    @Override
    public void insertRiskassessments(Riskassessments riskassessments, TokenModel tokenModel) throws Exception {
        riskassessments.preInsert(tokenModel);
        riskassessments.setRiskassessmentid(UUID.randomUUID().toString());
        riskassessmentsMapper.insert(riskassessments);
    }

    //根据id查找风险研判数据
    @Override
    public Riskassessments getDataById(String id) throws Exception {
        Riskassessments riskassessments = new Riskassessments();
        riskassessments.setRiskassessmentid(id);
        return riskassessmentsMapper.selectOne(riskassessments);
    }

    //获取今日风险研判综合信息(前端大屏首页用)
    @Override
    public Riskassessments getSynthesize() throws Exception {
        return new Riskassessments();
    }

    //获取今日各装置综合信息
    @Override
    public List<Riskassessments> getRespectiveData() throws Exception {
        List<Riskassessments> riskassessments = new ArrayList<>();
        return riskassessments;
    }


}
