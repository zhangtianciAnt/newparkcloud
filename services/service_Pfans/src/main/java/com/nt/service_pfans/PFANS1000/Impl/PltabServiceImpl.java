package com.nt.service_pfans.PFANS1000.Impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONArray;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.ProjectIncomeVo2;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS8000.MonthlyRate;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.LoanApplicationService;
import com.nt.service_pfans.PFANS1000.PltabService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectContractMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectsystemMapper;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class PltabServiceImpl implements PltabService {
    @Autowired
    private ProjectIncomeMapper projectincomemapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private CostCarryForwardMapper costcarryforwardmapper;

    @Autowired
    private ProjectContractMapper projectcontractmapper;

    @Autowired
    private ThemeInforMapper themeinformapper;
    @Autowired
    private PltabMapper pltabMapper;

    @Autowired
    private ExpatriatesinforMapper expatriatesinformapper;
    @Autowired
    private ProjectsystemMapper projectsystemmapper;

    @Autowired
    private CompanyProjectsMapper companyprojectsmapper;

    @Override
    public List<CostCarryForward> list() throws Exception {
        List<CostCarryForward> costcarryforwardlist = costcarryforwardmapper.selectAll();
        List<CostCarryForward> list = costcarryforwardlist.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getYear() + ";" + o.getRegion()))), ArrayList::new)
        );
        return list;
    }

    @Override
    public void inset(List<CostCarryForward> costcarryforward, TokenModel tokenModel) throws Exception {
        if (costcarryforward.size() > 0) {
            for (CostCarryForward cost : costcarryforward) {
                if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(cost.getCostcarryforward_id())) {
                    cost.preUpdate(tokenModel);
                    costcarryforwardmapper.updateByPrimaryKey(cost);
                } else {
                    cost.preInsert(tokenModel);
                    cost.setCostcarryforward_id(UUID.randomUUID().toString());
                    costcarryforwardmapper.insertSelective(cost);
                }
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
    public List<CostCarryForward> getCostLast(String groupid, String year, String month) throws Exception {
        CostCarryForward costcarry = new CostCarryForward();
        costcarry.setYear(year);
        costcarry.setRegion(month);
        costcarry.setGroup_id(groupid);
        List<CostCarryForward> costcarryforwardlist = costcarryforwardmapper.select(costcarry);
        return costcarryforwardlist;
    }

    @Override
    public List<CostCarryForward> selectPl(String groupid, String year, String month) throws Exception {
        CostCarryForward costcarry = new CostCarryForward();
        costcarry.setYear(year);
        costcarry.setRegion(month);
        costcarry.setGroup_id(groupid);
        List<CostCarryForward> costcarryforwardlist = costcarryforwardmapper.select(costcarry);
        if (costcarryforwardlist.size() > 0) {
            return costcarryforwardlist;
        }
        List<CostCarryForward> CostCarryList = new ArrayList<>();
        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        ProjectIncome projectincome = new ProjectIncome();
        projectincome.setGroup_id(groupid);
        projectincome.setYear(year);
        projectincome.setMonth(month);
        List<ProjectIncome> projectincomelist = projectincomemapper.select(projectincome);
        if (projectincomelist.size() > 0) {
            BigDecimal twocost = new BigDecimal("0.00");
            BigDecimal rent = new BigDecimal("0.00");
            BigDecimal leasecost = new BigDecimal("0.00");
            BigDecimal temporaryrent = new BigDecimal("0.00");
            BigDecimal other = new BigDecimal("0.00");
            BigDecimal yuanqincost = new BigDecimal("0.00");
            BigDecimal researchcost = new BigDecimal("0.00");
            BigDecimal travalcost = new BigDecimal("0.00");
            BigDecimal concost = new BigDecimal("0.00");
            BigDecimal callcost = new BigDecimal("0.00");
            BigDecimal threefree = new BigDecimal("0.00");
            BigDecimal commonfee = new BigDecimal("0.00");
            BigDecimal brandcost = new BigDecimal("0.00");
            BigDecimal otherexpenses = new BigDecimal("0.00");
            BigDecimal depreciationsoft = new BigDecimal("0.00");
            BigDecimal depreciationequipment = new BigDecimal("0.00");
            BigDecimal gnwzstarttime = new BigDecimal("0.00");
            BigDecimal gwwzstarttime = new BigDecimal("0.00");
            BigDecimal snwzstarttime = new BigDecimal("0.00");
            BigDecimal gnwzstarttimeonly = new BigDecimal("0.00");
            BigDecimal gwwzstarttimeonly = new BigDecimal("0.00");
            BigDecimal snwzstarttimeonly = new BigDecimal("0.00");
            JSONArray jsonArray = JSONArray.parseArray(projectincomelist.get(0).getProjectincomevo1());
            JSONArray jsonArray2 = JSONArray.parseArray(projectincomelist.get(0).getProjectincomevo2());
            JSONArray jsonArray3 = JSONArray.parseArray(projectincomelist.get(0).getProjectincomevo5());
            int operationformtype1 = 0;
            int operationformtype2 = 0;
            int operationformtype3 = 0;
            ArrayList<String> gnwz = new ArrayList<>();
            ArrayList<String> gwwz = new ArrayList<>();
            ArrayList<String> snwz = new ArrayList<>();
            for (Object ob : jsonArray2) {
                String name = getProperty(ob, "name");
                String money = getProperty(ob, "money");
                String type = getProperty(ob, "type");
                String id = getProperty(ob, "nameid");
                if (money == null) {
                    money = "0.00";
                }
                if (type.equals("0")) {
                    Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                    expatriatesinfor.setExpatriatesinfor_id(id);
                    Expatriatesinfor expatriatesinfortype = expatriatesinformapper.selectByPrimaryKey(expatriatesinfor);
                    if (expatriatesinfortype.getOperationform().equals("BP024001")) {
                        gnwz.add(id);
                        operationformtype1++;
                    } else if (expatriatesinfortype.getOperationform().equals("BP024002")) {
                        gwwz.add(id);
                        operationformtype2++;
                    }
                } else if (type.equals("1")) {
                    snwz.add(id);
                    operationformtype3++;
                }
                if (name != null) {
                    if (name.equals("厚生費")) {
                        twocost = new BigDecimal(money);
                    } else if (name.equals("オフィス家賃")) {
                        rent = new BigDecimal(money);
                    } else if (name.equals("リース費")) {
                        leasecost = new BigDecimal(money);
                    } else if (name.equals("出向者賃借料")) {
                        temporaryrent = new BigDecimal(money);
                    } else if (name.equals("その他(固定費)")) {
                        other = new BigDecimal(money);
                    } else if (name.equals("研究材料費")) {
                        researchcost = new BigDecimal(money);
                    } else if (name.equals("原動費")) {
                        yuanqincost = new BigDecimal(money);
                    } else if (name.equals("旅費交通費")) {
                        travalcost = new BigDecimal(money);
                    } else if (name.equals("通信費")) {
                        concost = new BigDecimal(money);
                    } else if (name.equals("消耗品費")) {
                        callcost = new BigDecimal(money);
                    } else if (name.equals("会議費/交際費/研修費")) {
                        threefree = new BigDecimal(money);
                    } else if (name.equals("共同事務費")) {
                        commonfee = new BigDecimal(money);
                    } else if (name.equals("ブランド使用料")) {
                        brandcost = new BigDecimal(money);
                    } else if (name.equals("その他経費")) {
                        otherexpenses = new BigDecimal(money);
                    } else if (name.equals("減価償却費（設備）")) {
                        depreciationsoft = new BigDecimal(money);
                    } else if (name.equals("減価償却費（ソフト）")) {
                        depreciationequipment = new BigDecimal(money);
                    }
                }
            }
            List<Object> object = new ArrayList<>();
            for (Object obsum : jsonArray3) {
                String type = getProperty(obsum, "type");
                if (type.equals("3")) {
                    object.add(obsum);
                }
            }
            Object o1 = object.get(0);
            Object o2 = object.get(1);
            Object o3 = object.get(2);
            int b = -1;
            BigDecimal project = new BigDecimal("0");
            for (Object ob : jsonArray) {
                String id = getProperty(ob, "companyprojectid");
                Projectsystem projectsystem = new Projectsystem();
                projectsystem.setCompanyprojects_id(id);
                projectsystem.setType("0");
                List<Projectsystem> projectsystemlist = projectsystemmapper.select(projectsystem);
                if (projectsystemlist.size() > 0) {
                    BigDecimal projects = new BigDecimal(projectsystemlist.size());
                    project = project.add(projects).setScale(scale, roundingMode);
                }
                for (int j = 0; j < gnwz.size(); j++) {
                    List<LogManagement> starttime = pltabMapper.getstarttimesum(id, gnwz.get(j));
                    BigDecimal timestart = new BigDecimal(starttime.get(0).getTime_start());
                    gnwzstarttimeonly = gnwzstarttimeonly.add(timestart).setScale(scale, roundingMode);
                }
                for (int m = 0; m < gwwz.size(); m++) {
                    List<LogManagement> starttime = pltabMapper.getstarttimesum(id, gwwz.get(m));
                    BigDecimal timestart = new BigDecimal(starttime.get(0).getTime_start());
                    gwwzstarttimeonly = gwwzstarttimeonly.add(timestart).setScale(scale, roundingMode);
                }
                for (int n = 0; n < snwz.size(); n++) {
                    List<LogManagement> starttime = pltabMapper.getstarttimesum(id, snwz.get(n));
                    BigDecimal timestart = new BigDecimal(starttime.get(0).getTime_start());
                    snwzstarttimeonly = snwzstarttimeonly.add(timestart).setScale(scale, roundingMode);
                }
            }
            for (Object ob : jsonArray) {
                b++;
                String costr = getProperty(o1, "cost" + b);
                String costw = getProperty(o2, "cost" + b);
                String costg = getProperty(o3, "cost" + b);
                CostCarryForward costcarryforward = new CostCarryForward();
                String name = getProperty(ob, "companyproject");
                String id = getProperty(ob, "companyprojectid");
                String theme = getProperty(ob, "theme");
                String type = getProperty(ob, "type");
                ProjectContract projectcontract = new ProjectContract();
                projectcontract.setCompanyprojects_id(id);
                projectcontract.setTheme(theme);
                List<ProjectContract> projectcontractlist = projectcontractmapper.select(projectcontract);
                Projectsystem projectsystem = new Projectsystem();
                projectsystem.setCompanyprojects_id(id);
                projectsystem.setType("0");
                List<Projectsystem> projectsystemlist = projectsystemmapper.select(projectsystem);
                BigDecimal contractamount = new BigDecimal("0");
                for (ProjectContract list : projectcontractlist) {
                    BigDecimal contractamounts = new BigDecimal(list.getContractamount());
                    contractamount = contractamount.add(contractamounts).setScale(scale, roundingMode);
                }


                for (int j = 0; j < gnwz.size(); j++) {
                    List<LogManagement> starttime = pltabMapper.getstarttimesum(id, gnwz.get(j));
                    BigDecimal timestart = new BigDecimal(starttime.get(0).getTime_start());
                    gnwzstarttime = gnwzstarttime.add(timestart).setScale(scale, roundingMode);
                }
                for (int m = 0; m < gwwz.size(); m++) {
                    List<LogManagement> starttime = pltabMapper.getstarttimesum(id, gwwz.get(m));
                    BigDecimal timestart = new BigDecimal(starttime.get(0).getTime_start());
                    gwwzstarttime = gwwzstarttime.add(timestart).setScale(scale, roundingMode);

                }
                for (int n = 0; n < snwz.size(); n++) {
                    List<LogManagement> starttime = pltabMapper.getstarttimesum(id, snwz.get(n));
                    BigDecimal timestart = new BigDecimal(starttime.get(0).getTime_start());
                    snwzstarttime = snwzstarttime.add(timestart).setScale(scale, roundingMode);
                }

                ThemeInfor themeinfor = new ThemeInfor();
                themeinfor.setThemename(theme);
                themeinfor.setGroupid(groupid);
                themeinfor.setYear(year);
                List<ThemeInfor> themeinforlist = themeinformapper.select(themeinfor);
                costcarryforward.setPj(name);
                if (themeinforlist.size() > 0) {
                    int i = 0;
                    if (themeinforlist.get(0).getContract().equals("PJ142001")) {
                        i = 1;
                    } else if (themeinforlist.get(0).getContract().equals("PJ142002")) {
                        i = 2;
                    } else if (themeinforlist.get(0).getContract().equals("PJ142003")) {
                        i = 3;
                    } else {
                        i = 4;
                    }
                    if (i == 1) {
                        costcarryforward.setOutst1(String.valueOf(contractamount));
                        costcarryforward.setTaxyw("0.00");
                        costcarryforward.setTaxsa("0.00");
                        costcarryforward.setInst("0.00");
                        costcarryforward.setCenterintotal(String.valueOf(contractamount));
                        costcarryforward.setIntotal(String.valueOf(contractamount));
                    } else if (i == 2) {
                        costcarryforward.setOutst1("0.00");
                        costcarryforward.setTaxyw(String.valueOf(contractamount));
                        costcarryforward.setTaxsa("0.00");
                        costcarryforward.setInst("0.00");
                        costcarryforward.setCenterintotal(String.valueOf(contractamount));
                        BigDecimal tax1 = new BigDecimal("1.06");
                        BigDecimal tax2 = new BigDecimal("0.06");
                        BigDecimal money = contractamount.divide(tax1, scale, roundingMode).multiply(tax2);
                        costcarryforward.setIntotal(String.valueOf(money));
                    } else if (i == 3) {
                        costcarryforward.setOutst1("0.00");
                        costcarryforward.setTaxyw("0.00");
                        costcarryforward.setTaxsa(String.valueOf(contractamount));
                        costcarryforward.setInst("0.00");
                        costcarryforward.setCenterintotal(String.valueOf(contractamount));
                        BigDecimal tax1 = new BigDecimal("1.13");
                        BigDecimal tax2 = new BigDecimal("0.13");
                        BigDecimal money = contractamount.divide(tax1, scale, roundingMode).multiply(tax2);
                        costcarryforward.setIntotal(String.valueOf(money));
                    } else if (i == 4) {
                        costcarryforward.setOutst1("0.00");
                        costcarryforward.setTaxyw("0.00");
                        costcarryforward.setTaxsa("0.00");
                        costcarryforward.setInst(String.valueOf(contractamount));
                        costcarryforward.setCenterintotal(String.valueOf(contractamount));
                        costcarryforward.setIntotal("0.00");
                    }
                } else {
                    costcarryforward.setOutst1("0.00");
                    costcarryforward.setTaxyw("0.00");
                    costcarryforward.setTaxsa("0.00");
                    costcarryforward.setInst("0.00");
                    costcarryforward.setCenterintotal("0.00");
                    costcarryforward.setIntotal("0.00");
                }
                costcarryforward.setPeocost(costr);
                costcarryforward.setTwocost(String.valueOf(twocost));
                BigDecimal peocostsum = twocost.add(new BigDecimal(costr));
                costcarryforward.setPeocostsum(String.valueOf(peocostsum));
                costcarryforward.setRent(String.valueOf(rent));
                costcarryforward.setLeasecost(String.valueOf(leasecost));
                costcarryforward.setTemporaryrent(String.valueOf(temporaryrent));
                costcarryforward.setOther(String.valueOf(other));
                costcarryforward.setDepreciationequipment(String.valueOf(depreciationequipment));
                costcarryforward.setDepreciationsoft(String.valueOf(depreciationsoft));
                BigDecimal costsubtotal = temporaryrent.add(leasecost).add(rent).add(other).add(depreciationsoft).add(depreciationequipment);
                costcarryforward.setCostsubtotal(String.valueOf(costsubtotal));
                BigDecimal outsourcingpjhourssn = new BigDecimal(operationformtype3);
                BigDecimal outsourcingpjhoursw = new BigDecimal(operationformtype2);
                BigDecimal outsourcingpjhoursn = new BigDecimal(operationformtype1);
                costcarryforward.setOutsourcinghours(String.valueOf(gwwzstarttime));
                costcarryforward.setOutsourcingname(String.valueOf(gnwzstarttime));
                costcarryforward.setEmployeename(String.valueOf(projectsystemlist.size()));
                BigDecimal outsourcingpjhours1 = new BigDecimal("160");
                BigDecimal outsourcingpjhours4 = gwwzstarttime.add(gnwzstarttime);
                BigDecimal outsourcingpjhoursout = outsourcingpjhours4.divide(outsourcingpjhours1, scale, roundingMode);
                costcarryforward.setOutsourcingpjhours(String.valueOf(outsourcingpjhoursout));
                costcarryforward.setOutsourcing(String.valueOf(outsourcingpjhoursout));
                BigDecimal outsourcing1 = new BigDecimal(projectsystemlist.size());
                BigDecimal outsourcingout = outsourcing1.divide(outsourcingpjhours1, scale, roundingMode);
                costcarryforward.setEmhours(String.valueOf(outsourcingout));
                costcarryforward.setEmployeeuptime(String.valueOf(outsourcingout));
                if (!outsourcingpjhours4.equals(new BigDecimal("0.00"))) {
                    BigDecimal externalpjrate = outsourcingpjhoursout.divide(outsourcingpjhours4, scale, roundingMode);
                    costcarryforward.setExternalpjrate(String.valueOf(externalpjrate));
                    costcarryforward.setExternalinjectionrate(String.valueOf(externalpjrate));
                } else {
                    costcarryforward.setExternalpjrate("0.00");
                    costcarryforward.setExternalinjectionrate("0.00");
                }

                if (!outsourcing1.equals(new BigDecimal("0"))) {
                    BigDecimal memberpjrate = outsourcingout.divide(outsourcing1, scale, roundingMode);
                    costcarryforward.setMemberpjrate(String.valueOf(memberpjrate));
                    costcarryforward.setMembershiprate(String.valueOf(memberpjrate));
                } else {
                    costcarryforward.setMemberpjrate("0.00");
                    costcarryforward.setMembershiprate("0.00");
                }

                BigDecimal pjrateemployees1 = outsourcingpjhoursout.add(outsourcingout);
                BigDecimal pjrateemployees2 = outsourcingpjhours4.add(outsourcing1);
                if (!pjrateemployees2.equals(new BigDecimal("0.00"))) {
                    BigDecimal pjrateemployeesout = pjrateemployees1.divide(pjrateemployees2, scale, roundingMode);
                    costcarryforward.setPjrateemployees(String.valueOf(pjrateemployeesout));
                    costcarryforward.setStaffingrate(String.valueOf(pjrateemployeesout));
                } else {
                    costcarryforward.setPjrateemployees("0.00");
                    costcarryforward.setStaffingrate("0.00");
                }

                costcarryforward.setResearchcost(String.valueOf(researchcost));
                costcarryforward.setSurveyfee("---");
                CompanyProjects companyprojects = new CompanyProjects();
                companyprojects.setCompanyprojects_id(id);
                CompanyProjects company = companyprojectsmapper.selectByPrimaryKey(companyprojects);
                if (company != null) {
                    if (company.getToolstype().equals("1")) {
                        BigDecimal inwetuos = new BigDecimal(costr).add(new BigDecimal(costw)).add(new BigDecimal(costg));
                        if (!project.equals(new BigDecimal("0.00"))) {
                            BigDecimal inwetuo = outsourcing1.divide(project, scale, roundingMode).multiply(inwetuos);
                            costcarryforward.setInwetuo(String.valueOf(inwetuo));
                        } else {
                            costcarryforward.setInwetuo("0.00");
                        }
                    } else {
                        costcarryforward.setInwetuo("0.00");
                    }
                } else {
                    costcarryforward.setInwetuo("0.00");
                }
                costcarryforward.setOutcost(costw);
                costcarryforward.setOthersoftwarefree(costcarryforward.getInst());
                BigDecimal departmenttotal = researchcost.add(new BigDecimal(costcarryforward.getInwetuo())).add(new BigDecimal(costw)).add(new BigDecimal(costcarryforward.getInst()));
                costcarryforward.setDepartmenttotal(String.valueOf(departmenttotal));

                BigDecimal gn = new BigDecimal("0");
                BigDecimal gw = new BigDecimal("0");
                BigDecimal sn = new BigDecimal("0");
                List<com.nt.dao_Org.Dictionary> curListA = dictionaryService.getForSelect("CW001");
                for (Dictionary iteA : curListA) {
                    if (iteA.getCode().equals("CW001001")) {
                        sn = new BigDecimal(iteA.getValue2());
                    } else if (iteA.getCode().equals("CW001002")) {
                        gn = new BigDecimal(iteA.getValue2());
                    } else if (iteA.getCode().equals("CW001003")) {
                        gw = new BigDecimal(iteA.getValue2());
                    }
                }
                BigDecimal expensessubtotal1 = new BigDecimal("0");
                BigDecimal expensessubtotal2 = new BigDecimal("0");
                BigDecimal expensessubtotal3 = new BigDecimal("0");
                if (!gnwzstarttime.equals(new BigDecimal("0.00"))) {
                    expensessubtotal1 = gnwzstarttimeonly.divide(gnwzstarttime, scale, roundingMode).multiply(outsourcingpjhoursn).multiply(gn);
                }
                if (!gwwzstarttime.equals(new BigDecimal("0.00"))) {
                    expensessubtotal2 = gwwzstarttimeonly.divide(gwwzstarttime, scale, roundingMode).multiply(outsourcingpjhoursw).multiply(gw);
                }
                if (!snwzstarttime.equals(new BigDecimal("0.00"))) {
                    expensessubtotal3 = snwzstarttimeonly.divide(snwzstarttime, scale, roundingMode).multiply(outsourcingpjhourssn).multiply(sn);
                }
                BigDecimal expensessubtotalout = expensessubtotal1.add(expensessubtotal2).add(expensessubtotal3);
                costcarryforward.setExpensessubtotal(String.valueOf(expensessubtotalout));
                costcarryforward.setTransferone("---");
                costcarryforward.setTransfertwo("---");
                costcarryforward.setAllocationsum(String.valueOf(expensessubtotalout));
                costcarryforward.setYuanqincost(String.valueOf(yuanqincost));
                costcarryforward.setTravalcost(String.valueOf(travalcost));
                costcarryforward.setConcost(String.valueOf(concost));
                costcarryforward.setCallcost(String.valueOf(callcost));
                costcarryforward.setThreefree(String.valueOf(threefree));
                costcarryforward.setCommonfee(String.valueOf(commonfee));
                costcarryforward.setBrandcost(String.valueOf(brandcost));
                costcarryforward.setOtherexpenses(String.valueOf(otherexpenses));
                if (!costcarryforward.getIntotal().equals("0.00")) {
                    BigDecimal benefitrate = new BigDecimal(costcarryforward.getIntotal()).subtract(new BigDecimal(costr)).subtract(new BigDecimal(costw)).subtract(new BigDecimal(costcarryforward.getTravalcost())).subtract(new BigDecimal(costcarryforward.getResearchcost()));
                    BigDecimal benefitrates = benefitrate.divide(new BigDecimal(costcarryforward.getIntotal()), scale, roundingMode);
                    costcarryforward.setBenefitrate(String.valueOf(benefitrates));
                } else {
                    costcarryforward.setBenefitrate("---");
                }
                if (type == null) {
                    type = "1";
                }
                if (type.equals("0")) {
                    BigDecimal process = new BigDecimal(costr).add(new BigDecimal(costw)).add(new BigDecimal(costg));
                    costcarryforward.setProcess(String.valueOf(process));
                } else {
                    BigDecimal process1 = new BigDecimal(costr).add(new BigDecimal(costw)).add(new BigDecimal(costg));
                    BigDecimal process = new BigDecimal("0");
                    if (costcarryforward.getBenefitrate().equals("---")) {
                        process = process1;
                    } else {
                        BigDecimal process2 = new BigDecimal("1").subtract(new BigDecimal(costcarryforward.getBenefitrate()));
                        process = process1.divide(process2, scale, roundingMode);
                    }
                    costcarryforward.setProcess(String.valueOf(process));
                }

                BigDecimal totalpro = new BigDecimal(costr).add(new BigDecimal(costw)).add(new BigDecimal(costg)).subtract(new BigDecimal(costcarryforward.getProcess()));
                costcarryforward.setTotalpro(String.valueOf(totalpro));
                costcarryforward.setOtherincome("0.00");
                BigDecimal otherexpentotal = travalcost.add(totalpro).add(new BigDecimal(costcarryforward.getProcess()));
                costcarryforward.setOtherexpentotal(String.valueOf(otherexpentotal));
                BigDecimal costtotal = peocostsum.add(costsubtotal).add(departmenttotal).add(expensessubtotalout).add(otherexpentotal);
                costcarryforward.setCosttotal(String.valueOf(costtotal));
                BigDecimal operating = new BigDecimal(costcarryforward.getIntotal()).subtract(costtotal);
                costcarryforward.setOperating(String.valueOf(operating));
                costcarryforward.setInterestrate("0.00");
                costcarryforward.setExchanges("0.00");
                costcarryforward.setOperatingprofit("0.00");
                costcarryforward.setPretaxprofit("0.00");
                costcarryforward.setTaxallowance("0.00");
                costcarryforward.setPosttaxbenefit("0.00");
                if (!costcarryforward.getIntotal().equals("0.00")) {
                    BigDecimal operatingmargin = operating.divide(new BigDecimal(costcarryforward.getIntotal()), scale, roundingMode);
                    costcarryforward.setOperatingmargin(String.valueOf(operatingmargin));
                } else {
                    costcarryforward.setOperatingmargin("0.00");
                }
                CostCarryList.add(costcarryforward);
            }
        }
        return CostCarryList;
    }

    private String getProperty(Object o, String key) throws Exception {
        try {
            return org.apache.commons.beanutils.BeanUtils.getProperty(o, key);
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
