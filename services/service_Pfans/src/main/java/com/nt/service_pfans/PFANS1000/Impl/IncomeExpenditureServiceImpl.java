package com.nt.service_pfans.PFANS1000.Impl;


import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.IncomeExpenditure;
import com.nt.dao_Pfans.PFANS1000.ThemePlan;
import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.IncomeExpenditureVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ProjectIncomeVo4;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.service_Org.DictionaryService;
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
    private DictionaryService dictionaryService;

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

    @Override
    public List<IncomeExpenditureVo> getradio(String radiox, String radioy) throws Exception {
        List<IncomeExpenditureVo> incomeexpenditurevolist = new ArrayList<>();
        SimpleDateFormat s = new SimpleDateFormat("MM");
        SimpleDateFormat s1 = new SimpleDateFormat("YYYY");
        if (radioy.equals("4")) {
            OrgTree orgs = orgTreeService.get(new OrgTree());
            for (OrgTree org : orgs.getOrgs()) {
                for (OrgTree org1 : org.getOrgs()) {
                    int year = Integer.valueOf(s.format(new Date())) >= 4 ? Integer.valueOf(s1.format(new Date())) + 1 : Integer.valueOf(s1.format(new Date()));
                    IncomeExpenditure incomeexpenditure = new IncomeExpenditure();
                    incomeexpenditure.setYear(String.valueOf(year));
                    incomeexpenditure.setGroup_id(org1.get_id());
                    List<IncomeExpenditure> incomeexpenditurelist = incomeexpendituremapper.select(incomeexpenditure);
                    if (radiox.equals("1")) {
                        if (incomeexpenditurelist.size() > 0) {
                            for (IncomeExpenditure list : incomeexpenditurelist) {
                                IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                                List<com.nt.dao_Org.Dictionary> curListA = dictionaryService.getForSelect("PJ063");
                                for (Dictionary iteA : curListA) {
                                    if (iteA.getCode().equals(list.getBranch())) {
                                        list.setBranch(iteA.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListD = dictionaryService.getForSelect("PJ143");
                                for (Dictionary iteD : curListD) {
                                    if (iteD.getCode().equals(list.getOtherone())) {
                                        list.setOtherone(iteD.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListC = dictionaryService.getForSelect("PJ144");
                                for (Dictionary iteC : curListC) {
                                    if (iteC.getCode().equals(list.getOthertwo())) {
                                        list.setOthertwo(iteC.getValue1());
                                    }
                                }
                                List<com.nt.dao_Org.Dictionary> curListB = dictionaryService.getForSelect("PJ145");
                                for (Dictionary iteB : curListB) {
                                    if (iteB.getCode().equals(list.getOtherthree())) {
                                        list.setOtherthree(iteB.getValue1());
                                    }
                                }
                                //预计十二个月
                                incomeexpenditurevo.setAmount1(list.getAmount1());
                                incomeexpenditurevo.setAmount2(list.getAmount2());
                                incomeexpenditurevo.setAmount3(list.getAmount3());
                                incomeexpenditurevo.setAmount4(list.getAmount4());
                                incomeexpenditurevo.setAmount5(list.getAmount5());
                                incomeexpenditurevo.setAmount6(list.getAmount6());
                                incomeexpenditurevo.setAmount7(list.getAmount7());
                                incomeexpenditurevo.setAmount8(list.getAmount8());
                                incomeexpenditurevo.setAmount9(list.getAmount9());
                                incomeexpenditurevo.setAmount10(list.getAmount10());
                                incomeexpenditurevo.setAmount11(list.getAmount11());
                                incomeexpenditurevo.setAmount12(list.getAmount12());
                                //实际十二个月
                                incomeexpenditurevo.setPlanamount1(list.getPlanamount1());
                                incomeexpenditurevo.setPlanamount2(list.getPlanamount2());
                                incomeexpenditurevo.setPlanamount3(list.getPlanamount3());
                                incomeexpenditurevo.setPlanamount4(list.getPlanamount4());
                                incomeexpenditurevo.setPlanamount5(list.getPlanamount5());
                                incomeexpenditurevo.setPlanamount6(list.getPlanamount6());
                                incomeexpenditurevo.setPlanamount7(list.getPlanamount7());
                                incomeexpenditurevo.setPlanamount8(list.getPlanamount8());
                                incomeexpenditurevo.setPlanamount9(list.getPlanamount9());
                                incomeexpenditurevo.setPlanamount10(list.getPlanamount10());
                                incomeexpenditurevo.setPlanamount11(list.getPlanamount11());
                                incomeexpenditurevo.setPlanamount12(list.getPlanamount12());
                                incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                                incomeexpenditurevo.setBranch(list.getBranch());
                                incomeexpenditurevo.setThemename(list.getThemename());
                                incomeexpenditurevo.setOtherone(list.getOtherone());
                                incomeexpenditurevo.setOthertwo(list.getOthertwo());
                                incomeexpenditurevo.setOtherthree(list.getOtherthree());
                                incomeexpenditurevo.setCenter_id("大连松下");
                                incomeexpenditurevolist.add(incomeexpenditurevo);
                            }
                        }
                    }
                    else if (radiox.equals("2")) {
                        if (incomeexpenditurelist.size() > 0) {
                            for (IncomeExpenditure list : incomeexpenditurelist) {
                                IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                                //预计十二个月

                                incomeexpenditurevo.setAmount1(list.getAmount1());
                                incomeexpenditurevo.setAmount2(list.getAmount2());
                                incomeexpenditurevo.setAmount3(list.getAmount3());
                                incomeexpenditurevo.setAmount4(list.getAmount4());
                                incomeexpenditurevo.setAmount5(list.getAmount5());
                                incomeexpenditurevo.setAmount6(list.getAmount6());
                                incomeexpenditurevo.setAmount7(list.getAmount7());
                                incomeexpenditurevo.setAmount8(list.getAmount8());
                                incomeexpenditurevo.setAmount9(list.getAmount9());
                                incomeexpenditurevo.setAmount10(list.getAmount10());
                                incomeexpenditurevo.setAmount11(list.getAmount11());
                                incomeexpenditurevo.setAmount12(list.getAmount12());
                                //实际十二个月
                                incomeexpenditurevo.setPlanamount1(list.getPlanamount1());
                                incomeexpenditurevo.setPlanamount2(list.getPlanamount2());
                                incomeexpenditurevo.setPlanamount3(list.getPlanamount3());
                                incomeexpenditurevo.setPlanamount4(list.getPlanamount4());
                                incomeexpenditurevo.setPlanamount5(list.getPlanamount5());
                                incomeexpenditurevo.setPlanamount6(list.getPlanamount6());
                                incomeexpenditurevo.setPlanamount7(list.getPlanamount7());
                                incomeexpenditurevo.setPlanamount8(list.getPlanamount8());
                                incomeexpenditurevo.setPlanamount9(list.getPlanamount9());
                                incomeexpenditurevo.setPlanamount10(list.getPlanamount10());
                                incomeexpenditurevo.setPlanamount11(list.getPlanamount11());
                                incomeexpenditurevo.setPlanamount12(list.getPlanamount12());
                                incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                                incomeexpenditurevo.setBranch(list.getBranch());
                                incomeexpenditurevo.setThemename(list.getThemename());
                                incomeexpenditurevo.setOtherone(list.getOtherone());
                                incomeexpenditurevo.setOthertwo(list.getOthertwo());
                                incomeexpenditurevo.setOtherthree(list.getOtherthree());
                                incomeexpenditurevo.setCenter_id("大连松下");
                                incomeexpenditurevolist.add(incomeexpenditurevo);
                            }
                        }
                    }
                    else if (radiox.equals("3")) {
                        if (incomeexpenditurelist.size() > 0) {
                            for (IncomeExpenditure list : incomeexpenditurelist) {
                                IncomeExpenditureVo incomeexpenditurevo = new IncomeExpenditureVo();
                                //预计十二个月
                                incomeexpenditurevo.setAmount1(list.getAmount1());
                                incomeexpenditurevo.setAmount2(list.getAmount2());
                                incomeexpenditurevo.setAmount3(list.getAmount3());
                                incomeexpenditurevo.setAmount4(list.getAmount4());
                                incomeexpenditurevo.setAmount5(list.getAmount5());
                                incomeexpenditurevo.setAmount6(list.getAmount6());
                                incomeexpenditurevo.setAmount7(list.getAmount7());
                                incomeexpenditurevo.setAmount8(list.getAmount8());
                                incomeexpenditurevo.setAmount9(list.getAmount9());
                                incomeexpenditurevo.setAmount10(list.getAmount10());
                                incomeexpenditurevo.setAmount11(list.getAmount11());
                                incomeexpenditurevo.setAmount12(list.getAmount12());
                                //实际十二个月
                                incomeexpenditurevo.setPlanamount1(list.getPlanamount1());
                                incomeexpenditurevo.setPlanamount2(list.getPlanamount2());
                                incomeexpenditurevo.setPlanamount3(list.getPlanamount3());
                                incomeexpenditurevo.setPlanamount4(list.getPlanamount4());
                                incomeexpenditurevo.setPlanamount5(list.getPlanamount5());
                                incomeexpenditurevo.setPlanamount6(list.getPlanamount6());
                                incomeexpenditurevo.setPlanamount7(list.getPlanamount7());
                                incomeexpenditurevo.setPlanamount8(list.getPlanamount8());
                                incomeexpenditurevo.setPlanamount9(list.getPlanamount9());
                                incomeexpenditurevo.setPlanamount10(list.getPlanamount10());
                                incomeexpenditurevo.setPlanamount11(list.getPlanamount11());
                                incomeexpenditurevo.setPlanamount12(list.getPlanamount12());
                                incomeexpenditurevo.setGroup_id(org1.getCompanyen());
                                incomeexpenditurevo.setBranch(list.getBranch());
                                incomeexpenditurevo.setThemename(list.getThemename());
                                incomeexpenditurevo.setOtherone(list.getOtherone());
                                incomeexpenditurevo.setOthertwo(list.getOthertwo());
                                incomeexpenditurevo.setOtherthree(list.getOtherthree());
                                incomeexpenditurevo.setCenter_id("大连松下");
                                incomeexpenditurevolist.add(incomeexpenditurevo);
                            }
                        }
                    }

                }
            }
        }
        return incomeexpenditurevolist;
    }

    //汇率定时任务
//    @Scheduled(cron = "0 */5 * * * ?")
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
