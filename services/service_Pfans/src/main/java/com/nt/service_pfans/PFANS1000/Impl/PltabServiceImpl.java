package com.nt.service_pfans.PFANS1000.Impl;

import cn.hutool.core.convert.Convert;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.CostCarryForward;
import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.Pltab;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.PltabService;
import com.nt.service_pfans.PFANS1000.mapper.LoanApplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.PltabMapper;
import com.nt.service_pfans.PFANS1000.mapper.CostCarryForwardMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service

public class PltabServiceImpl implements PltabService {

    @Autowired
    private PltabMapper pltabMapper;

    @Autowired
    private CostCarryForwardMapper costcarryforwardmapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void inset(List<CostCarryForward> costcarryforward, TokenModel tokenModel) throws Exception {
        if(costcarryforward.size()>0){
            CostCarryForward costcarry = new CostCarryForward();
            costcarry.setYear(costcarryforward.get(0).getYear());
            costcarry.setRegion(costcarryforward.get(0).getRegion());
            costcarry.setGroup_id(costcarryforward.get(0).getGroup_id());
            costcarryforwardmapper.delete(costcarry);
            for(CostCarryForward cost:costcarryforward){
                cost.preInsert(tokenModel);
                cost.setCostcarryforward_id(UUID.randomUUID().toString());
                costcarryforwardmapper.insertSelective(cost);
            }
        }
    }
    @Override
    public List<CostCarryForward> getCostList(String groupid, String year, String month) throws Exception {
        CostCarryForward costcarry = new CostCarryForward();
        costcarry.setYear(year);
        costcarry.setRegion(month);
        costcarry.setGroup_id(groupid);
        List<CostCarryForward> costcarryforwardlist = costcarryforwardmapper.select(costcarry);
        return costcarryforwardlist;
    }
    @Override
    public List<Pltab> selectPl(String groupid, String year, String month) throws Exception {
       List<CustomerInfo> customerInfos =  mongoTemplate.find(new Query(), CustomerInfo.class);
       List<LogManagement> logManagements = pltabMapper.getCMPJ(year,month);
       int cm_pjHours = 0;
        for (LogManagement log:
                logManagements) {
            for (CustomerInfo customerInfo:
            customerInfos) {
                if(customerInfo.getUserinfo() != null && log.getJobnumber() != null && log.getJobnumber().equals(customerInfo.getUserinfo().getJobnumber())){
                    //todo GM等没有group成本如何计算？
                    if (customerInfo.getUserinfo().getGroupid() != null) {
                        cm_pjHours += customerInfo.getUserinfo().getGroupid().equals(groupid) ? 1 : 0;
                    }
                }
            }
        }
        List<Pltab> pltabs2  = pltabMapper.selectPlmoney(groupid,year,month);
        List<Pltab> pltabs  = pltabMapper.getPltab(groupid,year,month);
        pltabs2.addAll(pltabs);
        return pltabs2;
    }
}
