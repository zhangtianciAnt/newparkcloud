package com.nt.service_pfans.PFANS8000.Impl;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS8000.MonthlyRate;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS8000.MonthlyRateService;
import com.nt.service_pfans.PFANS8000.mapper.MonthlyRateMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class MonthlyRateServiceImpl implements MonthlyRateService {
    @Autowired
    private MonthlyRateMapper monthlyratemapper;
    @Autowired
    private DictionaryService dictionaryService;

    //汇率定时任务
    //【每年12月1日10分】
    //@Scheduled(cron = "0 10 0 1 12 ?")
    public void getExchangeRateY() throws Exception {
        List<MonthlyRate> monthlyratelist = new ArrayList<>();
        List<MonthlyRate> monthlyratelists = new ArrayList<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        String data11 = sf.format(new Date()) + "-" + "11";
        String data10 = sf.format(new Date()) + "-" + "10";
        String data9 = sf.format(new Date()) + "-" + "09";
        String data8 = sf.format(new Date()) + "-" + "08";
        String data7 = sf.format(new Date()) + "-" + "07";
        String data6 = sf.format(new Date()) + "-" + "06";
        String data5 = sf.format(new Date()) + "-" + "05";
        String data4 = sf.format(new Date()) + "-" + "04";
        String data3 = sf.format(new Date()) + "-" + "03";
        String data2 = sf.format(new Date()) + "-" + "02";
        String data1 = sf.format(new Date()) + "-" + "01";
        MonthlyRate month = new MonthlyRate();
        month.setYear(sf.format(new Date()));
        month.setMonth(data11);
        List<MonthlyRate> monthlyratelist11 = monthlyratemapper.select(month);
        month.setMonth(data10);
        List<MonthlyRate> monthlyratelist10 = monthlyratemapper.select(month);
        month.setMonth(data9);
        List<MonthlyRate> monthlyratelist9 = monthlyratemapper.select(month);
        month.setMonth(data8);
        List<MonthlyRate> monthlyratelist8 = monthlyratemapper.select(month);
        month.setMonth(data7);
        List<MonthlyRate> monthlyratelist7 = monthlyratemapper.select(month);
        month.setMonth(data6);
        List<MonthlyRate> monthlyratelist6 = monthlyratemapper.select(month);
        month.setMonth(data5);
        List<MonthlyRate> monthlyratelist5 = monthlyratemapper.select(month);
        month.setMonth(data4);
        List<MonthlyRate> monthlyratelist4 = monthlyratemapper.select(month);
        month.setMonth(data3);
        List<MonthlyRate> monthlyratelist3 = monthlyratemapper.select(month);
        month.setMonth(data2);
        List<MonthlyRate> monthlyratelist2 = monthlyratemapper.select(month);
        month.setMonth(data1);
        List<MonthlyRate> monthlyratelist1 = monthlyratemapper.select(month);
        List<com.nt.dao_Org.Dictionary> curListA = dictionaryService.getForSelect("PG019");
        if (curListA.size() == monthlyratelist11.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist11) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else if (curListA.size() == monthlyratelist10.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist10) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else if (curListA.size() == monthlyratelist9.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist9) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else if (curListA.size() == monthlyratelist8.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist8) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else if (curListA.size() == monthlyratelist7.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist7) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else if (curListA.size() == monthlyratelist6.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist6) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else if (curListA.size() == monthlyratelist5.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist5) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else if (curListA.size() == monthlyratelist4.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist4) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else if (curListA.size() == monthlyratelist3.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist3) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else if (curListA.size() == monthlyratelist2.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist2) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else if (curListA.size() == monthlyratelist1.size()) {
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelist1) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        } else {
            int n = 0;
            for (Dictionary dic : curListA) {
                monthlyratelist = monthlyratelist11.stream().filter(item -> (item.getCurrency().equals(dic.getCode()))).collect(Collectors.toList());
                if (monthlyratelist.size() == 0) {
                    n++;
                    int m = monthlyratelist11.size() + n;
                    MonthlyRate monthr = new MonthlyRate();
                    monthr.setCurrency(dic.getCode());
                    monthr.setCurrencyname(dic.getValue1());
                    monthr.setAccountingexchangerate("0");
                    monthr.setBusinessplanexchangerate("0");
                    monthr.setExchangerate("0");
                    monthr.setIndexdata(m);
                    monthlyratelists.add(monthr);
                } else {
                    monthlyratelists.addAll(monthlyratelist);
                }
            }
            int year = 0;
            int years = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
            for (int i = 1; i <= 12; i++) {
                if (i <= 3) {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 2;
                } else {
                    year = Integer.valueOf(DateUtil.format(new Date(), "YYYY")) + 1;
                }
                for (MonthlyRate list : monthlyratelists) {
                    String monthr = i <= 9 ? "0" + i : String.valueOf(i);
                    String data = year + "-" + monthr;
                    list.setMonthlyrate_id(UUID.randomUUID().toString());
                    list.setYear(String.valueOf(years));
                    list.setMonth(data);
                    list.setCreateon(new Date());
                    monthlyratemapper.insert(list);
                }
            }
        }
    }

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
