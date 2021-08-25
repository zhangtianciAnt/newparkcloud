package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.PjExternalInjection;
import com.nt.dao_Pfans.PFANS6000.Variousfunds;
import com.nt.dao_Pfans.PFANS6000.Vo.PjExternalInjectionVo;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS1000.DepartmentalService;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.service_pfans.PFANS6000.PjExternalInjectionService;
import com.nt.service_pfans.PFANS6000.mapper.CoststatisticsMapper;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS6000.mapper.PjExternalInjectionMapper;
import com.nt.service_pfans.PFANS6000.mapper.VariousfundsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component("PjExternalInjection")
@Service
@Transactional(rollbackFor = Exception.class)
public class PjExternalInjectionServiceImpl implements PjExternalInjectionService {

    @Autowired
    private PjExternalInjectionMapper pjExternalInjectionMapper;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private VariousfundsMapper variousfundsMapper;

    @Autowired
    private CompanyProjectsMapper companyProjectsMapper;

    @Autowired
    private CoststatisticsMapper coststatisticsMapper;

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Autowired
    private DepartmentalService departmentalService;

    @Autowired
    private DepartmentAccountService departmentAccountService;


    //pj别外注费统计定时任务
    //每日凌晨0点15分
    @Scheduled(cron = "0 15 0 * * ?")
    public void saveTableinfo() throws Exception {

    //region add_修改定时任务时间、并对下面流程做判断
        SimpleDateFormat sfYM = new SimpleDateFormat("yyyyMM");
        Calendar lastMonthDate = Calendar.getInstance();
        lastMonthDate.add(Calendar.MONTH, -1);
        String year = sfYM.format(lastMonthDate.getTime());
        String Y = year.substring(0, 4);
        String M = year.substring(4, 6);

        List<String> groupIdList = new ArrayList<String>();
        Expatriatesinfor infor = new Expatriatesinfor();
        infor.setExits("1");
        infor.setWhetherentry("BP006001");
        infor.setExitime(null);
        List<Expatriatesinfor> inforlist = expatriatesinforMapper.select(infor);
        Map<String,List<Expatriatesinfor>> userGroupMap = inforlist.stream().collect(Collectors.groupingBy(Expatriatesinfor::getGroup_id));
        for (String key : userGroupMap.keySet()) {
            groupIdList.add(key + "," + Y +  "," + M);
        }
        //查询所有审批通过的部门
        List<Workflowinstance> workflow = coststatisticsMapper.getworkflowinstance(groupIdList);
        if(workflow.size() == groupIdList.size()) {
            //region pj别外注统计定时任务
            //获取当前系统中有效的部门，按照预算编码统计
            List<DepartmentVo> departmentVoList = new ArrayList<>();
            departmentVoList = orgTreeService.getAllDepartment();

            TokenModel tokenModel = new TokenModel();
//            SimpleDateFormat sfYM = new SimpleDateFormat("yyyyMM");
//            Calendar lastMonthDate = Calendar.getInstance();
//            lastMonthDate.add(Calendar.MONTH, -1);
//            String year = sfYM.format(lastMonthDate.getTime());
            for (DepartmentVo departvo : departmentVoList) {
                List<PjExternalInjectionVo> pjlistVo = pjExternalInjectionMapper.getThemeCompany(year, departvo.getDepartmentId());

                String VarYear = new Date().toString();
                VarYear = VarYear.substring(VarYear.length() - 4, VarYear.length()) + "-01-01";
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
                Date varyear = sim.parse(VarYear);
                Variousfunds variousfunds = new Variousfunds();
                variousfunds.setYear(varyear);
                List<Variousfunds> variousfundsList = variousfundsMapper.select(variousfunds);
                CompanyProjects companyProjects = new CompanyProjects();


                PjExternalInjection pjExternal = new PjExternalInjection();
                List<PjExternalInjection> insertpjExternal = new ArrayList<>();
                List<PjExternalInjection> updatepjExternal = new ArrayList<>();
                if (pjlistVo.size() > 0) {
                    for (PjExternalInjectionVo pjExternalInjectionVo : pjlistVo) {
                        pjExternal = new PjExternalInjection();
                        pjExternal.setPjexternalinjection_id(UUID.randomUUID().toString());
                        pjExternal.setYears(year.substring(0, 4));
                        pjExternal.setGroup_id(pjExternalInjectionVo.getGroup_id());
                        pjExternal.setThemeinfor_id(pjExternalInjectionVo.getThemeinfor_id());
                        pjExternal.setThemename(pjExternalInjectionVo.getThemename());
                        pjExternal.setDivide(pjExternalInjectionVo.getDivide());
                        pjExternal.setToolsorgs(pjExternalInjectionVo.getToolsorgs());
                        pjExternal.setCompanyprojects_id(pjExternalInjectionVo.getCompanyprojects_id());
                        pjExternal.setProject_name(pjExternalInjectionVo.getProject_name());
                        pjExternal.setCompany(pjExternalInjectionVo.getCompany());
                        pjExternal.setApril("0.00");
                        pjExternal.setMay("0.00");
                        pjExternal.setJune("0.00");
                        pjExternal.setJuly("0.00");
                        pjExternal.setAugust("0.00");
                        pjExternal.setSeptember("0.00");
                        pjExternal.setOctober("0.00");
                        pjExternal.setNovember("0.00");
                        pjExternal.setDecember("0.00");
                        pjExternal.setJanuary("0.00");
                        pjExternal.setFebruary("0.00");
                        pjExternal.setMarch("0.00");
                        if (pjExternalInjectionVo.getMoney() != null) {
                            if (year.substring(4, 6).equals("04")) {
                                pjExternal.setApril(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("05")) {
                                pjExternal.setMay(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("06")) {
                                pjExternal.setJune(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("07")) {
                                pjExternal.setJuly(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("08")) {
                                pjExternal.setAugust(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("09")) {
                                pjExternal.setSeptember(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("10")) {
                                pjExternal.setOctober(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("11")) {
                                pjExternal.setNovember(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("12")) {
                                pjExternal.setDecember(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("01")) {
                                pjExternal.setJanuary(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("02")) {
                                pjExternal.setFebruary(pjExternalInjectionVo.getMoney());
                            } else if (year.substring(4, 6).equals("03")) {
                                pjExternal.setMarch(pjExternalInjectionVo.getMoney());
                            }
                        }

                        if (variousfundsList.size() > 0) {
                            for (Variousfunds various : variousfundsList) {
                                companyProjects = new CompanyProjects();
                                companyProjects.setNumbers(various.getPjname().substring(0, 13));
                                List<CompanyProjects> comList = companyProjectsMapper.select(companyProjects);
                                if (comList.size() > 0) {
                                    if (comList.get(0).getCompanyprojects_id().equals(pjExternalInjectionVo.getCompanyprojects_id())
                                            && various.getBpclubname().equals(pjExternalInjectionVo.getCompany())) {

                                        if (various.getPlmonthplan().equals("BP013001")) {
                                            pjExternal.setJune(String.valueOf(Integer.valueOf(pjExternal.getJune()) + Integer.valueOf(various.getPayment())));
                                        } else if (various.getPlmonthplan().equals("BP013002")) {
                                            pjExternal.setSeptember(String.valueOf(Integer.valueOf(pjExternal.getSeptember()) + Integer.valueOf(various.getPayment())));
                                        } else if (various.getPlmonthplan().equals("BP013003")) {
                                            pjExternal.setDecember(String.valueOf(Integer.valueOf(pjExternal.getDecember()) + Integer.valueOf(various.getPayment())));
                                        } else if (various.getPlmonthplan().equals("BP013004")) {
                                            pjExternal.setMarch(String.valueOf(Integer.valueOf(pjExternal.getMarch()) + Integer.valueOf(various.getPayment())));
                                        }
                                    }
                                }
                            }
                        }

                        PjExternalInjection injection = new PjExternalInjection();
                        injection.setYears(year.substring(0, 4));
                        injection.setCompanyprojects_id(pjExternalInjectionVo.getCompanyprojects_id());
                        injection.setCompany(pjExternalInjectionVo.getCompany());
                        List<PjExternalInjection> injectionList = pjExternalInjectionMapper.select(injection);
                        if (injectionList.size() > 0) {
                            pjExternal.setPjexternalinjection_id(injectionList.get(0).getPjexternalinjection_id());
                            pjExternal.preUpdate(tokenModel);
                            pjExternal.setApril(injectionList.get(0).getApril());
                            pjExternal.setMay(injectionList.get(0).getMay());
                            pjExternal.setJune(injectionList.get(0).getJune());
                            pjExternal.setJuly(injectionList.get(0).getJuly());
                            pjExternal.setAugust(injectionList.get(0).getAugust());
                            pjExternal.setSeptember(injectionList.get(0).getSeptember());
                            pjExternal.setOctober(injectionList.get(0).getOctober());
                            pjExternal.setNovember(injectionList.get(0).getNovember());
                            pjExternal.setDecember(injectionList.get(0).getDecember());
                            pjExternal.setJanuary(injectionList.get(0).getJanuary());
                            pjExternal.setFebruary(injectionList.get(0).getFebruary());
                            pjExternal.setMarch(injectionList.get(0).getMarch());
                            if (pjExternalInjectionVo.getMoney() != null) {
                                if (year.substring(4, 6).equals("04")) {
                                    pjExternal.setApril(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("05")) {
                                    pjExternal.setMay(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("06")) {
                                    pjExternal.setJune(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("07")) {
                                    pjExternal.setJuly(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("08")) {
                                    pjExternal.setAugust(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("09")) {
                                    pjExternal.setSeptember(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("10")) {
                                    pjExternal.setOctober(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("11")) {
                                    pjExternal.setNovember(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("12")) {
                                    pjExternal.setDecember(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("01")) {
                                    pjExternal.setJanuary(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("02")) {
                                    pjExternal.setFebruary(pjExternalInjectionVo.getMoney());
                                } else if (year.substring(4, 6).equals("03")) {
                                    pjExternal.setMarch(pjExternalInjectionVo.getMoney());
                                }
                            }

                            if (variousfundsList.size() > 0) {
                                for (Variousfunds various : variousfundsList) {
                                    companyProjects = new CompanyProjects();
                                    companyProjects.setNumbers(various.getPjname().substring(0, 13));
                                    List<CompanyProjects> comList = companyProjectsMapper.select(companyProjects);
                                    if (comList.size() > 0) {
                                        if (comList.get(0).getCompanyprojects_id().equals(pjExternalInjectionVo.getCompanyprojects_id())
                                                && various.getBpclubname().equals(pjExternalInjectionVo.getCompany())) {

                                            if (various.getPlmonthplan().equals("BP013001")) {
                                                pjExternal.setJune(String.valueOf(Integer.valueOf(pjExternal.getJune()) + Integer.valueOf(various.getPayment())));
                                            } else if (various.getPlmonthplan().equals("BP013002")) {
                                                pjExternal.setSeptember(String.valueOf(Integer.valueOf(pjExternal.getSeptember()) + Integer.valueOf(various.getPayment())));
                                            } else if (various.getPlmonthplan().equals("BP013003")) {
                                                pjExternal.setDecember(String.valueOf(Integer.valueOf(pjExternal.getDecember()) + Integer.valueOf(various.getPayment())));
                                            } else if (various.getPlmonthplan().equals("BP013004")) {
                                                pjExternal.setMarch(String.valueOf(Integer.valueOf(pjExternal.getMarch()) + Integer.valueOf(various.getPayment())));
                                            }
                                        }
                                    }
                                }
                            }
                            pjExternal.setTotal(String.valueOf(new BigDecimal(pjExternal.getApril()).add(new BigDecimal(pjExternal.getMay())).add(new BigDecimal(pjExternal.getJune()))
                                    .add(new BigDecimal(pjExternal.getJuly())).add(new BigDecimal(pjExternal.getAugust())).add(new BigDecimal(pjExternal.getSeptember())).add(new BigDecimal(pjExternal.getOctober()))
                                    .add(new BigDecimal(pjExternal.getNovember())).add(new BigDecimal(pjExternal.getDecember())).add(new BigDecimal(pjExternal.getJanuary())).add(new BigDecimal(pjExternal.getFebruary()))
                                    .add(new BigDecimal(pjExternal.getMarch()))));
                            updatepjExternal.add(pjExternal);
                        } else {
                            pjExternal.setTotal(pjExternalInjectionVo.getMoney());
                            pjExternal.preInsert(tokenModel);
                            insertpjExternal.add(pjExternal);
                        }
                    }
                }
                if (updatepjExternal.size() > 0) {
                    pjExternalInjectionMapper.updatepj(updatepjExternal);
                }
                if (insertpjExternal.size() > 0) {
                    pjExternalInjectionMapper.insertpj(insertpjExternal);
                }
            }
            //endregion pj别外注统计定时任务

            //调取外注别部门支出任务
            departmentalService.getExpatureList();

            //调取部门别收支表任务
            departmentAccountService.insert();
        }
    //endregion add_修改定时任务时间、并对下面流程做判断
    }

    @Override
    public List<PjExternalInjectionVo> getTableinfo(String year, String group_id) throws Exception {
        PjExternalInjection pjExternalInjection = new PjExternalInjection();
        pjExternalInjection.setYears(year);
        pjExternalInjection.setGroup_id(group_id);
        List<PjExternalInjection> pjExternalInjectionList = pjExternalInjectionMapper.select(pjExternalInjection);
        if (pjExternalInjectionList.size() > 0) {
            for (PjExternalInjection injection : pjExternalInjectionList) {
                if(com.nt.utils.StringUtils.isBase64Encode(injection.getProject_name())){
                    injection.setProject_name(Base64.decodeStr(injection.getProject_name()));
                }
            }
        }
        TreeMap<String,List<PjExternalInjection>> injectionList =  pjExternalInjectionList.stream().collect(Collectors.groupingBy(PjExternalInjection :: getCompanyprojects_id,TreeMap::new,Collectors.toList()));
        List<PjExternalInjectionVo> returnlist = new ArrayList<>();
        if (injectionList.size() > 0) {
            for (List<PjExternalInjection> value : injectionList.values()) {
                PjExternalInjectionVo pjExternalVo = new PjExternalInjectionVo();
                pjExternalVo.setThemeinfor_id(value.get(0).getThemeinfor_id());
                pjExternalVo.setThemename(value.get(0).getThemename());
                pjExternalVo.setDivide(value.get(0).getDivide());
                pjExternalVo.setToolsorgs(value.get(0).getToolsorgs());
                pjExternalVo.setCompanyprojects_id(value.get(0).getCompanyprojects_id());
                pjExternalVo.setProject_name(value.get(0).getProject_name());
                pjExternalVo.setCompany("-");
                pjExternalVo.setApril("-");
                pjExternalVo.setMay("-");
                pjExternalVo.setJune("-");
                pjExternalVo.setJuly("-");
                pjExternalVo.setAugust("-");
                pjExternalVo.setSeptember("-");
                pjExternalVo.setOctober("-");
                pjExternalVo.setNovember("-");
                pjExternalVo.setDecember("-");
                pjExternalVo.setJanuary("-");
                pjExternalVo.setFebruary("-");
                pjExternalVo.setMarch("-");
                pjExternalVo.setTotal("-");

//                //项目小合计
//                PjExternalInjection pjExternalInjectionC = new PjExternalInjection();
//                BigDecimal AprilC = new BigDecimal(0.00);
//                BigDecimal MayC = new BigDecimal(0.00);
//                BigDecimal JuneC = new BigDecimal(0.00);
//                BigDecimal JulyC = new BigDecimal(0.00);
//                BigDecimal AugustC = new BigDecimal(0.00);
//                BigDecimal SeptemberC = new BigDecimal(0.00);
//                BigDecimal OctoberC = new BigDecimal(0.00);
//                BigDecimal NovemberC = new BigDecimal(0.00);
//                BigDecimal DecemberC = new BigDecimal(0.00);
//                BigDecimal JanuaryC = new BigDecimal(0.00);
//                BigDecimal FebruaryC = new BigDecimal(0.00);
//                BigDecimal MarchC = new BigDecimal(0.00);
//                BigDecimal TotalC = new BigDecimal(0.00);
//                if (value.size() > 0) {
//                    for (PjExternalInjection injectionC : value) {
//                        AprilC = AprilC.add(new BigDecimal(injectionC.getApril()));
//                        MayC = MayC.add(new BigDecimal(injectionC.getMay()));
//                        JuneC = JuneC.add(new BigDecimal(injectionC.getJune()));
//                        JulyC = JulyC.add(new BigDecimal(injectionC.getJuly()));
//                        AugustC = AugustC.add(new BigDecimal(injectionC.getAugust()));
//                        SeptemberC = SeptemberC.add(new BigDecimal(injectionC.getSeptember()));
//                        OctoberC = OctoberC.add(new BigDecimal(injectionC.getOctober()));
//                        NovemberC = NovemberC.add(new BigDecimal(injectionC.getNovember()));
//                        DecemberC = DecemberC.add(new BigDecimal(injectionC.getDecember()));
//                        JanuaryC = JanuaryC.add(new BigDecimal(injectionC.getJanuary()));
//                        FebruaryC = FebruaryC.add(new BigDecimal(injectionC.getFebruary()));
//                        MarchC = MarchC.add(new BigDecimal(injectionC.getMarch()));
//                        TotalC = TotalC.add(new BigDecimal(injectionC.getTotal()));
//                    }
//                }
//                pjExternalInjectionC.setCompanyprojects_id(value.get(0).getCompanyprojects_id());
//                pjExternalInjectionC.setProject_name("小计");
//                pjExternalInjectionC.setApril(String.valueOf(AprilC));
//                pjExternalInjectionC.setMay(String.valueOf(MayC));
//                pjExternalInjectionC.setJune(String.valueOf(JuneC));
//                pjExternalInjectionC.setJuly(String.valueOf(JulyC));
//                pjExternalInjectionC.setAugust(String.valueOf(AugustC));
//                pjExternalInjectionC.setSeptember(String.valueOf(SeptemberC));
//                pjExternalInjectionC.setOctober(String.valueOf(OctoberC));
//                pjExternalInjectionC.setNovember(String.valueOf(NovemberC));
//                pjExternalInjectionC.setDecember(String.valueOf(DecemberC));
//                pjExternalInjectionC.setJanuary(String.valueOf(JanuaryC));
//                pjExternalInjectionC.setFebruary(String.valueOf(FebruaryC));
//                pjExternalInjectionC.setMarch(String.valueOf(MarchC));
//                pjExternalInjectionC.setTotal(String.valueOf(TotalC));
//                value.add(pjExternalInjectionC);
                pjExternalVo.setPjExternalInjectionList(value);
                returnlist.add(pjExternalVo);
            }
        }

        TreeMap<String,List<PjExternalInjectionVo>> pjexList =  returnlist.stream().collect(Collectors.groupingBy(PjExternalInjectionVo :: getThemeinfor_id,TreeMap::new,Collectors.toList()));
        List<PjExternalInjectionVo> returnlist1 = new ArrayList<>();
        //theme总计
        PjExternalInjectionVo pjExternalInjectionVoZ = new PjExternalInjectionVo();
        BigDecimal AprilZ = new BigDecimal(0.00);
        BigDecimal MayZ = new BigDecimal(0.00);
        BigDecimal JuneZ = new BigDecimal(0.00);
        BigDecimal JulyZ = new BigDecimal(0.00);
        BigDecimal AugustZ = new BigDecimal(0.00);
        BigDecimal SeptemberZ = new BigDecimal(0.00);
        BigDecimal OctoberZ = new BigDecimal(0.00);
        BigDecimal NovemberZ = new BigDecimal(0.00);
        BigDecimal DecemberZ = new BigDecimal(0.00);
        BigDecimal JanuaryZ = new BigDecimal(0.00);
        BigDecimal FebruaryZ = new BigDecimal(0.00);
        BigDecimal MarchZ = new BigDecimal(0.00);
        BigDecimal TotalZ = new BigDecimal(0.00);

        if (pjexList.size() > 0) {
            for (List<PjExternalInjectionVo> value : pjexList.values()) {
                PjExternalInjectionVo pjExternalVo1 = new PjExternalInjectionVo();
                pjExternalVo1.setThemeinfor_id(value.get(0).getThemeinfor_id());
                pjExternalVo1.setThemename(value.get(0).getThemename());
                pjExternalVo1.setDivide(value.get(0).getDivide());
                pjExternalVo1.setToolsorgs(value.get(0).getToolsorgs());
                pjExternalVo1.setCompanyprojects_id("-");
                pjExternalVo1.setProject_name("-");
                pjExternalVo1.setCompany("-");
                pjExternalVo1.setApril("-");
                pjExternalVo1.setMay("-");
                pjExternalVo1.setJune("-");
                pjExternalVo1.setJuly("-");
                pjExternalVo1.setAugust("-");
                pjExternalVo1.setSeptember("-");
                pjExternalVo1.setOctober("-");
                pjExternalVo1.setNovember("-");
                pjExternalVo1.setDecember("-");
                pjExternalVo1.setJanuary("-");
                pjExternalVo1.setFebruary("-");
                pjExternalVo1.setMarch("-");
                pjExternalVo1.setTotal("-");

                //theme合计
                PjExternalInjectionVo pjExternalInjectionVoT = new PjExternalInjectionVo();
                BigDecimal AprilT = new BigDecimal(0.00);
                BigDecimal MayT = new BigDecimal(0.00);
                BigDecimal JuneT = new BigDecimal(0.00);
                BigDecimal JulyT = new BigDecimal(0.00);
                BigDecimal AugustT = new BigDecimal(0.00);
                BigDecimal SeptemberT = new BigDecimal(0.00);
                BigDecimal OctoberT = new BigDecimal(0.00);
                BigDecimal NovemberT = new BigDecimal(0.00);
                BigDecimal DecemberT = new BigDecimal(0.00);
                BigDecimal JanuaryT = new BigDecimal(0.00);
                BigDecimal FebruaryT = new BigDecimal(0.00);
                BigDecimal MarchT = new BigDecimal(0.00);
                BigDecimal TotalT = new BigDecimal(0.00);
                for (PjExternalInjectionVo injectionVo : value) {
                    for (PjExternalInjection injectionT : injectionVo.getPjExternalInjectionList()) {
                        AprilT = AprilT.add(new BigDecimal(injectionT.getApril()));
                        MayT = MayT.add(new BigDecimal(injectionT.getMay()));
                        JuneT = JuneT.add(new BigDecimal(injectionT.getJune()));
                        JulyT = JulyT.add(new BigDecimal(injectionT.getJuly()));
                        AugustT = AugustT.add(new BigDecimal(injectionT.getAugust()));
                        SeptemberT = SeptemberT.add(new BigDecimal(injectionT.getSeptember()));
                        OctoberT = OctoberT.add(new BigDecimal(injectionT.getOctober()));
                        NovemberT = NovemberT.add(new BigDecimal(injectionT.getNovember()));
                        DecemberT = DecemberT.add(new BigDecimal(injectionT.getDecember()));
                        JanuaryT = JanuaryT.add(new BigDecimal(injectionT.getJanuary()));
                        FebruaryT = FebruaryT.add(new BigDecimal(injectionT.getFebruary()));
                        MarchT = MarchT.add(new BigDecimal(injectionT.getMarch()));
                        TotalT = TotalT.add(new BigDecimal(injectionT.getTotal()));

                        AprilZ = AprilZ.add(new BigDecimal(injectionT.getApril()));
                        MayZ = MayZ.add(new BigDecimal(injectionT.getMay()));
                        JuneZ = JuneZ.add(new BigDecimal(injectionT.getJune()));
                        JulyZ = JulyZ.add(new BigDecimal(injectionT.getJuly()));
                        AugustZ = AugustZ.add(new BigDecimal(injectionT.getAugust()));
                        SeptemberZ = SeptemberZ.add(new BigDecimal(injectionT.getSeptember()));
                        OctoberZ = OctoberZ.add(new BigDecimal(injectionT.getOctober()));
                        NovemberZ = NovemberZ.add(new BigDecimal(injectionT.getNovember()));
                        DecemberZ = DecemberZ.add(new BigDecimal(injectionT.getDecember()));
                        JanuaryZ = JanuaryZ.add(new BigDecimal(injectionT.getJanuary()));
                        FebruaryZ = FebruaryZ.add(new BigDecimal(injectionT.getFebruary()));
                        MarchZ = MarchZ.add(new BigDecimal(injectionT.getMarch()));
                        TotalZ = TotalZ.add(new BigDecimal(injectionT.getTotal()));
                    }
                }
                pjExternalInjectionVoT.setCompanyprojects_id(value.get(0).getCompanyprojects_id());
                pjExternalInjectionVoT.setThemename("小计");
                pjExternalInjectionVoT.setApril(String.valueOf(AprilT));
                pjExternalInjectionVoT.setMay(String.valueOf(MayT));
                pjExternalInjectionVoT.setJune(String.valueOf(JuneT));
                pjExternalInjectionVoT.setJuly(String.valueOf(JulyT));
                pjExternalInjectionVoT.setAugust(String.valueOf(AugustT));
                pjExternalInjectionVoT.setSeptember(String.valueOf(SeptemberT));
                pjExternalInjectionVoT.setOctober(String.valueOf(OctoberT));
                pjExternalInjectionVoT.setNovember(String.valueOf(NovemberT));
                pjExternalInjectionVoT.setDecember(String.valueOf(DecemberT));
                pjExternalInjectionVoT.setJanuary(String.valueOf(JanuaryT));
                pjExternalInjectionVoT.setFebruary(String.valueOf(FebruaryT));
                pjExternalInjectionVoT.setMarch(String.valueOf(MarchT));
                pjExternalInjectionVoT.setTotal(String.valueOf(TotalT));
                value.add(pjExternalInjectionVoT);

                pjExternalVo1.setPjExternalInjectionListVo(value);
                returnlist1.add(pjExternalVo1);
            }
        }

        if (returnlist1.size() > 0) {
            pjExternalInjectionVoZ.setThemename("合计");
            pjExternalInjectionVoZ.setApril(String.valueOf(AprilZ));
            pjExternalInjectionVoZ.setMay(String.valueOf(MayZ));
            pjExternalInjectionVoZ.setJune(String.valueOf(JuneZ));
            pjExternalInjectionVoZ.setJuly(String.valueOf(JulyZ));
            pjExternalInjectionVoZ.setAugust(String.valueOf(AugustZ));
            pjExternalInjectionVoZ.setSeptember(String.valueOf(SeptemberZ));
            pjExternalInjectionVoZ.setOctober(String.valueOf(OctoberZ));
            pjExternalInjectionVoZ.setNovember(String.valueOf(NovemberZ));
            pjExternalInjectionVoZ.setDecember(String.valueOf(DecemberZ));
            pjExternalInjectionVoZ.setJanuary(String.valueOf(JanuaryZ));
            pjExternalInjectionVoZ.setFebruary(String.valueOf(FebruaryZ));
            pjExternalInjectionVoZ.setMarch(String.valueOf(MarchZ));
            pjExternalInjectionVoZ.setTotal(String.valueOf(TotalZ));
            returnlist1.add(pjExternalInjectionVoZ);
        }

        return returnlist1;
    }

    @Override
    public Object getTableinfoReport(String year, String group_id) throws Exception {
        PjExternalInjection pjExternalInjection = new PjExternalInjection();
        pjExternalInjection.setYears(year);
        pjExternalInjection.setGroup_id(group_id);
        List<PjExternalInjection> pjExternalInjectionList = pjExternalInjectionMapper.select(pjExternalInjection);
        if (pjExternalInjectionList.size() > 0) {
            for (PjExternalInjection injection : pjExternalInjectionList) {
                if(com.nt.utils.StringUtils.isBase64Encode(injection.getProject_name())){
                    injection.setProject_name(Base64.decodeStr(injection.getProject_name()));
                }
            }
        }
        return JSONObject.toJSON(pjExternalInjectionList);
    }
}




