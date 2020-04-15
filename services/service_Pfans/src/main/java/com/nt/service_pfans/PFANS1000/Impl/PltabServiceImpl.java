package com.nt.service_pfans.PFANS1000.Impl;

import cn.hutool.core.convert.Convert;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.dao_Pfans.PFANS1000.Pltab;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.PltabService;
import com.nt.service_pfans.PFANS1000.mapper.LoanApplicationMapper;
import com.nt.service_pfans.PFANS1000.mapper.PltabMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service

public class PltabServiceImpl implements PltabService {

    @Autowired
    private PltabMapper pltabMapper;
    @Autowired
    private MongoTemplate mongoTemplate;


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
        List<Pltab> pltabs  = pltabMapper.getPltab(groupid,year,month,"是","有效");
        return pltabs;
    }
}
