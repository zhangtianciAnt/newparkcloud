package com.nt.service_pfans.PFANS1000.Impl;


import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.IncomeExpenditure;
import com.nt.dao_Pfans.PFANS1000.ThemePlan;
import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.ProjectIncomeVo4;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.IncomeExpenditureService;

import com.nt.service_pfans.PFANS1000.mapper.IncomeExpenditureMapper;
import com.nt.service_pfans.PFANS1000.mapper.ThemePlanDetailMapper;
import com.nt.service_pfans.PFANS1000.mapper.ThemePlanMapper;
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
public class IncomeExpenditureServiceImpl implements IncomeExpenditureService {
    @Autowired
    private ThemePlanDetailMapper themePlanDetailMapper;

    @Autowired
    private ThemePlanMapper themePlanMapper;

    @Autowired
    private IncomeExpenditureMapper incomeexpendituremapper;

    @Autowired
    private OrgTreeService orgTreeService;

    @Override
    public List<IncomeExpenditure> getdatalist() throws Exception {
        List<IncomeExpenditure> themeplanlist = incomeexpendituremapper.selectAll();
        List<IncomeExpenditure> list = themeplanlist.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getYear() + ";" + o.getGroup_id() + ";" + o.getCenter_id()))), ArrayList::new)
        );
        return list;
    }

    //汇率定时任务
    @Scheduled(cron = "0 */5 * * * ?")
//    @Scheduled(cron = "0 10 0 1 12 ?")
    public void getThemeDetatiList() throws Exception {
        SimpleDateFormat s = new SimpleDateFormat("MM");
        SimpleDateFormat s1 = new SimpleDateFormat("YYYY");
        OrgTree orgs = orgTreeService.get(new OrgTree());
        for (OrgTree org : orgs.getOrgs()) {
            for (OrgTree org1 : org.getOrgs()) {
                int year = Integer.valueOf(s.format(new Date())) >= 4 ? Integer.valueOf(s1.format(new Date())) + 1 : Integer.valueOf(s1.format(new Date()));
                ThemePlanDetail themePlanDetail = new ThemePlanDetail();
                themePlanDetail.setYear(String.valueOf(year));
                themePlanDetail.setGroup_id(org1.get_id());
                List<ThemePlanDetail> themeplandetail = themePlanDetailMapper.select(themePlanDetail);
                if (themeplandetail.size() > 0) {
                    for (ThemePlanDetail list : themeplandetail) {
                        IncomeExpenditure incomeexpenditures = new IncomeExpenditure();
                        String theme = list.getThemename();
                        for (int i = 1; i <= 12; i++) {
                            String month = i <= 9 ? "0" + i : String.valueOf(i);
                            List<ProjectContract> projectcontractlist = incomeexpendituremapper.getprojectcontractlist(theme, String.valueOf(year), month);
                            if (i == 1) {
                                incomeexpenditures.setPlanamount1(projectcontractlist.get(0).getContractamount());
                            } else if (i == 2) {
                                incomeexpenditures.setPlanamount2(projectcontractlist.get(0).getContractamount());
                            } else if (i == 3) {
                                incomeexpenditures.setPlanamount3(projectcontractlist.get(0).getContractamount());
                            } else if (i == 4) {
                                incomeexpenditures.setPlanamount4(projectcontractlist.get(0).getContractamount());
                            } else if (i == 5) {
                                incomeexpenditures.setPlanamount5(projectcontractlist.get(0).getContractamount());
                            } else if (i == 6) {
                                incomeexpenditures.setPlanamount6(projectcontractlist.get(0).getContractamount());
                            } else if (i == 7) {
                                incomeexpenditures.setPlanamount7(projectcontractlist.get(0).getContractamount());
                            } else if (i == 8) {
                                incomeexpenditures.setPlanamount8(projectcontractlist.get(0).getContractamount());
                            } else if (i == 9) {
                                incomeexpenditures.setPlanamount9(projectcontractlist.get(0).getContractamount());
                            } else if (i == 10) {
                                incomeexpenditures.setPlanamount10(projectcontractlist.get(0).getContractamount());
                            } else if (i == 11) {
                                incomeexpenditures.setPlanamount11(projectcontractlist.get(0).getContractamount());
                            } else if (i == 12) {
                                incomeexpenditures.setPlanamount12(projectcontractlist.get(0).getContractamount());
                            }
                        }
                        incomeexpenditures.setIncomeexpenditure_id(UUID.randomUUID().toString());
                        incomeexpenditures.setAmount1(list.getAmount1());
                        incomeexpenditures.setAmount2(list.getAmount2());
                        incomeexpenditures.setAmount3(list.getAmount3());
                        incomeexpenditures.setAmount4(list.getAmount4());
                        incomeexpenditures.setAmount5(list.getAmount5());
                        incomeexpenditures.setAmount6(list.getAmount6());
                        incomeexpenditures.setAmount7(list.getAmount7());
                        incomeexpenditures.setAmount8(list.getAmount8());
                        incomeexpenditures.setAmount9(list.getAmount9());
                        incomeexpenditures.setAmount10(list.getAmount10());
                        incomeexpenditures.setAmount11(list.getAmount11());
                        incomeexpenditures.setAmount12(list.getAmount12());
                        incomeexpenditures.setGroup_id(list.getGroup_id());
                        incomeexpenditures.setYear(list.getYear());
                        incomeexpenditures.setBranch(list.getBranch());
                        incomeexpenditures.setCenter_id(list.getCenter_id());
                        incomeexpenditures.setCurrencytype(list.getCurrencytype());
                        incomeexpenditures.setKind(list.getKind());
                        incomeexpenditures.setThemename(list.getThemename());
                        incomeexpendituremapper.insert(incomeexpenditures);
                    }
                }
            }
        }
    }

    @Override
    public List<IncomeExpenditure> selectlist(String year, String group_id) throws Exception {
        List<IncomeExpenditure> onlylist = new ArrayList<>();
        IncomeExpenditure incomeexpenditure = new IncomeExpenditure();
        incomeexpenditure.setYear(year);
        incomeexpenditure.setGroup_id(group_id);
        List<IncomeExpenditure> incomeexpenditurelist = incomeexpendituremapper.select(incomeexpenditure);
        if (incomeexpenditurelist.size() > 0) {
            onlylist.addAll(incomeexpenditurelist);
        } else {
            ThemePlanDetail themePlanDetail = new ThemePlanDetail();
            themePlanDetail.setYear(year);
            themePlanDetail.setGroup_id(group_id);
            List<ThemePlanDetail> themeplandetail = themePlanDetailMapper.select(themePlanDetail);
            for (ThemePlanDetail list : themeplandetail) {
                IncomeExpenditure incomeexpenditures = new IncomeExpenditure();
                String theme = list.getThemename();
                for (int i = 1; i <= 12; i++) {
                    String month = i <= 9 ? "0" + i : String.valueOf(i);
                    List<ProjectContract> projectcontractlist = incomeexpendituremapper.getprojectcontractlist(theme, year, month);
                    if (i == 1) {
                        incomeexpenditures.setPlanamount1(projectcontractlist.get(0).getContractamount());
                    } else if (i == 2) {
                        incomeexpenditures.setPlanamount2(projectcontractlist.get(0).getContractamount());
                    } else if (i == 3) {
                        incomeexpenditures.setPlanamount3(projectcontractlist.get(0).getContractamount());
                    } else if (i == 4) {
                        incomeexpenditures.setPlanamount4(projectcontractlist.get(0).getContractamount());
                    } else if (i == 5) {
                        incomeexpenditures.setPlanamount5(projectcontractlist.get(0).getContractamount());
                    } else if (i == 6) {
                        incomeexpenditures.setPlanamount6(projectcontractlist.get(0).getContractamount());
                    } else if (i == 7) {
                        incomeexpenditures.setPlanamount7(projectcontractlist.get(0).getContractamount());
                    } else if (i == 8) {
                        incomeexpenditures.setPlanamount8(projectcontractlist.get(0).getContractamount());
                    } else if (i == 9) {
                        incomeexpenditures.setPlanamount9(projectcontractlist.get(0).getContractamount());
                    } else if (i == 10) {
                        incomeexpenditures.setPlanamount10(projectcontractlist.get(0).getContractamount());
                    } else if (i == 11) {
                        incomeexpenditures.setPlanamount11(projectcontractlist.get(0).getContractamount());
                    } else if (i == 12) {
                        incomeexpenditures.setPlanamount12(projectcontractlist.get(0).getContractamount());
                    }
                }
                incomeexpenditures.setAmount1(list.getAmount1());
                incomeexpenditures.setAmount2(list.getAmount2());
                incomeexpenditures.setAmount3(list.getAmount3());
                incomeexpenditures.setAmount4(list.getAmount4());
                incomeexpenditures.setAmount5(list.getAmount5());
                incomeexpenditures.setAmount6(list.getAmount6());
                incomeexpenditures.setAmount7(list.getAmount7());
                incomeexpenditures.setAmount8(list.getAmount8());
                incomeexpenditures.setAmount9(list.getAmount9());
                incomeexpenditures.setAmount10(list.getAmount10());
                incomeexpenditures.setAmount11(list.getAmount11());
                incomeexpenditures.setAmount12(list.getAmount12());
                incomeexpenditures.setGroup_id(list.getGroup_id());
                incomeexpenditures.setYear(list.getYear());
                incomeexpenditures.setBranch(list.getBranch());
                incomeexpenditures.setCenter_id(list.getCenter_id());
                incomeexpenditures.setCurrencytype(list.getCurrencytype());
                incomeexpenditures.setKind(list.getKind());
                incomeexpenditures.setThemename(list.getThemename());
                onlylist.add(incomeexpenditures);
            }

        }
        return onlylist;
    }

    @Override
    public void insert(List<IncomeExpenditure> incomeexpenditure, TokenModel tokenModel) throws Exception {
        String groupid = incomeexpenditure.get(0).getGroup_id();
        String year = incomeexpenditure.get(0).getYear();
        IncomeExpenditure income = new IncomeExpenditure();
        income.setYear(year);
        income.setGroup_id(groupid);
        List<IncomeExpenditure> incomeexpenditurelist = incomeexpendituremapper.select(income);
        if (incomeexpenditurelist.size() > 0) {
            for (IncomeExpenditure inc : incomeexpenditure) {
                inc.preUpdate(tokenModel);
                incomeexpendituremapper.updateByPrimaryKeySelective(inc);
            }
        } else {
            for (IncomeExpenditure inc : incomeexpenditure) {
                inc.setIncomeexpenditure_id(UUID.randomUUID().toString());
                inc.preInsert(tokenModel);
                incomeexpendituremapper.insert(inc);
            }
        }
    }
}
