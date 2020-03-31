package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Riskassessments;
import com.nt.service_BASF.RiskassessmentsServices;
import com.nt.service_BASF.mapper.RiskassessmentsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String insertRiskassessments(Riskassessments riskassessments, TokenModel tokenModel) throws Exception {
        riskassessments.preInsert(tokenModel);
        String uuid = UUID.randomUUID().toString();
        riskassessments.setRiskassessmentid(uuid);
        riskassessmentsMapper.insert(riskassessments);
        return uuid;
    }

    //根据id查找风险研判数据
    @Override
    public Riskassessments getDataById(String id) throws Exception {
        Riskassessments riskassessments = new Riskassessments();
        riskassessments.setRiskassessmentid(id);
        return riskassessmentsMapper.selectOne(riskassessments);
    }

    //根据装置code查找今日有无填写信息
    @Override
    public boolean checkExist(String devicecode) throws Exception {
        int count = riskassessmentsMapper.checkExist(devicecode);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    //查询装置今日已填写的风险研判信息
    public List<Riskassessments> writeList() throws Exception {
        return riskassessmentsMapper.writeList();
    }

}
