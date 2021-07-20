package com.nt.service_pfans.PFANS1000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Pfans.PFANS1000.DepartmentAccount;
import com.nt.dao_Pfans.PFANS1000.DepartmentAccountTotal;
import com.nt.dao_Pfans.PFANS1000.ThemeInfor;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentAccountVo;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentTotalVo;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentAccountMapper;
import com.nt.service_pfans.PFANS1000.mapper.ThemeInforMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectContractMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.MarshalledObject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.data.util.Pair.toMap;

@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentAccountServiceImpl implements DepartmentAccountService {

    @Autowired
    private DepartmentAccountMapper departmentAccountMapper;

    @Autowired
    private ThemeInforMapper themeInforMapper;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private ProjectContractMapper projectContractMapper;

    @Autowired
    private DictionaryMapper dictionaryMapper;


    @Override
    public List<DepartmentAccount> selectBygroupid(String year, String department) throws Exception {
        List<DepartmentAccount> departmentAccountList = new ArrayList<>();
        departmentAccountList = departmentAccountMapper.selectALL(year,department);
        return departmentAccountList;
    }

    @Override
    public void insert() throws Exception {
        //确定当前年度
        Calendar calendar = Calendar.getInstance();
        int year = 0;
        int monthlast = calendar.get(Calendar.MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(month >= 1 && month <= 4) {
            //时间大于4月10日的，属于新年度，小于10日，属于旧年度
            if(day >=10)
            {
                year = calendar.get(Calendar.YEAR);
            }
            else
            {
                year = calendar.get(Calendar.YEAR) - 1;
            }
        }
        else
        {
            year = calendar.get(Calendar.YEAR);
        }

        //当前年
        Calendar calnew = Calendar.getInstance();
        int yearnow = calnew.get(Calendar.YEAR);

        List<DepartmentVo> departmentVoList = new ArrayList<>();
        DepartmentVo departmentVo = new DepartmentVo();
        //获取当前系统中有效的部门，按照预算编码统计
        OrgTree orgs = orgTreeService.get(new OrgTree());
        //副总
        for (OrgTree orgfu : orgs.getOrgs()) {
            //Center
            for (OrgTree orgCenter : orgfu.getOrgs()) {
                if(!StringUtils.isNullOrEmpty(orgCenter.getEncoding()))
                {
                    departmentVo = new DepartmentVo();
                    departmentVo.setDepartmentId(orgCenter.get_id());
                    departmentVo.setDepartmentname(orgCenter.getCompanyname());
                    departmentVo.setDepartmentshortname(orgCenter.getCompanyshortname());
                    departmentVo.setDepartmentEncoding(orgCenter.getEncoding());
                    departmentVo.setDepartmentEn(orgCenter.getCompanyen());
                    departmentVo.setDepartmentType(orgCenter.getType());
                    departmentVo.setDepartmentUserid(orgCenter.getUser());
                    departmentVoList.add(departmentVo);
                }
                else
                {
                    for(OrgTree orgGroup : orgCenter.getOrgs())
                    {
                        departmentVo = new DepartmentVo();
                        departmentVo.setDepartmentId(orgGroup.get_id());
                        departmentVo.setDepartmentname(orgGroup.getCompanyname());
                        departmentVo.setDepartmentshortname(orgGroup.getCompanyshortname());
                        departmentVo.setDepartmentEncoding(orgGroup.getEncoding());
                        departmentVo.setDepartmentEn(orgGroup.getCompanyen());
                        departmentVo.setDepartmentType(orgGroup.getType());
                        departmentVo.setDepartmentUserid(orgGroup.getUser());
                        departmentVoList.add(departmentVo);
                    }
                }
            }
        }

        //检索当前年度和组织查询所有的 和合同关联的theme列表，合同列表
        TokenModel tokenModel = new TokenModel();
        if(departmentVoList.size()>0)
        {
            //按照部门和年度检索，保存数据
            for (DepartmentVo dv:departmentVoList) {
                //测试代码
//                if(dv.getDepartmentId().equals("AB58CD3323329599E8C86EB3343844CE1E05"))
//                {
                    //检索当前部门的人件费，字典固定值
                    Double personalmoney = 0d;
                    List<Dictionary> dictionaryList = new ArrayList<>();
                    Dictionary dictionary = new Dictionary();
                    dictionary.setPcode("PJ147");
                    dictionary.setType("JY");
                    dictionary.setValue1(dv.getDepartmentEn());
                    dictionaryList = dictionaryMapper.select(dictionary);
                    if(dictionaryList.size()>0)
                    {
                        personalmoney = Double.valueOf(dictionaryList.get(0).getValue2() == null || dictionaryList.get(0).getValue2() == "" ? "0" :dictionaryList.get(0).getValue2());
                    }
                    //所有合同回数项目列表
                    List<DepartmentAccountVo> departmentAccountVoList = new ArrayList<>();
                    departmentAccountVoList = departmentAccountMapper.selectByYearAndDep(String.valueOf(year),dv.getDepartmentId());

                    if(departmentAccountVoList.size()>0)
                    {
                        List<DepartmentAccount> departmentAccountListInsert = new ArrayList<>();
                        List<DepartmentAccount> departmentAccountListUnpdate = new ArrayList<>();
                        for (DepartmentAccountVo dav:departmentAccountVoList) {

                            //判断一个合同关联的项目数，决定取请求金额，还是分配金额
                            List<ProjectContract> pcList = new ArrayList<>();
                            ProjectContract pc =new ProjectContract();
                            pc.setContract("NSM1811330009");
                            pcList = projectContractMapper.select(pc);
                            //检索集合中按照合同分组，每个合同的重复数
                             List<ProjectContract> pcGroupByList = pcList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProjectContract :: getCompanyprojects_id))), ArrayList::new));

                            //检索数据是否存在，存在更新，不存在创建
                            List<DepartmentAccount> departmentAccountTemp = new ArrayList<>();
                            DepartmentAccount da = new DepartmentAccount();
                            da.setYears(String.valueOf(year));
                            da.setDepartment(dv.getDepartmentId());
                            da.setCompanyprojects_id(dav.getCompanyprojects_id());
                            da.setContractapplication_id(dav.getContractapplication_id());
                            da.setTheme_id(dav.getTheme_id());
                            departmentAccountTemp = departmentAccountMapper.select(da);
                            if(departmentAccountTemp.size()>0)
                            {
                                if(pcGroupByList.size()>1)
                                {
                                    switch (monthlast)
                                    {
                                        case 0:
                                            departmentAccountTemp.get(0).setMoneyactual12(Double.valueOf(dav.getContractamountSum12()));
                                            break;
                                        case 1:
                                            departmentAccountTemp.get(0).setMoneyactual1(Double.valueOf(dav.getContractamountSum1()));
                                            break;
                                        case 2:
                                            departmentAccountTemp.get(0).setMoneyactual2(Double.valueOf(dav.getContractamountSum2()));
                                            break;
                                        case 3:
                                            departmentAccountTemp.get(0).setMoneyactual3(Double.valueOf(dav.getContractamountSum3()));
                                            break;
                                        case 4:
                                            departmentAccountTemp.get(0).setMoneyactual4(Double.valueOf(dav.getContractamountSum4()));
                                            break;
                                        case 5:
                                            departmentAccountTemp.get(0).setMoneyactual5(Double.valueOf(dav.getContractamountSum5()));
                                            break;
                                        case 6:
                                            departmentAccountTemp.get(0).setMoneyactual6(Double.valueOf(dav.getContractamountSum6()));
                                            break;
                                        case 7:
                                            departmentAccountTemp.get(0).setMoneyactual7(Double.valueOf(dav.getContractamountSum7()));
                                            break;
                                        case 8:
                                            departmentAccountTemp.get(0).setMoneyactual8(Double.valueOf(dav.getContractamountSum8()));
                                            break;
                                        case 9:
                                            departmentAccountTemp.get(0).setMoneyactual9(Double.valueOf(dav.getContractamountSum9()));
                                            break;
                                        case 10:
                                            departmentAccountTemp.get(0).setMoneyactual10(Double.valueOf(dav.getContractamountSum10()));
                                            break;
                                        case 11:
                                            departmentAccountTemp.get(0).setMoneyactual11(Double.valueOf(dav.getContractamountSum11()));
                                            break;
                                    }
                                }
                                else
                                {
                                    switch (monthlast)
                                    {
                                        case 0:
                                            departmentAccountTemp.get(0).setMoneyactual12(Double.valueOf(dav.getClaimamountSum12()));
                                            break;
                                        case 1:
                                            departmentAccountTemp.get(0).setMoneyactual1(Double.valueOf(dav.getClaimamountSum1()));
                                            break;
                                        case 2:
                                            departmentAccountTemp.get(0).setMoneyactual2(Double.valueOf(dav.getClaimamountSum2()));
                                            break;
                                        case 3:
                                            departmentAccountTemp.get(0).setMoneyactual3(Double.valueOf(dav.getClaimamountSum3()));
                                            break;
                                        case 4:
                                            departmentAccountTemp.get(0).setMoneyactual4(Double.valueOf(dav.getClaimamountSum4()));
                                            break;
                                        case 5:
                                            departmentAccountTemp.get(0).setMoneyactual5(Double.valueOf(dav.getClaimamountSum5()));
                                            break;
                                        case 6:
                                            departmentAccountTemp.get(0).setMoneyactual6(Double.valueOf(dav.getClaimamountSum6()));
                                            break;
                                        case 7:
                                            departmentAccountTemp.get(0).setMoneyactual7(Double.valueOf(dav.getClaimamountSum7()));
                                            break;
                                        case 8:
                                            departmentAccountTemp.get(0).setMoneyactual8(Double.valueOf(dav.getClaimamountSum8()));
                                            break;
                                        case 9:
                                            departmentAccountTemp.get(0).setMoneyactual9(Double.valueOf(dav.getClaimamountSum9()));
                                            break;
                                        case 10:
                                            departmentAccountTemp.get(0).setMoneyactual10(Double.valueOf(dav.getClaimamountSum10()));
                                            break;
                                        case 11:
                                            departmentAccountTemp.get(0).setMoneyactual11(Double.valueOf(dav.getClaimamountSum11()));
                                            break;
                                    }
                                }
                                // 第一季度4,5,6,
                                departmentAccountTemp.get(0).setTotalactual1q(departmentAccountTemp.get(0).getMoneyactual4()+departmentAccountTemp.get(0).getMoneyactual5()+departmentAccountTemp.get(0).getMoneyactual6());
                                // 第二季度7,8,9
                                departmentAccountTemp.get(0).setTotalactual2q(departmentAccountTemp.get(0).getMoneyactual7()+departmentAccountTemp.get(0).getMoneyactual8()+departmentAccountTemp.get(0).getMoneyactual9());
                                // 第三季度10,11,12
                                departmentAccountTemp.get(0).setTotalactual3q(departmentAccountTemp.get(0).getMoneyactual10()+departmentAccountTemp.get(0).getMoneyactual11()+departmentAccountTemp.get(0).getMoneyactual12());
                                // 第四季度1,2,3
                                departmentAccountTemp.get(0).setTotalactual4q(departmentAccountTemp.get(0).getMoneyactual1()+departmentAccountTemp.get(0).getMoneyactual2()+departmentAccountTemp.get(0).getMoneyactual3());
                                //全年
                                departmentAccountTemp.get(0).setTotalactual(departmentAccountTemp.get(0).getTotalactual1q()+departmentAccountTemp.get(0).getTotalactual2q()+departmentAccountTemp.get(0).getTotalactual3q()+departmentAccountTemp.get(0).getTotalactual4q());
                                departmentAccountTemp.get(0).preUpdate(tokenModel);
                                departmentAccountListUnpdate.add(departmentAccountTemp.get(0));
                            }
                            else
                            {
                                DepartmentAccount deptemp = new DepartmentAccount();
                                deptemp.setYears(String.valueOf(year));
                                deptemp.setDepartment(dv.getDepartmentId());
                                deptemp.setCompanyprojects_id(dav.getCompanyprojects_id());
                                deptemp.setContractapplication_id(dav.getContractapplication_id());
                                deptemp.setTheme_id(dav.getTheme_id());
                                deptemp.setThemename(dav.getThemename());
                                deptemp.setContract(dav.getContract());
                                deptemp.setToolsorgs(dav.getToolsorgs());
                                deptemp.setContractnumber(dav.getContractnumber());
                                deptemp.setClaimamounttotal(dav.getClaimamounttotal());
                                deptemp.setEntrycondition(dav.getEntrycondition());
                                deptemp.setState(dav.getState());
                                deptemp.setNumbers(dav.getNumbers());
                                deptemp.setMoneyplan4(dav.getMoneyplan4());
                                deptemp.setMoneyplan5(dav.getMoneyplan5());
                                deptemp.setMoneyplan6(dav.getMoneyplan6());
                                deptemp.setMoneyplan7(dav.getMoneyplan7());
                                deptemp.setMoneyplan8(dav.getMoneyplan8());
                                deptemp.setMoneyplan9(dav.getMoneyplan9());
                                deptemp.setMoneyplan10(dav.getMoneyplan10());
                                deptemp.setMoneyplan11(dav.getMoneyplan11());
                                deptemp.setMoneyplan12(dav.getMoneyplan12());
                                deptemp.setMoneyplan1(dav.getMoneyplan1());
                                deptemp.setMoneyplan2(dav.getMoneyplan2());
                                deptemp.setMoneyplan3(dav.getMoneyplan3());

                                //判断同一个合同全系统中是否关联多个项目
                                if(pcGroupByList.size()>1)
                                {
                                    //多个项目，取分配金额
                                    deptemp.setAmount(dav.getContractamountSum());
                                    deptemp.setMoneyactual4(Double.valueOf(dav.getContractamountSum4()));
                                    deptemp.setMoneyactual5(Double.valueOf(dav.getContractamountSum5()));
                                    deptemp.setMoneyactual6(Double.valueOf(dav.getContractamountSum6()));
                                    deptemp.setMoneyactual7(Double.valueOf(dav.getContractamountSum7()));
                                    deptemp.setMoneyactual8(Double.valueOf(dav.getContractamountSum8()));
                                    deptemp.setMoneyactual9(Double.valueOf(dav.getContractamountSum9()));
                                    deptemp.setMoneyactual10(Double.valueOf(dav.getContractamountSum10()));
                                    deptemp.setMoneyactual11(Double.valueOf(dav.getContractamountSum11()));
                                    deptemp.setMoneyactual12(Double.valueOf(dav.getContractamountSum12()));
                                    deptemp.setMoneyactual1(Double.valueOf(dav.getContractamountSum1()));
                                    deptemp.setMoneyactual2(Double.valueOf(dav.getContractamountSum2()));
                                    deptemp.setMoneyactual3(Double.valueOf(dav.getContractamountSum3()));
                                }
                                else
                                {
                                    //一个项目，取请求金额
                                    deptemp.setAmount(dav.getClaimamountSum());
                                    deptemp.setMoneyactual4(Double.valueOf(dav.getClaimamountSum4()));
                                    deptemp.setMoneyactual5(Double.valueOf(dav.getClaimamountSum5()));
                                    deptemp.setMoneyactual6(Double.valueOf(dav.getClaimamountSum6()));
                                    deptemp.setMoneyactual7(Double.valueOf(dav.getClaimamountSum7()));
                                    deptemp.setMoneyactual8(Double.valueOf(dav.getClaimamountSum8()));
                                    deptemp.setMoneyactual9(Double.valueOf(dav.getClaimamountSum9()));
                                    deptemp.setMoneyactual10(Double.valueOf(dav.getClaimamountSum10()));
                                    deptemp.setMoneyactual11(Double.valueOf(dav.getClaimamountSum11()));
                                    deptemp.setMoneyactual12(Double.valueOf(dav.getClaimamountSum12()));
                                    deptemp.setMoneyactual1(Double.valueOf(dav.getClaimamountSum1()));
                                    deptemp.setMoneyactual2(Double.valueOf(dav.getClaimamountSum2()));
                                    deptemp.setMoneyactual3(Double.valueOf(dav.getClaimamountSum3()));
                                }
                                deptemp.setDepartment(dv.getDepartmentId());
                                deptemp.setDepartmentaccountid(UUID.randomUUID().toString());
                                // 第一季度4,5,6,
                                deptemp.setTotalactual1q(deptemp.getMoneyactual4()+deptemp.getMoneyactual5()+deptemp.getMoneyactual6());
                                // 第二季度7,8,9
                                deptemp.setTotalactual2q(deptemp.getMoneyactual7()+deptemp.getMoneyactual8()+deptemp.getMoneyactual9());
                                // 第三季度10,11,12
                                deptemp.setTotalactual3q(deptemp.getMoneyactual10()+deptemp.getMoneyactual11()+deptemp.getMoneyactual12());
                                // 第四季度1,2,3
                                deptemp.setTotalactual4q(deptemp.getMoneyactual1()+deptemp.getMoneyactual2()+deptemp.getMoneyactual3());
                                //全年
                                deptemp.setTotalactual(deptemp.getTotalactual1q()+deptemp.getTotalactual2q()+deptemp.getTotalactual3q()+deptemp.getTotalactual4q());
                                deptemp.preInsert();
                                departmentAccountListInsert.add(deptemp);
                            }
                        }
                        try
                        {
                             //新建数据
                             if(departmentAccountListInsert.size()>0)
                             {
                                departmentAccountMapper.insertDepAll(departmentAccountListInsert);
                             }
                              //更新数据
                             if(departmentAccountListUnpdate.size()>0)
                             {
                                departmentAccountMapper.updateDepAll(departmentAccountListUnpdate);
                             }
                        }
                        catch (Exception e)
                        {
                            System.out.println(month+"月数据生成数据异常");
                        }
                    }

                    //同一年度同一部门同一theme进行一个下方合计计算
                    List<DepartmentAccount> dList = new ArrayList<>();
                    DepartmentAccount departmentAccount = new DepartmentAccount();
                    departmentAccount.setDepartment(dv.getDepartmentId());
                    departmentAccount.setYears(String.valueOf(year));
                    dList = departmentAccountMapper.select(departmentAccount);
                    if(dList.size()>0)
                    {
                        //对当前部门的theme进行groupby
                        List<DepartmentAccount> themeIDGroupByList = dList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DepartmentAccount :: getTheme_id))), ArrayList::new));

                        for(DepartmentAccount themeid:themeIDGroupByList)
                        {
                            //检索合计是否已经存在
                            List<DepartmentAccountTotal> departmentAccountTotal = new ArrayList<>();
                            departmentAccountTotal = departmentAccountMapper.selectTotal(String.valueOf(year),dv.getDepartmentId(),themeid.getTheme_id());



                            //检索原始集合数据
                            //社内员工工数和社外员工工数
                            //外注费用，项目别
                            List<DepartmentTotalVo> departmentTotalVo = departmentAccountMapper.selectTotalBytheme(String.valueOf(year),dv.getDepartmentId(),themeid.getTheme_id());
                            List<DepartmentAccountTotal> depTotal = departmentAccountMapper.selectPJMount(String.valueOf(year),dv.getDepartmentId(),themeid.getTheme_id());
                            if(departmentTotalVo.size()>0 && depTotal.size()>0)
                            {
                                List<DepartmentAccountTotal> departmentAccountTotalInsert = new ArrayList<>();
                                List<DepartmentAccountTotal> departmentAccountTotalUpdate = new ArrayList<>();
                                if(departmentAccountTotal.size()>0)
                                {
                                    //更新
                                    //当前月员工工数
                                    Double mountinside = 0d;
                                    //当前月收入合计
                                    Double moneyintotal = 0d;
                                    //当前月支出合计
                                    Double moneyouttotal = 0d;
                                    for(DepartmentAccountTotal dattemp:departmentAccountTotal)
                                    {
                                        //通过theme ，对集合进行对应月份的合计收入统计
                                        switch (monthlast)
                                        {
                                            case 0:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual12(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual12).sum());
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10()+dattemp.getMoneyactual11()+dattemp.getMoneyactual12());
                                                    moneyintotal = dattemp.getTotalactual3q();
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算12月的工数
                                                    List<DepartmentTotalVo> dep12 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"12"))).collect(Collectors.toList());
                                                    if(dep12.size()>0)
                                                    {
                                                        dattemp.setMoneyactual12(dep12.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10()+dattemp.getMoneyactual11()+dattemp.getMoneyactual12());
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual12();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual12(mountinside * personalmoney);
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10()+dattemp.getMoneyactual11()+dattemp.getMoneyactual12());
                                                    moneyouttotal = dattemp.getTotalactual3q();
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算12月的工数
                                                    List<DepartmentTotalVo> dep12 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"12"))).collect(Collectors.toList());
                                                    if(dep12.size()>0)
                                                    {
                                                        dattemp.setMoneyactual12(dep12.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10()+dattemp.getMoneyactual11()+dattemp.getMoneyactual12());
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的12月外注费用
                                                    dattemp.setMoneyactual12(depTotal.get(0).getMoneyactual12());
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10()+dattemp.getMoneyactual11()+dattemp.getMoneyactual12());
                                                    moneyouttotal = moneyouttotal + dattemp.getTotalactual3q();
                                                }
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                if(dattemp.getAmount().equals("边界利润率"))
                                                {
                                                    //10,11,12 三个月的边界利润率
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual12((moneyintotal - moneyouttotal)/(moneyintotal == 0 ? 1:moneyintotal));
                                                }
                                                break;
                                            case 1:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual1(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual1).sum());
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算1月的工数
                                                    List<DepartmentTotalVo> dep1 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"01"))).collect(Collectors.toList());
                                                    if(dep1.size()>0)
                                                    {
                                                        dattemp.setMoneyactual1(dep1.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual1();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual1(mountinside * personalmoney);
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算1月的工数
                                                    List<DepartmentTotalVo> dep1 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"01"))).collect(Collectors.toList());
                                                    if(dep1.size()>0)
                                                    {
                                                        dattemp.setMoneyactual1(dep1.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的12月外注费用
                                                    dattemp.setMoneyactual1(depTotal.get(0).getMoneyactual1());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual1(0d);
                                                }
                                                //第四季度 1,2,3
                                                dattemp.setTotalactual4q(dattemp.getMoneyactual1()+dattemp.getMoneyactual2()+dattemp.getMoneyactual3());
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                break;
                                            case 2:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual2(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual2).sum());
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算2月的工数
                                                    List<DepartmentTotalVo> dep2 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"02"))).collect(Collectors.toList());
                                                    if(dep2.size()>0)
                                                    {
                                                        dattemp.setMoneyactual2(dep2.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual2();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual2(mountinside * personalmoney);
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算2月的工数
                                                    List<DepartmentTotalVo> dep2 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"02"))).collect(Collectors.toList());
                                                    if(dep2.size()>0)
                                                    {
                                                        dattemp.setMoneyactual2(dep2.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的12月外注费用
                                                    dattemp.setMoneyactual2(depTotal.get(0).getMoneyactual2());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual2(0d);
                                                }
                                                //第四季度 1,2,3
                                                dattemp.setTotalactual4q(dattemp.getMoneyactual1()+dattemp.getMoneyactual2()+dattemp.getMoneyactual3());
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                break;
                                            case 3:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual3(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual3).sum());
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1()+dattemp.getMoneyactual2()+dattemp.getMoneyactual3());
                                                    moneyintotal = dattemp.getTotalactual4q();
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算3月的工数
                                                    List<DepartmentTotalVo> dep3 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"03"))).collect(Collectors.toList());
                                                    if(dep3.size()>0)
                                                    {
                                                        dattemp.setMoneyactual3(dep3.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1()+dattemp.getMoneyactual2()+dattemp.getMoneyactual3());
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual3();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual3(mountinside * personalmoney);
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1()+dattemp.getMoneyactual2()+dattemp.getMoneyactual3());
                                                    moneyouttotal = dattemp.getTotalactual4q();
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算3月的工数
                                                    List<DepartmentTotalVo> dep3 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"03"))).collect(Collectors.toList());
                                                    if(dep3.size()>0)
                                                    {
                                                        dattemp.setMoneyactual3(dep3.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1()+dattemp.getMoneyactual2()+dattemp.getMoneyactual3());
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的12月外注费用
                                                    dattemp.setMoneyactual3(depTotal.get(0).getMoneyactual3());
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1()+dattemp.getMoneyactual2()+dattemp.getMoneyactual3());
                                                    moneyouttotal = moneyouttotal + dattemp.getTotalactual4q();
                                                }
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                if(dattemp.getAmount().equals("边界利润率"))
                                                {
                                                    //1,2,3 三个月的边界利润率
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual3((moneyintotal - moneyouttotal)/(moneyintotal == 0 ? 1:moneyintotal));
                                                }
                                                break;
                                            case 4:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual4(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual4).sum());
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算4月的工数
                                                    List<DepartmentTotalVo> dep4 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"04"))).collect(Collectors.toList());
                                                    if(dep4.size()>0)
                                                    {
                                                        dattemp.setMoneyactual4(dep4.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual4();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual4(mountinside * personalmoney);
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算4月的工数
                                                    List<DepartmentTotalVo> dep4 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"04"))).collect(Collectors.toList());
                                                    if(dep4.size()>0)
                                                    {
                                                        dattemp.setMoneyactual4(dep4.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的4月外注费用
                                                    dattemp.setMoneyactual4(depTotal.get(0).getMoneyactual4());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual4(0d);
                                                }
                                                //第一季度 4,5,6
                                                dattemp.setTotalactual1q(dattemp.getMoneyactual4()+dattemp.getMoneyactual5()+dattemp.getMoneyactual6());
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                break;
                                            case 5:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual5(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual5).sum());
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算5月的工数
                                                    List<DepartmentTotalVo> dep5 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"05"))).collect(Collectors.toList());
                                                    if(dep5.size()>0)
                                                    {
                                                        dattemp.setMoneyactual5(dep5.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual5();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual5(mountinside * personalmoney);
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算5月的工数
                                                    List<DepartmentTotalVo> dep5 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"05"))).collect(Collectors.toList());
                                                    if(dep5.size()>0)
                                                    {
                                                        dattemp.setMoneyactual5(dep5.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的5月外注费用
                                                    dattemp.setMoneyactual5(depTotal.get(0).getMoneyactual5());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual5(0d);
                                                }
                                                //第一季度 4,5,6
                                                dattemp.setTotalactual1q(dattemp.getMoneyactual4()+dattemp.getMoneyactual5()+dattemp.getMoneyactual6());
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                break;
                                            case 6:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual6(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual6).sum());
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4()+dattemp.getMoneyactual5()+dattemp.getMoneyactual6());
                                                    moneyintotal = dattemp.getTotalactual1q();
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算6月的工数
                                                    List<DepartmentTotalVo> dep6 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"06"))).collect(Collectors.toList());
                                                    if(dep6.size()>0)
                                                    {
                                                        dattemp.setMoneyactual6(dep6.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4()+dattemp.getMoneyactual5()+dattemp.getMoneyactual6());
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual6();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual6(mountinside * personalmoney);
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4()+dattemp.getMoneyactual5()+dattemp.getMoneyactual6());
                                                    moneyouttotal = dattemp.getTotalactual1q();
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算6月的工数
                                                    List<DepartmentTotalVo> dep6 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"06"))).collect(Collectors.toList());
                                                    if(dep6.size()>0)
                                                    {
                                                        dattemp.setMoneyactual6(dep6.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4()+dattemp.getMoneyactual5()+dattemp.getMoneyactual6());
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的5月外注费用
                                                    dattemp.setMoneyactual6(depTotal.get(0).getMoneyactual6());
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4()+dattemp.getMoneyactual5()+dattemp.getMoneyactual6());
                                                    moneyouttotal = moneyouttotal + dattemp.getTotalactual1q();
                                                }
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                if(dattemp.getAmount().equals("边界利润率"))
                                                {
                                                    //4,5,6 三个月的边界利润率
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual6((moneyintotal - moneyouttotal)/(moneyintotal == 0 ? 1:moneyintotal));
                                                }
                                                break;
                                            case 7:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual7(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual7).sum());
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算7月的工数
                                                    List<DepartmentTotalVo> dep7 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"07"))).collect(Collectors.toList());
                                                    if(dep7.size()>0)
                                                    {
                                                        dattemp.setMoneyactual7(dep7.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual7();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual7(mountinside * personalmoney);
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算7月的工数
                                                    List<DepartmentTotalVo> dep7 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"07"))).collect(Collectors.toList());
                                                    if(dep7.size()>0)
                                                    {
                                                        dattemp.setMoneyactual7(dep7.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的7月外注费用
                                                    dattemp.setMoneyactual7(depTotal.get(0).getMoneyactual7());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual7(0d);
                                                }
                                                //第二季度 7,8,9
                                                dattemp.setTotalactual2q(dattemp.getMoneyactual7()+dattemp.getMoneyactual8()+dattemp.getMoneyactual9());
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                break;
                                            case 8:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual8(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual8).sum());
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算8月的工数
                                                    List<DepartmentTotalVo> dep8 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"08"))).collect(Collectors.toList());
                                                    if(dep8.size()>0)
                                                    {
                                                        dattemp.setMoneyactual8(dep8.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual8();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual8(mountinside * personalmoney);
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算8月的工数
                                                    List<DepartmentTotalVo> dep8 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"08"))).collect(Collectors.toList());
                                                    if(dep8.size()>0)
                                                    {
                                                        dattemp.setMoneyactual8(dep8.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的8月外注费用
                                                    dattemp.setMoneyactual8(depTotal.get(0).getMoneyactual8());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual8(0d);
                                                }
                                                //第二季度 7,8,9
                                                dattemp.setTotalactual2q(dattemp.getMoneyactual7()+dattemp.getMoneyactual8()+dattemp.getMoneyactual9());
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                break;
                                            case 9:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual9(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual9).sum());
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7()+dattemp.getMoneyactual8()+dattemp.getMoneyactual9());
                                                    moneyintotal = dattemp.getTotalactual2q();
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算9月的工数
                                                    List<DepartmentTotalVo> dep9 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"09"))).collect(Collectors.toList());
                                                    if(dep9.size()>0)
                                                    {
                                                        dattemp.setMoneyactual9(dep9.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7()+dattemp.getMoneyactual8()+dattemp.getMoneyactual9());
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual9();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual9(mountinside * personalmoney);
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7()+dattemp.getMoneyactual8()+dattemp.getMoneyactual9());
                                                    moneyouttotal = dattemp.getTotalactual2q();
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算9月的工数
                                                    List<DepartmentTotalVo> dep9 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"09"))).collect(Collectors.toList());
                                                    if(dep9.size()>0)
                                                    {
                                                        dattemp.setMoneyactual9(dep9.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7()+dattemp.getMoneyactual8()+dattemp.getMoneyactual9());
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的8月外注费用
                                                    dattemp.setMoneyactual9(depTotal.get(0).getMoneyactual9());
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7()+dattemp.getMoneyactual8()+dattemp.getMoneyactual9());
                                                    moneyouttotal = moneyouttotal + dattemp.getTotalactual2q();
                                                }
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                if(dattemp.getAmount().equals("边界利润率"))
                                                {
                                                    //7,8,9 三个月的边界利润率
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual9((moneyintotal - moneyouttotal)/(moneyintotal == 0 ? 1:moneyintotal));
                                                }
                                                break;
                                            case 10:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual10(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual10).sum());
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算10月的工数
                                                    List<DepartmentTotalVo> dep10 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"10"))).collect(Collectors.toList());
                                                    if(dep10.size()>0)
                                                    {
                                                        dattemp.setMoneyactual10(dep10.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual10();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual10(mountinside * personalmoney);
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算10月的工数
                                                    List<DepartmentTotalVo> dep10 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"10"))).collect(Collectors.toList());
                                                    if(dep10.size()>0)
                                                    {
                                                        dattemp.setMoneyactual10(dep10.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的10月外注费用
                                                    dattemp.setMoneyactual10(depTotal.get(0).getMoneyactual10());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual10(0d);
                                                }
                                                //第三季度 10,11,12
                                                dattemp.setTotalactual3q(dattemp.getMoneyactual10()+dattemp.getMoneyactual11()+dattemp.getMoneyactual12());
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                break;
                                            case 11:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual11(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual11).sum());
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算11月的工数
                                                    List<DepartmentTotalVo> dep11 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"11"))).collect(Collectors.toList());
                                                    if(dep11.size()>0)
                                                    {
                                                        dattemp.setMoneyactual11(dep11.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual11();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual11(mountinside * personalmoney);
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算11月的工数
                                                    List<DepartmentTotalVo> dep11 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"11"))).collect(Collectors.toList());
                                                    if(dep11.size()>0)
                                                    {
                                                        dattemp.setMoneyactual11(dep11.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的11月外注费用
                                                    dattemp.setMoneyactual11(depTotal.get(0).getMoneyactual11());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual11(0d);
                                                }
                                                //第三季度 10,11,12
                                                dattemp.setTotalactual3q(dattemp.getMoneyactual10()+dattemp.getMoneyactual11()+dattemp.getMoneyactual12());
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q() + dattemp.getTotalactual2q() + dattemp.getTotalactual3q() + dattemp.getTotalactual4q());
                                                break;
                                        }
                                        dattemp.preUpdate(tokenModel);
                                        departmentAccountTotalUpdate.add(dattemp);
                                    }
                                }
                                else
                                {
                                    //新建
                                    //4月员工工数
                                    Double mountinside4 = 0d;
                                    //5月员工工数
                                    Double mountinside5 = 0d;
                                    //6月员工工数
                                    Double mountinside6 = 0d;
                                    //7月员工工数
                                    Double mountinside7 = 0d;
                                    //8月员工工数
                                    Double mountinside8 = 0d;
                                    //9月员工工数
                                    Double mountinside9 = 0d;
                                    //10月员工工数
                                    Double mountinside10 = 0d;
                                    //11月员工工数
                                    Double mountinside11 = 0d;
                                    //12月员工工数
                                    Double mountinside12 = 0d;
                                    //1月员工工数
                                    Double mountinside1 = 0d;
                                    //2月员工工数
                                    Double mountinside2= 0d;
                                    //3月员工工数
                                    Double mountinside3= 0d;

                                    //第一季度收入合计
                                    Double moneyintotal1q = 0d;
                                    //第二季度收入合计
                                    Double moneyintotal2q = 0d;
                                    //第三季度收入合计
                                    Double moneyintotal3q = 0d;
                                    //第四季度收入合计
                                    Double moneyintotal4q = 0d;
                                    //第一季度支出合计
                                    Double moneyouttotal1q = 0d;
                                    //第二季度支出合计
                                    Double moneyouttotal2q = 0d;
                                    //第三季度支出合计
                                    Double moneyouttotal3q = 0d;
                                    //第四季度支出合计
                                    Double moneyouttotal4q = 0d;
                                    for(int i=1;i<7;i++)
                                    {
                                        //收支合计的实体
                                        DepartmentAccountTotal dat = new DepartmentAccountTotal();
                                        dat.setYears(String.valueOf(year));
                                        dat.setDepartment(dv.getDepartmentId());
                                        dat.setTheme_id(themeid.getTheme_id());
                                        if(i==1)
                                        {
                                            dat.setAmount("收入合计");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("1");
                                            dat.setMoneyactual4(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual4).sum());
                                            dat.setMoneyactual5(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual5).sum());
                                            dat.setMoneyactual6(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual6).sum());
                                            dat.setTotalactual1q(dList.stream().mapToDouble(DepartmentAccount::getTotalactual1q).sum());
                                            moneyintotal1q = dat.getTotalactual1q();
                                            dat.setMoneyactual7(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual7).sum());
                                            dat.setMoneyactual8(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual8).sum());
                                            dat.setMoneyactual9(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual9).sum());
                                            dat.setTotalactual2q(dList.stream().mapToDouble(DepartmentAccount::getTotalactual2q).sum());
                                            moneyintotal2q = dat.getTotalactual2q();
                                            dat.setMoneyactual10(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual10).sum());
                                            dat.setMoneyactual11(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual11).sum());
                                            dat.setMoneyactual12(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual12).sum());
                                            dat.setTotalactual3q(dList.stream().mapToDouble(DepartmentAccount::getTotalactual3q).sum());
                                            moneyintotal3q = dat.getTotalactual3q();
                                            dat.setMoneyactual1(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual1).sum());
                                            dat.setMoneyactual2(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual2).sum());
                                            dat.setMoneyactual3(dList.stream().mapToDouble(DepartmentAccount::getMoneyactual3).sum());
                                            dat.setTotalactual4q(dList.stream().mapToDouble(DepartmentAccount::getTotalactual4q).sum());
                                            moneyintotal4q = dat.getTotalactual4q();
                                            dat.setTotalactual(dList.stream().mapToDouble(DepartmentAccount::getTotalactual).sum());
                                        }
                                        else if(i==2)
                                        {
                                            dat.setAmount("员工工数(人月)");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("2");
                                            //社内员工计算4月的工数
                                            List<DepartmentTotalVo> dep4 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"04"))).collect(Collectors.toList());
                                            if(dep4.size()>0)
                                            {
                                                dat.setMoneyactual4(dep4.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual4(0d);
                                            }
                                            mountinside4 = dat.getMoneyactual4();
                                            //社内员工计算5月的工数
                                            List<DepartmentTotalVo> dep5 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"05"))).collect(Collectors.toList());
                                            if(dep5.size()>0)
                                            {
                                                dat.setMoneyactual5(dep5.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual5(0d);
                                            }
                                            mountinside5 = dat.getMoneyactual5();
                                            //社内员工计算6月的工数
                                            List<DepartmentTotalVo> dep6 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"06"))).collect(Collectors.toList());
                                            if(dep6.size()>0)
                                            {
                                                dat.setMoneyactual6(dep6.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual6(0d);
                                            }
                                            mountinside6 = dat.getMoneyactual6();
                                            dat.setTotalactual1q(dat.getMoneyactual4()+dat.getMoneyactual5()+dat.getMoneyactual6());
                                            //社内员工计算7月的工数
                                            List<DepartmentTotalVo> dep7 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"07"))).collect(Collectors.toList());
                                            if(dep7.size()>0)
                                            {
                                                dat.setMoneyactual7(dep7.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual7(0d);
                                            }
                                            mountinside7 = dat.getMoneyactual7();
                                            //社内员工计算8月的工数
                                            List<DepartmentTotalVo> dep8 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"08"))).collect(Collectors.toList());
                                            if(dep8.size()>0)
                                            {
                                                dat.setMoneyactual8(dep8.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual8(0d);
                                            }
                                            mountinside8 = dat.getMoneyactual8();
                                            //社内员工计算9月的工数
                                            List<DepartmentTotalVo> dep9 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"09"))).collect(Collectors.toList());
                                            if(dep9.size()>0)
                                            {
                                                dat.setMoneyactual9(dep9.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual9(0d);
                                            }
                                            mountinside9 = dat.getMoneyactual9();
                                            dat.setTotalactual2q(dat.getMoneyactual7()+dat.getMoneyactual8()+dat.getMoneyactual9());
                                            //社内员工计算10月的工数
                                            List<DepartmentTotalVo> dep10 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"10"))).collect(Collectors.toList());
                                            if(dep10.size()>0)
                                            {
                                                dat.setMoneyactual10(dep10.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual10(0d);
                                            }
                                            mountinside10 = dat.getMoneyactual10();
                                            //社内员工计算11月的工数
                                            List<DepartmentTotalVo> dep11 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"11"))).collect(Collectors.toList());
                                            if(dep11.size()>0)
                                            {
                                                dat.setMoneyactual11(dep11.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual11(0d);
                                            }
                                            mountinside11 = dat.getMoneyactual11();
                                            //社内员工计算12月的工数
                                            List<DepartmentTotalVo> dep12 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"12"))).collect(Collectors.toList());
                                            if(dep12.size()>0)
                                            {
                                                dat.setMoneyactual12(dep12.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual12(0d);
                                            }
                                            mountinside12 = dat.getMoneyactual12();
                                            dat.setTotalactual3q(dat.getMoneyactual10()+dat.getMoneyactual11()+dat.getMoneyactual12());
                                            //社内员工计算1月的工数
                                            List<DepartmentTotalVo> dep1 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"01"))).collect(Collectors.toList());
                                            if(dep1.size()>0)
                                            {
                                                dat.setMoneyactual1(dep1.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual1(0d);
                                            }
                                            mountinside1 = dat.getMoneyactual1();
                                            //社内员工计算2月的工数
                                            List<DepartmentTotalVo> dep2 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"02"))).collect(Collectors.toList());
                                            if(dep2.size()>0)
                                            {
                                                dat.setMoneyactual2(dep2.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual2(0d);
                                            }
                                            mountinside2 = dat.getMoneyactual2();
                                            //社内员工计算3月的工数
                                            List<DepartmentTotalVo> dep3 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"03"))).collect(Collectors.toList());
                                            if(dep3.size()>0)
                                            {
                                                dat.setMoneyactual3(dep3.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual3(0d);
                                            }
                                            mountinside3 = dat.getMoneyactual3();
                                            dat.setTotalactual4q(dat.getMoneyactual1()+dat.getMoneyactual2()+dat.getMoneyactual3());
                                            dat.setTotalactual(dat.getTotalactual1q()+dat.getTotalactual2q()+dat.getTotalactual3q()+dat.getTotalactual4q());
                                        }
                                        else if(i==3)
                                        {
                                            dat.setAmount("员工支出");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("3");
                                            dat.setMoneyactual4(mountinside4*personalmoney);
                                            dat.setMoneyactual5(mountinside5*personalmoney);
                                            dat.setMoneyactual6(mountinside6*personalmoney);
                                            dat.setTotalactual1q(dat.getMoneyactual4()+dat.getMoneyactual5()+dat.getMoneyactual6());
                                            moneyouttotal1q = dat.getTotalactual1q();
                                            dat.setMoneyactual7(mountinside7*personalmoney);
                                            dat.setMoneyactual8(mountinside8*personalmoney);
                                            dat.setMoneyactual9(mountinside9*personalmoney);
                                            dat.setTotalactual2q(dat.getMoneyactual7()+dat.getMoneyactual8()+dat.getMoneyactual9());
                                            moneyouttotal2q = dat.getTotalactual2q();
                                            dat.setMoneyactual10(mountinside10*personalmoney);
                                            dat.setMoneyactual11(mountinside11*personalmoney);
                                            dat.setMoneyactual12(mountinside12*personalmoney);
                                            dat.setTotalactual3q(dat.getMoneyactual10()+dat.getMoneyactual11()+dat.getMoneyactual12());
                                            moneyouttotal3q = dat.getTotalactual3q();
                                            dat.setMoneyactual1(mountinside1*personalmoney);
                                            dat.setMoneyactual2(mountinside2*personalmoney);
                                            dat.setMoneyactual3(mountinside3*personalmoney);
                                            dat.setTotalactual4q(dat.getMoneyactual1()+dat.getMoneyactual2()+dat.getMoneyactual3());
                                            moneyouttotal4q = dat.getTotalactual4q();
                                            dat.setTotalactual(dat.getTotalactual1q()+dat.getTotalactual2q()+dat.getTotalactual3q()+dat.getTotalactual4q());
                                        }
                                        else if(i==4)
                                        {
                                            dat.setAmount("外注工数(人月)");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("4");
                                            //社外员工计算4月的工数
                                            List<DepartmentTotalVo> dep4 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"04"))).collect(Collectors.toList());
                                            if(dep4.size()>0)
                                            {
                                                dat.setMoneyactual4(dep4.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual4(0d);
                                            }
                                            //社外员工计算5月的工数
                                            List<DepartmentTotalVo> dep5 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"05"))).collect(Collectors.toList());
                                            if(dep5.size()>0)
                                            {
                                                dat.setMoneyactual5(dep5.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual5(0d);
                                            }
                                            //社外员工计算6月的工数
                                            List<DepartmentTotalVo> dep6 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"06"))).collect(Collectors.toList());
                                            if(dep6.size()>0)
                                            {
                                                dat.setMoneyactual6(dep6.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual6(0d);
                                            }
                                            dat.setTotalactual1q(dat.getMoneyactual4()+dat.getMoneyactual5()+dat.getMoneyactual6());
                                            //社外员工计算7月的工数
                                            List<DepartmentTotalVo> dep7 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"07"))).collect(Collectors.toList());
                                            if(dep7.size()>0)
                                            {
                                                dat.setMoneyactual7(dep7.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual7(0d);
                                            }
                                            //社外员工计算8月的工数
                                            List<DepartmentTotalVo> dep8 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"08"))).collect(Collectors.toList());
                                            if(dep8.size()>0)
                                            {
                                                dat.setMoneyactual8(dep8.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual8(0d);
                                            }
                                            //社外员工计算9月的工数
                                            List<DepartmentTotalVo> dep9 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"09"))).collect(Collectors.toList());
                                            if(dep9.size()>0)
                                            {
                                                dat.setMoneyactual9(dep9.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual9(0d);
                                            }
                                            dat.setTotalactual2q(dat.getMoneyactual7()+dat.getMoneyactual8()+dat.getMoneyactual9());
                                            //社外员工计算10月的工数
                                            List<DepartmentTotalVo> dep10 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"10"))).collect(Collectors.toList());
                                            if(dep10.size()>0)
                                            {
                                                dat.setMoneyactual10(dep10.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual10(0d);
                                            }
                                            //社外员工计算11月的工数
                                            List<DepartmentTotalVo> dep11 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"11"))).collect(Collectors.toList());
                                            if(dep11.size()>0)
                                            {
                                                dat.setMoneyactual11(dep11.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual11(0d);
                                            }
                                            //社外员工计算12月的工数
                                            List<DepartmentTotalVo> dep12 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"12"))).collect(Collectors.toList());
                                            if(dep12.size()>0)
                                            {
                                                dat.setMoneyactual12(dep12.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual12(0d);
                                            }
                                            dat.setTotalactual3q(dat.getMoneyactual10()+dat.getMoneyactual11()+dat.getMoneyactual12());
                                            //社外员工计算1月的工数
                                            List<DepartmentTotalVo> dep1 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"01"))).collect(Collectors.toList());
                                            if(dep1.size()>0)
                                            {
                                                dat.setMoneyactual1(dep1.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual1(0d);
                                            }
                                            //社外员工计算2月的工数
                                            List<DepartmentTotalVo> dep2 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"02"))).collect(Collectors.toList());
                                            if(dep2.size()>0)
                                            {
                                                dat.setMoneyactual2(dep2.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual2(0d);
                                            }
                                            //社外员工计算3月的工数
                                            List<DepartmentTotalVo> dep3 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"03"))).collect(Collectors.toList());
                                            if(dep3.size()>0)
                                            {
                                                dat.setMoneyactual3(dep3.stream().mapToDouble(DepartmentTotalVo::getMOUNT).sum());
                                            }
                                            else
                                            {
                                                dat.setMoneyactual3(0d);
                                            }
                                            dat.setTotalactual4q(dat.getMoneyactual1()+dat.getMoneyactual2()+dat.getMoneyactual3());
                                            dat.setTotalactual(dat.getTotalactual1q()+dat.getTotalactual2q()+dat.getTotalactual3q()+dat.getTotalactual4q());
                                        }
                                        else if(i==5)
                                        {
                                            dat.setAmount("外注支出");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("5");
                                            dat.setMoneyactual4(depTotal.get(0).getMoneyactual4());
                                            dat.setMoneyactual5(depTotal.get(0).getMoneyactual5());
                                            dat.setMoneyactual6(depTotal.get(0).getMoneyactual6());
                                            dat.setTotalactual1q(dat.getMoneyactual4()+dat.getMoneyactual5()+dat.getMoneyactual6());
                                            moneyouttotal1q = moneyouttotal1q + dat.getTotalactual1q();
                                            dat.setMoneyactual7(depTotal.get(0).getMoneyactual7());
                                            dat.setMoneyactual8(depTotal.get(0).getMoneyactual8());
                                            dat.setMoneyactual9(depTotal.get(0).getMoneyactual9());
                                            dat.setTotalactual2q(dat.getMoneyactual7()+dat.getMoneyactual8()+dat.getMoneyactual9());
                                            moneyouttotal2q = moneyouttotal2q + dat.getTotalactual2q();
                                            dat.setMoneyactual10(depTotal.get(0).getMoneyactual10());
                                            dat.setMoneyactual11(depTotal.get(0).getMoneyactual11());
                                            dat.setMoneyactual12(depTotal.get(0).getMoneyactual12());
                                            dat.setTotalactual3q(dat.getMoneyactual10()+dat.getMoneyactual11()+dat.getMoneyactual12());
                                            moneyouttotal3q = moneyouttotal3q + dat.getTotalactual3q();
                                            dat.setMoneyactual1(depTotal.get(0).getMoneyactual1());
                                            dat.setMoneyactual2(depTotal.get(0).getMoneyactual2());
                                            dat.setMoneyactual3(depTotal.get(0).getMoneyactual3());
                                            dat.setTotalactual4q(dat.getMoneyactual1()+dat.getMoneyactual2()+dat.getMoneyactual3());
                                            moneyouttotal4q = moneyouttotal4q + dat.getTotalactual4q();
                                            dat.setTotalactual(dat.getTotalactual1q()+dat.getTotalactual2q()+dat.getTotalactual3q()+dat.getTotalactual4q());
                                        }
                                        else
                                        {
                                            dat.setAmount("边界利润率");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("6");
                                            dat.setMoneyactual4(0d);
                                            dat.setMoneyactual5(0d);
                                            dat.setMoneyactual6((moneyintotal1q - moneyouttotal1q)/(moneyintotal1q == 0 ? 1:moneyintotal1q));
                                            dat.setTotalactual1q(0d);
                                            dat.setMoneyactual7(0d);
                                            dat.setMoneyactual8(0d);
                                            dat.setMoneyactual9((moneyintotal2q - moneyouttotal2q)/(moneyintotal2q == 0 ? 1:moneyintotal2q));
                                            dat.setTotalactual2q(0d);
                                            dat.setMoneyactual10(0d);
                                            dat.setMoneyactual11(0d);
                                            dat.setMoneyactual12((moneyintotal3q - moneyouttotal3q)/(moneyintotal3q == 0 ? 1:moneyintotal3q));
                                            dat.setTotalactual3q(0d);
                                            dat.setMoneyactual1(0d);
                                            dat.setMoneyactual2(0d);
                                            dat.setMoneyactual3((moneyintotal4q - moneyouttotal4q)/(moneyintotal4q == 0 ? 1:moneyintotal4q));
                                            dat.setTotalactual4q(0d);
                                            dat.setTotalactual(0d);
                                        }
                                        dat.preInsert();
                                        departmentAccountTotalInsert.add(dat);
                                    }
                                }
                                try
                                {
                                    if(departmentAccountTotalInsert.size()>0)
                                    {
                                        //新建数据
                                        departmentAccountMapper.insertDepTotalAll(departmentAccountTotalInsert);
                                    }
                                    if(departmentAccountTotalUpdate.size()>0)
                                    {
                                        //更新数据
                                        departmentAccountMapper.updateDepTotalAll(departmentAccountTotalUpdate);
                                    }
                                }
                                catch (Exception e)
                                {
                                    System.out.println(month+"月数据生成数据异常");
                                }
                            }
                        }
                    }
//                }
            }
        }
    }
}
