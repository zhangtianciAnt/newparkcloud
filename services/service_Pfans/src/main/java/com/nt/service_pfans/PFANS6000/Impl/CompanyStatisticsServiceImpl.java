package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.Variousfunds;
import com.nt.service_pfans.PFANS6000.CompanyStatisticsService;
import com.nt.service_pfans.PFANS6000.CoststatisticsService;
import com.nt.service_pfans.PFANS6000.mapper.CoststatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.VariousfundsMapper;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyStatisticsServiceImpl implements CompanyStatisticsService {

    @Autowired
    private CoststatisticsMapper coststatisticsMapper;


    @Override
    public List<CompanyStatistics> getCosts(Coststatistics coststatistics) throws Exception{
        List<Coststatistics> allCostList = coststatisticsMapper.getExpatriatesinfor(coststatistics);

        Map<String, CompanyStatistics> companyMap = new HashMap<>();
        for ( Coststatistics c : allCostList ) {
            String bpcompany = c.getBpcompany();
            CompanyStatistics company = companyMap.get(bpcompany);
            if ( company == null ) {
                company = new CompanyStatistics();
                company.setBpcompany(bpcompany);
            }
            double price = c.getUnitprice();
            // 个人单位的合计费用
            double totalmanhours = 0;
            // 个人单位的合计工数
            double totalcost = 0;
            for ( int i=1; i<=12; i++ ) {
                String p_manhour = "manhour" + i;
                String p_cost = "cost" + i;

                double oldManhour = 0;
                double oldCost = 0;
                try {
                    oldManhour = Double.parseDouble(BeanUtils.getProperty(company, p_manhour));
                } catch (Exception e) {}
                try {
                    oldManhour = Double.parseDouble(BeanUtils.getProperty(company, p_cost));
                } catch (Exception e) {}

                double manhour = 0;
                try {
                    manhour = Double.parseDouble(BeanUtils.getProperty(c, p_manhour));
                } catch (Exception e) {}
                double cost = price * manhour;

                double newCost = cost + oldCost;
                double newManhour = manhour + oldManhour;

//                company.setCost1("");
//                company.setManhour1("");
                BeanUtils.setProperty(company, p_cost, newCost);
                BeanUtils.setProperty(company, p_manhour, newManhour);
                totalcost += cost;
                totalmanhours += manhour;
            }

            double oldTotalmanhours = 0;
            double oldTotalcost = 0;
            try {
                oldTotalcost = Double.parseDouble(company.getTotalcost());
            } catch (Exception e) {}
            try {
                oldTotalmanhours = Double.parseDouble(company.getTotalmanhours());
            } catch (Exception e) {}

            company.setTotalcost(oldTotalcost + totalcost + "");
            company.setTotalmanhours(oldTotalmanhours + totalmanhours + "");
            companyMap.put(bpcompany, company);
        }

        return new ArrayList<>(companyMap.values());
    }

    @Override
    public List<CompanyStatistics> getWorkTimes(Coststatistics coststatistics) {
        return null;
    }

    @Override
    public List<CompanyStatistics> getWorkTimeInfos(Coststatistics coststatistics) {
        return null;
    }
}
