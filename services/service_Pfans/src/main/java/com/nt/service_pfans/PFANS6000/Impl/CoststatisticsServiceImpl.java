package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.Variousfunds;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.service_pfans.PFANS6000.mapper.CoststatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.VariousfundsMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CoststatisticsServiceImpl implements CoststatisticsService {

    @Autowired
    private CoststatisticsMapper coststatisticsMapper;

    @Autowired
    private VariousfundsMapper variousfundsMapper;

    @Override
    public List<Coststatistics> getCostList(Coststatistics coststatistics) throws Exception {
        return coststatisticsMapper.select(coststatistics);
    }

    @Override
    public Integer insertCoststatistics(Coststatistics coststatistics, TokenModel tokenModel) throws Exception {
        coststatisticsMapper.delete(coststatistics);

        Variousfunds variousfunds = new Variousfunds();
//        variousfunds.setOwner(tokenModel.getUserId());
        List<Variousfunds> allVariousfunds = variousfundsMapper.select(variousfunds);
        Map<String, Double> variousfundsMap = new HashMap<String, Double>();
        for ( Variousfunds v : allVariousfunds ) {
            String key = v.getBpplayer() + v.getPlmonthplan();
            Double value = 0.0;
            try {
                value = Double.parseDouble(v.getPayment().trim());
            } catch (Exception e) {}
            if ( variousfundsMap.containsKey(key) ) {
                value = value + variousfundsMap.get(key);
            }
            variousfundsMap.put(key, value);
        }

        List<Coststatistics> allCostList = coststatisticsMapper.getExpatriatesinfor(coststatistics);
        for ( Coststatistics c : allCostList ) {
            // 合计费用
            double totalmanhours = 0;
            // 合计工数
            double totalcost = 0;
            double price = c.getUnitprice();
            for ( int i = 1 ; i <= 12; i++ ) {
                double manhour = 0;
                String property = "manhour" + i;
                try {
                    manhour = Double.parseDouble(BeanUtils.getProperty(c, property));
                } catch (Exception e) {}
                double cost = price * manhour;
                BeanUtils.setProperty(c, "cost" + i, cost);
                totalmanhours += manhour;
                totalcost += cost;
                if ( i%3 ==0 ) {
                    // 经费处理
                    String variousKey = c.getBpname() + i;
                    double various = 0;
                    if ( variousfundsMap.containsKey(variousKey) ) {
                        various = variousfundsMap.get(variousKey);
                    }
                    BeanUtils.setProperty(c, "expense" +i, various);
                    BeanUtils.setProperty(c, "contract" +i, various + totalcost);

                    BeanUtils.setProperty(c,"support" + i, i );
                    BeanUtils.setProperty(c, "totalcost" + i, totalcost);
                    BeanUtils.setProperty(c, "totalmanhours" + i, totalmanhours);
                    totalcost = 0;
                    totalmanhours = 0;
                }
            }

            c.setCoststatistics_id(UUID.randomUUID().toString());
            c.preInsert(tokenModel);
        }

        int insertCount = 0;
        if ( allCostList.size() > 0 ) {
            insertCount = coststatisticsMapper.insertAll(allCostList);
        }

        return insertCount;
    }
}
