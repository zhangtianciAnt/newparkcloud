package com.nt.service_pfans.PFANS8000.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS8000.MonthlyRate;
import com.nt.service_pfans.PFANS8000.MonthlyRateService;
import com.nt.service_pfans.PFANS8000.mapper.MonthlyRateMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class MonthlyRateServiceImpl implements MonthlyRateService {
    @Autowired
    private MonthlyRateMapper monthlyratemapper;

    @Override
    public List<MonthlyRate> getdatalist() throws Exception {
        List<MonthlyRate> monthlyratelist = monthlyratemapper.selectAll();
        List<MonthlyRate> list = monthlyratelist.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getYear() + ";" + o.getMonth()))), ArrayList::new)
        );
        return list;
    }

    @Override
    public void CreateData(List<MonthlyRate> monthlyrate, TokenModel tokenModel) throws Exception {
        for (MonthlyRate list : monthlyrate) {
            list.preInsert(tokenModel);
            list.setMonthlyrate_id(UUID.randomUUID().toString());
            monthlyratemapper.insert(list);
        }
    }

    @Override
    public void UpdateData(List<MonthlyRate> monthlyrate, TokenModel tokenModel) throws Exception {
        MonthlyRate month = new MonthlyRate();
        month.setYear(monthlyrate.get(0).getYear());
        month.setMonth(monthlyrate.get(0).getMonth());
        monthlyratemapper.delete(month);
        for (MonthlyRate list : monthlyrate) {
            list.preInsert(tokenModel);
            list.setMonthlyrate_id(UUID.randomUUID().toString());
            monthlyratemapper.insert(list);
        }
    }

    @Override
    public List<MonthlyRate> slectlist(MonthlyRate monthlyrate) throws Exception {
        MonthlyRate month = new MonthlyRate();
        month.setYear(monthlyrate.getYear());
        month.setMonth(monthlyrate.getMonth());
        List<MonthlyRate> monthlyratelist = monthlyratemapper.select(month);
        monthlyratelist = monthlyratelist.stream().sorted(Comparator.comparing(MonthlyRate::getIndexdata).reversed()).collect(Collectors.toList());
        return monthlyratelist;
    }
    @Override
    public List<MonthlyRate> slectlist2() throws Exception {
        return monthlyratemapper.selectAll();
    }
}
