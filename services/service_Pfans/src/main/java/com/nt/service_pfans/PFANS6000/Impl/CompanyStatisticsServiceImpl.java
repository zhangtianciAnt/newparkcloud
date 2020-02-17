package com.nt.service_pfans.PFANS6000.Impl;

import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Pfans.PFANS6000.Variousfunds;
import com.nt.service_pfans.PFANS6000.CompanyStatisticsService;
import com.nt.service_pfans.PFANS6000.mapper.CompanyStatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.CoststatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.VariousfundsMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyStatisticsServiceImpl implements CompanyStatisticsService {

    @Autowired
    private CoststatisticsMapper coststatisticsMapper;

    @Autowired
    private VariousfundsMapper variousfundsMapper;

    @Autowired
    private CompanyStatisticsMapper companyStatisticsMapper;


    @Override
    public Map<String, Object> getCosts(Coststatistics coststatistics) throws Exception{
        Map<String, Object> result = new HashMap<>();

        Variousfunds variousfunds = new Variousfunds();
//        variousfunds.setOwner(tokenModel.getUserId());
        List<Variousfunds> allVariousfunds = variousfundsMapper.select(variousfunds);
        Map<String, Double> tripMap = new HashMap<>();
        Map<String, Double> assetsMap = new HashMap<>();
        for ( Variousfunds v : allVariousfunds ) {
            String month = v.getPlmonthplan();
            Map<String, Double> targetMap = null;
            if ( "BP014001".equals(v.getTypeoffees())) {
                //出张经费
                targetMap = tripMap;
            } else if ("BP014002".equals(v.getTypeoffees())) {
                //设备经费
                targetMap = assetsMap;
            } else {
                continue;
            }
            double value = targetMap.getOrDefault(month, 0.0);
            double addValue = 0;
            try {
                addValue = Double.parseDouble(v.getPayment());
            } catch (Exception e) {}
            targetMap.put(month, value + addValue);
        }
        // rebuild Map
        // format : {cost1..12, totalcost}
        Map<String, Double> finalTripMap = new HashMap<>();
        Map<String, Double> finalAssetsMap = new HashMap<>();
        double totalTrip = 0;
        double totalAssets = 0;
        for (int i=1; i<=12; i++) {
            String key = i + "";
            double trip = tripMap.getOrDefault(key, 0.0);
            double asset = assetsMap.getOrDefault(key, 0.0);
            finalTripMap.put("cost".concat(key), trip);
            finalAssetsMap.put("cost".concat(key), asset);
            totalAssets += asset;
            totalTrip += trip;
        }
        finalTripMap.put("totalcost", totalTrip);
        finalAssetsMap.put("totalcost", totalAssets);

        // add to result
        result.put("trip", finalTripMap);
        result.put("asset", finalAssetsMap);



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
        result.put("company", new ArrayList<>(companyMap.values()));

        // year
        result.put("year", getBusinessYear());

        return result;
    }

    @Override
    public Map<String, Object> getWorkTimes(Coststatistics coststatistics) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> sqlParams = new HashMap<>();
        sqlParams.putAll(BeanMap.create(coststatistics));
        sqlParams.put("yesValue", "是");
        sqlParams.put("noValue", "否");
        List<CompanyStatistics> list = companyStatisticsMapper.getWorkTimes(sqlParams);
        result.put("worktimes", list);
        // year
        result.put("year", getBusinessYear());
        return result;
    }

    @Override
    public Map<String, Object> getWorkerCounts(Coststatistics coststatistics) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> sqlParams = new HashMap<>();
        sqlParams.putAll(BeanMap.create(coststatistics));
        sqlParams.put("yesValue", "是");
        List<CompanyStatistics> list = companyStatisticsMapper.getWorkers(sqlParams);
        result.put("workers", list);
        // year
        result.put("year", getBusinessYear());
        return result;
    }

    private int getBusinessYear() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        if ( now.get(Calendar.MONTH) <= 2 ) {
            year--;
        }
        return year;
    }
}
