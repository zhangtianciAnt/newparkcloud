package com.nt.service_pfans.PFANS1000.Impl;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.DepartmentAccount;
import com.nt.dao_Pfans.PFANS1000.DepartmentAccountTotal;
import com.nt.dao_Pfans.PFANS1000.ThemeInfor;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentAccountVo;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentTotalVo;
import com.nt.dao_Pfans.PFANS5000.ProjectContract;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.service_pfans.PFANS1000.DepartmentAccountService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentAccountMapper;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentAccountTotalMapper;
import com.nt.service_pfans.PFANS1000.mapper.ThemeInforMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectContractMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.params.sortedset.ZAddParams;

import java.math.BigDecimal;
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

    @Autowired
    private DepartmentAccountTotalMapper departmentAccountTotalMapper;

    @Autowired
    private OrgTreeService orgtreeService;

//update_qhr_20210813 后台返回数据类型变化
    @Override
    public List<DepartmentAccountVo> selectBygroupid(String year, String department) throws Exception {
        List<DepartmentAccount> departmentAccountList = new ArrayList<>();
        OrgTree org = orgTreeService.get(new OrgTree());
        departmentAccountList = departmentAccountMapper.selectALL(year,department);
        List<DepartmentAccountVo> departmentAccountVoList = new ArrayList<>();
        //按照theme分组
        TreeMap<String,List<DepartmentAccount>> departList =  departmentAccountList.stream().collect(Collectors.groupingBy(DepartmentAccount :: getTheme_id,TreeMap::new,Collectors.toList()));
        if (departList.size() > 0) {
            for (List<DepartmentAccount> value : departList.values()) {
                value.stream().sorted(Comparator.comparing(DepartmentAccount::getIndextype).thenComparing(DepartmentAccount::getAmount));
                DepartmentAccountVo departmentAccountVo = new DepartmentAccountVo();
                departmentAccountVo.setThemename(value.get(0).getThemename());
                departmentAccountVo.setContract(value.get(0).getContract());
                departmentAccountVo.setToolsorgs(value.get(0).getToolsorgs());
                if(value.get(0).getContract().equals("社内"))
                {
                    OrgTree orginfo = orgTreeService.getOrgInfo(org, value.get(0).getToolsorgs());
                    departmentAccountVo.setToolsorgs(orginfo.getCompanyname());
                }
                departmentAccountVo.setDepartmentAccountList(value);
                departmentAccountVoList.add(departmentAccountVo);
            }
        }
        return departmentAccountVoList;
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
        //获取当前系统中有效的部门，按照预算编码统计
        departmentVoList = orgTreeService.getAllDepartment();

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
                    BigDecimal personalmoney = new BigDecimal(0);
                    List<Dictionary> dictionaryList = new ArrayList<>();
                    Dictionary dictionary = new Dictionary();
                    dictionary.setPcode("PJ147");
                    dictionary.setType("JY");
                    dictionary.setValue1(dv.getDepartmentEn());
                    dictionaryList = dictionaryMapper.select(dictionary);
                    if(dictionaryList.size()>0)
                    {
                        personalmoney = new BigDecimal(dictionaryList.get(0).getValue2() == null || dictionaryList.get(0).getValue2() == "" ? "0" :dictionaryList.get(0).getValue2());
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
                            //pc.setContract("NSM1811330009");
                            pc.setContract(dav.getContractnumber());
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
                                            departmentAccountTemp.get(0).setMoneyactual12(new BigDecimal(dav.getContractamountSum12()));
                                            break;
                                        case 1:
                                            departmentAccountTemp.get(0).setMoneyactual1(new BigDecimal(dav.getContractamountSum1()));
                                            break;
                                        case 2:
                                            departmentAccountTemp.get(0).setMoneyactual2(new BigDecimal(dav.getContractamountSum2()));
                                            break;
                                        case 3:
                                            departmentAccountTemp.get(0).setMoneyactual3(new BigDecimal(dav.getContractamountSum3()));
                                            break;
                                        case 4:
                                            departmentAccountTemp.get(0).setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            break;
                                        case 5:
                                            departmentAccountTemp.get(0).setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            break;
                                        case 6:
                                            departmentAccountTemp.get(0).setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            break;
                                        case 7:
                                            departmentAccountTemp.get(0).setMoneyactual7(new BigDecimal(dav.getContractamountSum7()));
                                            break;
                                        case 8:
                                            departmentAccountTemp.get(0).setMoneyactual8(new BigDecimal(dav.getContractamountSum8()));
                                            break;
                                        case 9:
                                            departmentAccountTemp.get(0).setMoneyactual9(new BigDecimal(dav.getContractamountSum9()));
                                            break;
                                        case 10:
                                            departmentAccountTemp.get(0).setMoneyactual10(new BigDecimal(dav.getContractamountSum10()));
                                            break;
                                        case 11:
                                            departmentAccountTemp.get(0).setMoneyactual11(new BigDecimal(dav.getContractamountSum11()));
                                            break;
                                    }
                                }
                                else
                                {
                                    switch (monthlast)
                                    {
                                        case 0:
                                            departmentAccountTemp.get(0).setMoneyactual12(new BigDecimal(dav.getClaimamountSum12()));
                                            break;
                                        case 1:
                                            departmentAccountTemp.get(0).setMoneyactual1(new BigDecimal(dav.getClaimamountSum1()));
                                            break;
                                        case 2:
                                            departmentAccountTemp.get(0).setMoneyactual2(new BigDecimal(dav.getClaimamountSum2()));
                                            break;
                                        case 3:
                                            departmentAccountTemp.get(0).setMoneyactual3(new BigDecimal(dav.getClaimamountSum3()));
                                            break;
                                        case 4:
                                            departmentAccountTemp.get(0).setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            break;
                                        case 5:
                                            departmentAccountTemp.get(0).setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            break;
                                        case 6:
                                            departmentAccountTemp.get(0).setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            break;
                                        case 7:
                                            departmentAccountTemp.get(0).setMoneyactual7(new BigDecimal(dav.getClaimamountSum7()));
                                            break;
                                        case 8:
                                            departmentAccountTemp.get(0).setMoneyactual8(new BigDecimal(dav.getClaimamountSum8()));
                                            break;
                                        case 9:
                                            departmentAccountTemp.get(0).setMoneyactual9(new BigDecimal(dav.getClaimamountSum9()));
                                            break;
                                        case 10:
                                            departmentAccountTemp.get(0).setMoneyactual10(new BigDecimal(dav.getClaimamountSum10()));
                                            break;
                                        case 11:
                                            departmentAccountTemp.get(0).setMoneyactual11(new BigDecimal(dav.getClaimamountSum11()));
                                            break;
                                    }
                                }
                                // 第一季度4,5,6,
                                departmentAccountTemp.get(0).setTotalactual1q(departmentAccountTemp.get(0).getMoneyactual4().add(departmentAccountTemp.get(0).getMoneyactual5().add(departmentAccountTemp.get(0).getMoneyactual6())));
                                // 第二季度7,8,9
                                departmentAccountTemp.get(0).setTotalactual2q(departmentAccountTemp.get(0).getMoneyactual7().add(departmentAccountTemp.get(0).getMoneyactual8().add(departmentAccountTemp.get(0).getMoneyactual9())));
                                // 第三季度10,11,12
                                departmentAccountTemp.get(0).setTotalactual3q(departmentAccountTemp.get(0).getMoneyactual10().add(departmentAccountTemp.get(0).getMoneyactual11().add(departmentAccountTemp.get(0).getMoneyactual12())));
                                // 第四季度1,2,3
                                departmentAccountTemp.get(0).setTotalactual4q(departmentAccountTemp.get(0).getMoneyactual1().add(departmentAccountTemp.get(0).getMoneyactual2().add(departmentAccountTemp.get(0).getMoneyactual3())));
                                //全年
                                departmentAccountTemp.get(0).setTotalactual(departmentAccountTemp.get(0).getTotalactual1q().add(departmentAccountTemp.get(0).getTotalactual2q().add(departmentAccountTemp.get(0).getTotalactual3q().add(departmentAccountTemp.get(0).getTotalactual4q()))));
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
                                    switch (monthlast)
                                    {
                                        case 0:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getContractamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getContractamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getContractamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getContractamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(dav.getContractamountSum11()));
                                            deptemp.setMoneyactual12(new BigDecimal(dav.getContractamountSum12()));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 1:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getContractamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getContractamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getContractamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getContractamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(dav.getContractamountSum11()));
                                            deptemp.setMoneyactual12(new BigDecimal(dav.getContractamountSum12()));
                                            deptemp.setMoneyactual1(new BigDecimal(dav.getContractamountSum1()));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 2:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getContractamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getContractamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getContractamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getContractamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(dav.getContractamountSum11()));
                                            deptemp.setMoneyactual12(new BigDecimal(dav.getContractamountSum12()));
                                            deptemp.setMoneyactual1(new BigDecimal(dav.getContractamountSum1()));
                                            deptemp.setMoneyactual2(new BigDecimal(dav.getContractamountSum2()));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 3:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getContractamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getContractamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getContractamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getContractamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(dav.getContractamountSum11()));
                                            deptemp.setMoneyactual12(new BigDecimal(dav.getContractamountSum12()));
                                            deptemp.setMoneyactual1(new BigDecimal(dav.getContractamountSum1()));
                                            deptemp.setMoneyactual2(new BigDecimal(dav.getContractamountSum2()));
                                            deptemp.setMoneyactual3(new BigDecimal(dav.getContractamountSum3()));
                                            break;
                                        case 4:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(0));
                                            deptemp.setMoneyactual6(new BigDecimal(0));
                                            deptemp.setMoneyactual7(new BigDecimal(0));
                                            deptemp.setMoneyactual8(new BigDecimal(0));
                                            deptemp.setMoneyactual9(new BigDecimal(0));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 5:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(0));
                                            deptemp.setMoneyactual7(new BigDecimal(0));
                                            deptemp.setMoneyactual8(new BigDecimal(0));
                                            deptemp.setMoneyactual9(new BigDecimal(0));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 6:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(0));
                                            deptemp.setMoneyactual8(new BigDecimal(0));
                                            deptemp.setMoneyactual9(new BigDecimal(0));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 7:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getContractamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(0));
                                            deptemp.setMoneyactual9(new BigDecimal(0));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 8:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getContractamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getContractamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(0));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 9:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getContractamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getContractamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getContractamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 10:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getContractamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getContractamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getContractamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getContractamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 11:
                                            deptemp.setAmount(dav.getContractamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getContractamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getContractamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getContractamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getContractamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getContractamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getContractamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getContractamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(dav.getContractamountSum11()));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                    }
                                }
                                else
                                {
                                    //一个项目，取请求金额
                                    switch (monthlast)
                                    {
                                        case 0:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getClaimamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getClaimamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getClaimamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getClaimamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(dav.getClaimamountSum11()));
                                            deptemp.setMoneyactual12(new BigDecimal(dav.getClaimamountSum12()));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 1:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getClaimamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getClaimamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getClaimamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getClaimamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(dav.getClaimamountSum11()));
                                            deptemp.setMoneyactual12(new BigDecimal(dav.getClaimamountSum12()));
                                            deptemp.setMoneyactual1(new BigDecimal(dav.getClaimamountSum1()));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 2:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getClaimamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getClaimamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getClaimamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getClaimamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(dav.getClaimamountSum11()));
                                            deptemp.setMoneyactual12(new BigDecimal(dav.getClaimamountSum12()));
                                            deptemp.setMoneyactual1(new BigDecimal(dav.getClaimamountSum1()));
                                            deptemp.setMoneyactual2(new BigDecimal(dav.getClaimamountSum2()));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 3:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getClaimamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getClaimamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getClaimamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getClaimamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(dav.getClaimamountSum11()));
                                            deptemp.setMoneyactual12(new BigDecimal(dav.getClaimamountSum12()));
                                            deptemp.setMoneyactual1(new BigDecimal(dav.getClaimamountSum1()));
                                            deptemp.setMoneyactual2(new BigDecimal(dav.getClaimamountSum2()));
                                            deptemp.setMoneyactual3(new BigDecimal(dav.getClaimamountSum3()));
                                            break;
                                        case 4:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(0));
                                            deptemp.setMoneyactual6(new BigDecimal(0));
                                            deptemp.setMoneyactual7(new BigDecimal(0));
                                            deptemp.setMoneyactual8(new BigDecimal(0));
                                            deptemp.setMoneyactual9(new BigDecimal(0));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 5:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(0));
                                            deptemp.setMoneyactual7(new BigDecimal(0));
                                            deptemp.setMoneyactual8(new BigDecimal(0));
                                            deptemp.setMoneyactual9(new BigDecimal(0));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 6:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(0));
                                            deptemp.setMoneyactual8(new BigDecimal(0));
                                            deptemp.setMoneyactual9(new BigDecimal(0));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 7:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getClaimamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(0));
                                            deptemp.setMoneyactual9(new BigDecimal(0));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 8:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getClaimamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getClaimamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(0));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 9:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getClaimamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getClaimamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getClaimamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(0));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 10:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getClaimamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getClaimamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getClaimamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getClaimamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(0));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                        case 11:
                                            deptemp.setAmount(dav.getClaimamountSum());
                                            deptemp.setMoneyactual4(new BigDecimal(dav.getClaimamountSum4()));
                                            deptemp.setMoneyactual5(new BigDecimal(dav.getClaimamountSum5()));
                                            deptemp.setMoneyactual6(new BigDecimal(dav.getClaimamountSum6()));
                                            deptemp.setMoneyactual7(new BigDecimal(dav.getClaimamountSum7()));
                                            deptemp.setMoneyactual8(new BigDecimal(dav.getClaimamountSum8()));
                                            deptemp.setMoneyactual9(new BigDecimal(dav.getClaimamountSum9()));
                                            deptemp.setMoneyactual10(new BigDecimal(dav.getClaimamountSum10()));
                                            deptemp.setMoneyactual11(new BigDecimal(dav.getClaimamountSum11()));
                                            deptemp.setMoneyactual12(new BigDecimal(0));
                                            deptemp.setMoneyactual1(new BigDecimal(0));
                                            deptemp.setMoneyactual2(new BigDecimal(0));
                                            deptemp.setMoneyactual3(new BigDecimal(0));
                                            break;
                                    }
                                }
                                deptemp.setDepartment(dv.getDepartmentId());
                                deptemp.setDepartmentaccountid(UUID.randomUUID().toString());
                                // 第一季度4,5,6
                                deptemp.setTotalactual1q(deptemp.getMoneyactual4().add(deptemp.getMoneyactual5().add(deptemp.getMoneyactual6())));
                                // 第二季度7,8,9
                                deptemp.setTotalactual2q(deptemp.getMoneyactual7().add(deptemp.getMoneyactual8().add(deptemp.getMoneyactual9())));
                                // 第三季度10,11,12
                                deptemp.setTotalactual3q(deptemp.getMoneyactual10().add(deptemp.getMoneyactual11().add(deptemp.getMoneyactual12())));
                                // 第四季度1,2,3
                                deptemp.setTotalactual4q(deptemp.getMoneyactual1().add(deptemp.getMoneyactual2().add(deptemp.getMoneyactual3())));
                                //全年
                                deptemp.setTotalactual(deptemp.getTotalactual1q().add(deptemp.getTotalactual2q().add(deptemp.getTotalactual3q().add(deptemp.getTotalactual4q()))));
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
                            System.out.println(month+"月数据生成异常");
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
                            List<DepartmentAccount> themeGroupByList = dList.stream().filter(item -> (item.getTheme_id().equals(themeid.getTheme_id()))).collect(Collectors.toList());
                            List<DepartmentAccountTotal> departmentAccountTotal = new ArrayList<>();
                            departmentAccountTotal = departmentAccountMapper.selectTotal(String.valueOf(year),dv.getDepartmentId(),themeid.getTheme_id());

                            //检索原始集合数据
                            //社内员工工数和社外员工工数
                            //外注费用，项目别
                            List<DepartmentTotalVo> departmentTotalVo = departmentAccountMapper.selectTotalBytheme(String.valueOf(year),dv.getDepartmentId(),themeid.getTheme_id());
                            List<DepartmentAccountTotal> depTotal = departmentAccountMapper.selectPJMount(String.valueOf(year),dv.getDepartmentId(),themeid.getTheme_id());
                            if(depTotal.size()==0)
                            {
                                DepartmentAccountTotal d = new DepartmentAccountTotal();
                                d.setMoneyactual4(BigDecimal.ZERO);
                                d.setMoneyactual5(BigDecimal.ZERO);
                                d.setMoneyactual6(BigDecimal.ZERO);
                                d.setMoneyactual7(BigDecimal.ZERO);
                                d.setMoneyactual8(BigDecimal.ZERO);
                                d.setMoneyactual9(BigDecimal.ZERO);
                                d.setMoneyactual10(BigDecimal.ZERO);
                                d.setMoneyactual11(BigDecimal.ZERO);
                                d.setMoneyactual12(BigDecimal.ZERO);
                                d.setMoneyactual1(BigDecimal.ZERO);
                                d.setMoneyactual2(BigDecimal.ZERO);
                                d.setMoneyactual3(BigDecimal.ZERO);
                                depTotal.add(d);
                            }
                           //ADD CCM 20210810 下方合计增加经费和差旅费 fr
                            List<DepartmentAccountTotal> fundingTotal = departmentAccountMapper.selectFundingBytheme(String.valueOf(year),dv.getDepartmentId(),themeid.getTheme_id());
                            List<DepartmentAccountTotal> travelTotal = departmentAccountMapper.selectTravelMountBytheme(String.valueOf(year),dv.getDepartmentId(),themeid.getTheme_id());
                            //ADD CCM 20210810 下方合计增加经费和差旅费 to

                            if(departmentTotalVo.size()>0)
                            {
                                List<DepartmentAccountTotal> departmentAccountTotalInsert = new ArrayList<>();
                                List<DepartmentAccountTotal> departmentAccountTotalUpdate = new ArrayList<>();
                                if(departmentAccountTotal.size()>0)
                                {
                                    //更新
                                    //当前月员工工数
                                    BigDecimal mountinside = new BigDecimal(0);
                                    //当前月收入合计
                                    BigDecimal moneyintotal = new BigDecimal(0);
                                    //当前月支出合计
                                    BigDecimal moneyouttotal = new BigDecimal(0);
                                    for(DepartmentAccountTotal dattemp:departmentAccountTotal)
                                    {
                                        //通过theme ，对集合进行对应月份的合计收入统计
                                        switch (monthlast)
                                        {
                                            case 0:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual12(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual12).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                                    moneyintotal = dattemp.getTotalactual3q();
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算12月的工数
                                                    List<DepartmentTotalVo> dep12 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"12"))).collect(Collectors.toList());
                                                    if(dep12.size()>0)
                                                    {
                                                        dattemp.setMoneyactual12(dep12.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual12();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual12(mountinside.multiply(personalmoney));
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                                    moneyouttotal = dattemp.getTotalactual3q();
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算12月的工数
                                                    List<DepartmentTotalVo> dep12 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"12"))).collect(Collectors.toList());
                                                    if(dep12.size()>0)
                                                    {
                                                        dattemp.setMoneyactual12(dep12.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的12月外注费用
                                                    dattemp.setMoneyactual12(depTotal.get(0).getMoneyactual12());
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual3q());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual12(fundingTotal.get(0).getMoneyactual12());
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual3q());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual12(travelTotal.get(0).getMoneyactual12());
                                                    //第三季度 10,11,12
                                                    dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual3q());
                                                }

                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                if(dattemp.getAmount().equals("边界利润率"))
                                                {
                                                    //10,11,12 三个月的边界利润率
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual12((moneyintotal.subtract(moneyouttotal)).divide((moneyintotal.compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ONE:moneyintotal),2,BigDecimal.ROUND_HALF_UP));
                                                }
                                                break;
                                            case 1:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual1(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual1).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算1月的工数
                                                    List<DepartmentTotalVo> dep1 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"01"))).collect(Collectors.toList());
                                                    if(dep1.size()>0)
                                                    {
                                                        dattemp.setMoneyactual1(dep1.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual1();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual1(mountinside.multiply(personalmoney));
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算1月的工数
                                                    List<DepartmentTotalVo> dep1 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"01"))).collect(Collectors.toList());
                                                    if(dep1.size()>0)
                                                    {
                                                        dattemp.setMoneyactual1(dep1.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的12月外注费用
                                                    dattemp.setMoneyactual1(depTotal.get(0).getMoneyactual1());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual1(fundingTotal.get(0).getMoneyactual1());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual1(travelTotal.get(0).getMoneyactual1());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual1(BigDecimal.ZERO);
                                                }
                                                //第四季度 1,2,3
                                                dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                break;
                                            case 2:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual2(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual2).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算2月的工数
                                                    List<DepartmentTotalVo> dep2 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"02"))).collect(Collectors.toList());
                                                    if(dep2.size()>0)
                                                    {
                                                        dattemp.setMoneyactual2(dep2.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual2();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual2(mountinside.multiply(personalmoney));
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算2月的工数
                                                    List<DepartmentTotalVo> dep2 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"02"))).collect(Collectors.toList());
                                                    if(dep2.size()>0)
                                                    {
                                                        dattemp.setMoneyactual2(dep2.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的12月外注费用
                                                    dattemp.setMoneyactual2(depTotal.get(0).getMoneyactual2());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual2(fundingTotal.get(0).getMoneyactual2());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual2(travelTotal.get(0).getMoneyactual2());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual2(BigDecimal.ZERO);
                                                }
                                                //第四季度 1,2,3
                                                dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                break;
                                            case 3:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual3(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual3).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                                    moneyintotal = dattemp.getTotalactual4q();
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算3月的工数
                                                    List<DepartmentTotalVo> dep3 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"03"))).collect(Collectors.toList());
                                                    if(dep3.size()>0)
                                                    {
                                                        dattemp.setMoneyactual3(dep3.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual3();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual3(mountinside.multiply(personalmoney));
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                                    moneyouttotal = dattemp.getTotalactual4q();
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算3月的工数
                                                    List<DepartmentTotalVo> dep3 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"03"))).collect(Collectors.toList());
                                                    if(dep3.size()>0)
                                                    {
                                                        dattemp.setMoneyactual3(dep3.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的12月外注费用
                                                    dattemp.setMoneyactual3(depTotal.get(0).getMoneyactual3());
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual4q());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual3(fundingTotal.get(0).getMoneyactual3());
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual4q());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual3(travelTotal.get(0).getMoneyactual3());
                                                    //第四季度 1,2,3
                                                    dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual4q());
                                                }
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add( dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                if(dattemp.getAmount().equals("边界利润率"))
                                                {
                                                    //1,2,3 三个月的边界利润率
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual3((moneyintotal.subtract(moneyouttotal)).divide((moneyintotal.compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ONE:moneyintotal),2,BigDecimal.ROUND_HALF_UP));
                                                }
                                                break;
                                            case 4:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual4(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual4).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算4月的工数
                                                    List<DepartmentTotalVo> dep4 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"04"))).collect(Collectors.toList());
                                                    if(dep4.size()>0)
                                                    {
                                                        dattemp.setMoneyactual4(dep4.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual4();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual4(mountinside.multiply(personalmoney));
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算4月的工数
                                                    List<DepartmentTotalVo> dep4 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"04"))).collect(Collectors.toList());
                                                    if(dep4.size()>0)
                                                    {
                                                        dattemp.setMoneyactual4(dep4.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的4月外注费用
                                                    dattemp.setMoneyactual4(depTotal.get(0).getMoneyactual4());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual4(fundingTotal.get(0).getMoneyactual4());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual4(travelTotal.get(0).getMoneyactual4());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual4(BigDecimal.ZERO);
                                                }
                                                //第一季度 4,5,6
                                                dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                break;
                                            case 5:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual5(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual5).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算5月的工数
                                                    List<DepartmentTotalVo> dep5 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"05"))).collect(Collectors.toList());
                                                    if(dep5.size()>0)
                                                    {
                                                        dattemp.setMoneyactual5(dep5.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual5();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual5(mountinside.multiply(personalmoney));
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算5月的工数
                                                    List<DepartmentTotalVo> dep5 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"05"))).collect(Collectors.toList());
                                                    if(dep5.size()>0)
                                                    {
                                                        dattemp.setMoneyactual5(dep5.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的5月外注费用
                                                    dattemp.setMoneyactual5(depTotal.get(0).getMoneyactual5());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual5(fundingTotal.get(0).getMoneyactual5());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual5(travelTotal.get(0).getMoneyactual5());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual5(BigDecimal.ZERO);
                                                }
                                                //第一季度 4,5,6
                                                dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                break;
                                            case 6:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual6(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual6).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                                    moneyintotal = dattemp.getTotalactual1q();
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算6月的工数
                                                    List<DepartmentTotalVo> dep6 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"06"))).collect(Collectors.toList());
                                                    if(dep6.size()>0)
                                                    {
                                                        dattemp.setMoneyactual6(dep6.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual6();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual6(mountinside.multiply(personalmoney));
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                                    moneyouttotal = dattemp.getTotalactual1q();
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算6月的工数
                                                    List<DepartmentTotalVo> dep6 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"06"))).collect(Collectors.toList());
                                                    if(dep6.size()>0)
                                                    {
                                                        dattemp.setMoneyactual6(dep6.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的5月外注费用
                                                    dattemp.setMoneyactual6(depTotal.get(0).getMoneyactual6());
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual1q());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual6(fundingTotal.get(0).getMoneyactual6());
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual1q());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual6(travelTotal.get(0).getMoneyactual6());
                                                    //第一季度 4,5,6
                                                    dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual1q());
                                                }
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                if(dattemp.getAmount().equals("边界利润率"))
                                                {
                                                    //4,5,6 三个月的边界利润率
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual6((moneyintotal.subtract(moneyouttotal)).divide((moneyintotal.compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ONE:moneyintotal),2,BigDecimal.ROUND_HALF_UP));
                                                }
                                                break;
                                            case 7:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual7(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual7).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算7月的工数
                                                    List<DepartmentTotalVo> dep7 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"07"))).collect(Collectors.toList());
                                                    if(dep7.size()>0)
                                                    {
                                                        dattemp.setMoneyactual7(dep7.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual7();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual7(mountinside.multiply(personalmoney));
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算7月的工数
                                                    List<DepartmentTotalVo> dep7 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"07"))).collect(Collectors.toList());
                                                    if(dep7.size()>0)
                                                    {
                                                        dattemp.setMoneyactual7(dep7.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的7月外注费用
                                                    dattemp.setMoneyactual7(depTotal.get(0).getMoneyactual7());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual7(fundingTotal.get(0).getMoneyactual7());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual7(travelTotal.get(0).getMoneyactual7());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual7(BigDecimal.ZERO);
                                                }
                                                //第二季度 7,8,9
                                                dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                break;
                                            case 8:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual8(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual8).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算8月的工数
                                                    List<DepartmentTotalVo> dep8 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"08"))).collect(Collectors.toList());
                                                    if(dep8.size()>0)
                                                    {
                                                        dattemp.setMoneyactual8(dep8.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual8();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual8(mountinside.multiply(personalmoney));
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算8月的工数
                                                    List<DepartmentTotalVo> dep8 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"08"))).collect(Collectors.toList());
                                                    if(dep8.size()>0)
                                                    {
                                                        dattemp.setMoneyactual8(dep8.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的8月外注费用
                                                    dattemp.setMoneyactual8(depTotal.get(0).getMoneyactual8());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual8(fundingTotal.get(0).getMoneyactual8());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual8(travelTotal.get(0).getMoneyactual8());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual8(BigDecimal.ZERO);
                                                }
                                                //第二季度 7,8,9
                                                dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                break;
                                            case 9:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual9(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual9).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                                    moneyintotal = dattemp.getTotalactual2q();
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算9月的工数
                                                    List<DepartmentTotalVo> dep9 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"09"))).collect(Collectors.toList());
                                                    if(dep9.size()>0)
                                                    {
                                                        dattemp.setMoneyactual9(dep9.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual9();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual9(mountinside.multiply(personalmoney));
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                                    moneyouttotal = dattemp.getTotalactual2q();
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算9月的工数
                                                    List<DepartmentTotalVo> dep9 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"09"))).collect(Collectors.toList());
                                                    if(dep9.size()>0)
                                                    {
                                                        dattemp.setMoneyactual9(dep9.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的8月外注费用
                                                    dattemp.setMoneyactual9(depTotal.get(0).getMoneyactual9());
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual2q());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual9(fundingTotal.get(0).getMoneyactual9());
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual2q());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual9(travelTotal.get(0).getMoneyactual9());
                                                    //第二季度 7,8,9
                                                    dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                                    moneyouttotal = moneyouttotal.add(dattemp.getTotalactual2q());
                                                }
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                if(dattemp.getAmount().equals("边界利润率"))
                                                {
                                                    //7,8,9 三个月的边界利润率
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual9((moneyintotal.subtract(moneyouttotal)).divide((moneyintotal.compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ONE:moneyintotal),2,BigDecimal.ROUND_HALF_UP));

                                                }
                                                break;
                                            case 10:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual10(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual10).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算10月的工数
                                                    List<DepartmentTotalVo> dep10 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"10"))).collect(Collectors.toList());
                                                    if(dep10.size()>0)
                                                    {
                                                        dattemp.setMoneyactual10(dep10.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual10();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual10(mountinside.multiply(personalmoney));
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算10月的工数
                                                    List<DepartmentTotalVo> dep10 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"10"))).collect(Collectors.toList());
                                                    if(dep10.size()>0)
                                                    {
                                                        dattemp.setMoneyactual10(dep10.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的10月外注费用
                                                    dattemp.setMoneyactual10(depTotal.get(0).getMoneyactual10());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual10(fundingTotal.get(0).getMoneyactual10());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual10(travelTotal.get(0).getMoneyactual10());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual10(BigDecimal.ZERO);
                                                }
                                                //第三季度 10,11,12
                                                dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                                break;
                                            case 11:
                                                if(dattemp.getAmount().equals("收入合计"))
                                                {
                                                    dattemp.setMoneyactual11(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual11).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                }
                                                else if(dattemp.getAmount().equals("员工工数(人月)"))
                                                {
                                                    //社内员工计算11月的工数
                                                    List<DepartmentTotalVo> dep11 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"11"))).collect(Collectors.toList());
                                                    if(dep11.size()>0)
                                                    {
                                                        dattemp.setMoneyactual11(dep11.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                    //保留本月的社内工数
                                                    mountinside = dattemp.getMoneyactual11();
                                                }
                                                else if(dattemp.getAmount().equals("员工支出"))
                                                {
                                                    //使用工数*全公司人件费
                                                    dattemp.setMoneyactual11(mountinside.multiply(personalmoney));
                                                }
                                                else if(dattemp.getAmount().equals("外注工数(人月)"))
                                                {
                                                    //社外员工通过项目计算11月的工数
                                                    List<DepartmentTotalVo> dep11 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"11"))).collect(Collectors.toList());
                                                    if(dep11.size()>0)
                                                    {
                                                        dattemp.setMoneyactual11(dep11.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                                    }
                                                }
                                                else if(dattemp.getAmount().equals("外注支出"))
                                                {
                                                    //从BP社PJ别获取外注费用，通过年度，部门，theme，对应的11月外注费用
                                                    dattemp.setMoneyactual11(depTotal.get(0).getMoneyactual11());
                                                }
                                                else if(dattemp.getAmount().equals("经费"))
                                                {
                                                    //从公共费用精算书中获取经费，审批通过，取申请日月份
                                                    dattemp.setMoneyactual11(fundingTotal.get(0).getMoneyactual11());
                                                }
                                                else if(dattemp.getAmount().equals("差旅诶"))
                                                {
                                                    //从出差精算书中获取费用，审批通过，取申请日月份
                                                    dattemp.setMoneyactual11(travelTotal.get(0).getMoneyactual11());
                                                }
                                                else
                                                {
                                                    //(收入合计-员工支出-外注支出)/收入合计
                                                    dattemp.setMoneyactual11(BigDecimal.ZERO);
                                                }
                                                //第三季度 10,11,12
                                                dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                                //全年
                                                dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
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
                                    BigDecimal mountinside4 = BigDecimal.ZERO;
                                    //5月员工工数
                                    BigDecimal mountinside5 = BigDecimal.ZERO;
                                    //6月员工工数
                                    BigDecimal mountinside6 = BigDecimal.ZERO;
                                    //7月员工工数
                                    BigDecimal mountinside7 = BigDecimal.ZERO;
                                    //8月员工工数
                                    BigDecimal mountinside8 = BigDecimal.ZERO;
                                    //9月员工工数
                                    BigDecimal mountinside9 = BigDecimal.ZERO;
                                    //10月员工工数
                                    BigDecimal mountinside10 = BigDecimal.ZERO;
                                    //11月员工工数
                                    BigDecimal mountinside11 = BigDecimal.ZERO;
                                    //12月员工工数
                                    BigDecimal mountinside12 = BigDecimal.ZERO;
                                    //1月员工工数
                                    BigDecimal mountinside1 = BigDecimal.ZERO;
                                    //2月员工工数
                                    BigDecimal mountinside2= BigDecimal.ZERO;
                                    //3月员工工数
                                    BigDecimal mountinside3= BigDecimal.ZERO;

                                    //第一季度收入合计
                                    BigDecimal moneyintotal1q = BigDecimal.ZERO;
                                    //第二季度收入合计
                                    BigDecimal moneyintotal2q = BigDecimal.ZERO;
                                    //第三季度收入合计
                                    BigDecimal moneyintotal3q = BigDecimal.ZERO;
                                    //第四季度收入合计
                                    BigDecimal moneyintotal4q = BigDecimal.ZERO;
                                    //第一季度支出合计
                                    BigDecimal moneyouttotal1q = BigDecimal.ZERO;
                                    //第二季度支出合计
                                    BigDecimal moneyouttotal2q = BigDecimal.ZERO;
                                    //第三季度支出合计
                                    BigDecimal moneyouttotal3q = BigDecimal.ZERO;
                                    //第四季度支出合计
                                    BigDecimal moneyouttotal4q = BigDecimal.ZERO;
                                    for(int i=1;i<9;i++)
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
                                            dat.setMoneyactual4(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual4).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setMoneyactual5(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual5).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setMoneyactual6(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual6).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setTotalactual1q(themeGroupByList.stream().map(DepartmentAccount::getTotalactual1q).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            moneyintotal1q = dat.getTotalactual1q();
                                            dat.setMoneyactual7(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual7).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setMoneyactual8(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual8).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setMoneyactual9(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual9).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setTotalactual2q(themeGroupByList.stream().map(DepartmentAccount::getTotalactual2q).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            moneyintotal2q = dat.getTotalactual2q();
                                            dat.setMoneyactual10(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual10).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setMoneyactual11(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual11).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setMoneyactual12(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual12).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setTotalactual3q(themeGroupByList.stream().map(DepartmentAccount::getTotalactual3q).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            moneyintotal3q = dat.getTotalactual3q();
                                            dat.setMoneyactual1(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual1).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setMoneyactual2(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual2).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setMoneyactual3(themeGroupByList.stream().map(DepartmentAccount::getMoneyactual3).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            dat.setTotalactual4q(themeGroupByList.stream().map(DepartmentAccount::getTotalactual4q).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            moneyintotal4q = dat.getTotalactual4q();
                                            dat.setTotalactual(themeGroupByList.stream().map(DepartmentAccount::getTotalactual).reduce(BigDecimal.ZERO,BigDecimal::add));
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
                                                dat.setMoneyactual4(dep4.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual4(BigDecimal.ZERO);
                                            }
                                            mountinside4 = dat.getMoneyactual4();
                                            //社内员工计算5月的工数
                                            List<DepartmentTotalVo> dep5 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"05"))).collect(Collectors.toList());
                                            if(dep5.size()>0)
                                            {
                                                dat.setMoneyactual5(dep5.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual5(BigDecimal.ZERO);
                                            }
                                            mountinside5 = dat.getMoneyactual5();
                                            //社内员工计算6月的工数
                                            List<DepartmentTotalVo> dep6 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"06"))).collect(Collectors.toList());
                                            if(dep6.size()>0)
                                            {
                                                dat.setMoneyactual6(dep6.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual6(BigDecimal.ZERO);
                                            }
                                            mountinside6 = dat.getMoneyactual6();
                                            dat.setTotalactual1q(dat.getMoneyactual4().add(dat.getMoneyactual5().add(dat.getMoneyactual6())));
                                            //社内员工计算7月的工数
                                            List<DepartmentTotalVo> dep7 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"07"))).collect(Collectors.toList());
                                            if(dep7.size()>0)
                                            {
                                                dat.setMoneyactual7(dep7.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual7(BigDecimal.ZERO);
                                            }
                                            mountinside7 = dat.getMoneyactual7();
                                            //社内员工计算8月的工数
                                            List<DepartmentTotalVo> dep8 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"08"))).collect(Collectors.toList());
                                            if(dep8.size()>0)
                                            {
                                                dat.setMoneyactual8(dep8.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual8(BigDecimal.ZERO);
                                            }
                                            mountinside8 = dat.getMoneyactual8();
                                            //社内员工计算9月的工数
                                            List<DepartmentTotalVo> dep9 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"09"))).collect(Collectors.toList());
                                            if(dep9.size()>0)
                                            {
                                                dat.setMoneyactual9(dep9.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual9(BigDecimal.ZERO);
                                            }
                                            mountinside9 = dat.getMoneyactual9();
                                            dat.setTotalactual2q(dat.getMoneyactual7().add(dat.getMoneyactual8().add(dat.getMoneyactual9())));
                                            //社内员工计算10月的工数
                                            List<DepartmentTotalVo> dep10 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"10"))).collect(Collectors.toList());
                                            if(dep10.size()>0)
                                            {
                                                dat.setMoneyactual10(dep10.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual10(BigDecimal.ZERO);
                                            }
                                            mountinside10 = dat.getMoneyactual10();
                                            //社内员工计算11月的工数
                                            List<DepartmentTotalVo> dep11 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"11"))).collect(Collectors.toList());
                                            if(dep11.size()>0)
                                            {
                                                dat.setMoneyactual11(dep11.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual11(BigDecimal.ZERO);
                                            }
                                            mountinside11 = dat.getMoneyactual11();
                                            //社内员工计算12月的工数
                                            List<DepartmentTotalVo> dep12 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"12"))).collect(Collectors.toList());
                                            if(dep12.size()>0)
                                            {
                                                dat.setMoneyactual12(dep12.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual12(BigDecimal.ZERO);
                                            }
                                            mountinside12 = dat.getMoneyactual12();
                                            dat.setTotalactual3q(dat.getMoneyactual10().add(dat.getMoneyactual11().add(dat.getMoneyactual12())));
                                            //社内员工计算1月的工数
                                            List<DepartmentTotalVo> dep1 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"01"))).collect(Collectors.toList());
                                            if(dep1.size()>0)
                                            {
                                                dat.setMoneyactual1(dep1.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual1(BigDecimal.ZERO);
                                            }
                                            mountinside1 = dat.getMoneyactual1();
                                            //社内员工计算2月的工数
                                            List<DepartmentTotalVo> dep2 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"02"))).collect(Collectors.toList());
                                            if(dep2.size()>0)
                                            {
                                                dat.setMoneyactual2(dep2.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual2(BigDecimal.ZERO);
                                            }
                                            mountinside2 = dat.getMoneyactual2();
                                            //社内员工计算3月的工数
                                            List<DepartmentTotalVo> dep3 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("0") && item.getLOGDATE().equals(yearnow +"03"))).collect(Collectors.toList());
                                            if(dep3.size()>0)
                                            {
                                                dat.setMoneyactual3(dep3.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual3(BigDecimal.ZERO);
                                            }
                                            mountinside3 = dat.getMoneyactual3();
                                            dat.setTotalactual4q(dat.getMoneyactual1().add(dat.getMoneyactual2().add(dat.getMoneyactual3())));
                                            dat.setTotalactual(dat.getTotalactual1q().add(dat.getTotalactual2q().add(dat.getTotalactual3q().add(dat.getTotalactual4q()))));
                                        }
                                        else if(i==3)
                                        {
                                            dat.setAmount("员工支出");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("3");
                                            dat.setMoneyactual4(mountinside4.multiply(personalmoney));
                                            dat.setMoneyactual5(mountinside5.multiply(personalmoney));
                                            dat.setMoneyactual6(mountinside6.multiply(personalmoney));
                                            dat.setTotalactual1q(dat.getMoneyactual4().add(dat.getMoneyactual5().add(dat.getMoneyactual6())));
                                            moneyouttotal1q = dat.getTotalactual1q();
                                            dat.setMoneyactual7(mountinside7.multiply(personalmoney));
                                            dat.setMoneyactual8(mountinside8.multiply(personalmoney));
                                            dat.setMoneyactual9(mountinside9.multiply(personalmoney));
                                            dat.setTotalactual2q(dat.getMoneyactual7().add(dat.getMoneyactual8().add(dat.getMoneyactual9())));
                                            moneyouttotal2q = dat.getTotalactual2q();
                                            dat.setMoneyactual10(mountinside10.multiply(personalmoney));
                                            dat.setMoneyactual11(mountinside11.multiply(personalmoney));
                                            dat.setMoneyactual12(mountinside12.multiply(personalmoney));
                                            dat.setTotalactual3q(dat.getMoneyactual10().add(dat.getMoneyactual11().add(dat.getMoneyactual12())));
                                            moneyouttotal3q = dat.getTotalactual3q();
                                            dat.setMoneyactual1(mountinside1.multiply(personalmoney));
                                            dat.setMoneyactual2(mountinside2.multiply(personalmoney));
                                            dat.setMoneyactual3(mountinside3.multiply(personalmoney));
                                            dat.setTotalactual4q(dat.getMoneyactual1().add(dat.getMoneyactual2().add(dat.getMoneyactual3())));
                                            moneyouttotal4q = dat.getTotalactual4q();
                                            dat.setTotalactual(dat.getTotalactual1q().add(dat.getTotalactual2q().add(dat.getTotalactual3q().add(dat.getTotalactual4q()))));
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
                                                dat.setMoneyactual4(dep4.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual4(BigDecimal.ZERO);
                                            }
                                            //社外员工计算5月的工数
                                            List<DepartmentTotalVo> dep5 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"05"))).collect(Collectors.toList());
                                            if(dep5.size()>0)
                                            {
                                                dat.setMoneyactual5(dep5.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual5(BigDecimal.ZERO);
                                            }
                                            //社外员工计算6月的工数
                                            List<DepartmentTotalVo> dep6 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"06"))).collect(Collectors.toList());
                                            if(dep6.size()>0)
                                            {
                                                dat.setMoneyactual6(dep6.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual6(BigDecimal.ZERO);
                                            }
                                            dat.setTotalactual1q(dat.getMoneyactual4().add(dat.getMoneyactual5().add(dat.getMoneyactual6())));
                                            //社外员工计算7月的工数
                                            List<DepartmentTotalVo> dep7 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"07"))).collect(Collectors.toList());
                                            if(dep7.size()>0)
                                            {
                                                dat.setMoneyactual7(dep7.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual7(BigDecimal.ZERO);
                                            }
                                            //社外员工计算8月的工数
                                            List<DepartmentTotalVo> dep8 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"08"))).collect(Collectors.toList());
                                            if(dep8.size()>0)
                                            {
                                                dat.setMoneyactual8(dep8.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual8(BigDecimal.ZERO);
                                            }
                                            //社外员工计算9月的工数
                                            List<DepartmentTotalVo> dep9 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"09"))).collect(Collectors.toList());
                                            if(dep9.size()>0)
                                            {
                                                dat.setMoneyactual9(dep9.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual9(BigDecimal.ZERO);
                                            }
                                            dat.setTotalactual2q(dat.getMoneyactual7().add(dat.getMoneyactual8().add(dat.getMoneyactual9())));
                                            //社外员工计算10月的工数
                                            List<DepartmentTotalVo> dep10 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"10"))).collect(Collectors.toList());
                                            if(dep10.size()>0)
                                            {
                                                dat.setMoneyactual10(dep10.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual10(BigDecimal.ZERO);
                                            }
                                            //社外员工计算11月的工数
                                            List<DepartmentTotalVo> dep11 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"11"))).collect(Collectors.toList());
                                            if(dep11.size()>0)
                                            {
                                                dat.setMoneyactual11(dep11.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual11(BigDecimal.ZERO);
                                            }
                                            //社外员工计算12月的工数
                                            List<DepartmentTotalVo> dep12 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"12"))).collect(Collectors.toList());
                                            if(dep12.size()>0)
                                            {
                                                dat.setMoneyactual12(dep12.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual12(BigDecimal.ZERO);
                                            }
                                            dat.setTotalactual3q(dat.getMoneyactual10().add(dat.getMoneyactual11().add(dat.getMoneyactual12())));
                                            //社外员工计算1月的工数
                                            List<DepartmentTotalVo> dep1 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"01"))).collect(Collectors.toList());
                                            if(dep1.size()>0)
                                            {
                                                dat.setMoneyactual1(dep1.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual1(BigDecimal.ZERO);
                                            }
                                            //社外员工计算2月的工数
                                            List<DepartmentTotalVo> dep2 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"02"))).collect(Collectors.toList());
                                            if(dep2.size()>0)
                                            {
                                                dat.setMoneyactual2(dep2.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual2(BigDecimal.ZERO);
                                            }
                                            //社外员工计算3月的工数
                                            List<DepartmentTotalVo> dep3 = departmentTotalVo.stream().filter(item -> (item.getTYPE().equals("1") && item.getLOGDATE().equals(yearnow +"03"))).collect(Collectors.toList());
                                            if(dep3.size()>0)
                                            {
                                                dat.setMoneyactual3(dep3.stream().map(DepartmentTotalVo::getMOUNT).reduce(BigDecimal.ZERO,BigDecimal::add));
                                            }
                                            else
                                            {
                                                dat.setMoneyactual3(BigDecimal.ZERO);
                                            }
                                            dat.setTotalactual4q(dat.getMoneyactual1().add(dat.getMoneyactual2().add(dat.getMoneyactual3())));
                                            dat.setTotalactual(dat.getTotalactual1q().add(dat.getTotalactual2q().add(dat.getTotalactual3q().add(dat.getTotalactual4q()))));
                                        }
                                        else if(i==5)
                                        {
                                            dat.setAmount("外注支出");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("5");
                                            dat.setMoneyactual4(depTotal.get(0).getMoneyactual4());
                                            dat.setMoneyactual5(depTotal.get(0).getMoneyactual5());
                                            dat.setMoneyactual6(depTotal.get(0).getMoneyactual6());
                                            dat.setTotalactual1q(dat.getMoneyactual4().add(dat.getMoneyactual5().add(dat.getMoneyactual6())));
                                            moneyouttotal1q = moneyouttotal1q.add(dat.getTotalactual1q());
                                            dat.setMoneyactual7(depTotal.get(0).getMoneyactual7());
                                            dat.setMoneyactual8(depTotal.get(0).getMoneyactual8());
                                            dat.setMoneyactual9(depTotal.get(0).getMoneyactual9());
                                            dat.setTotalactual2q(dat.getMoneyactual7().add(dat.getMoneyactual8().add(dat.getMoneyactual9())));
                                            moneyouttotal2q = moneyouttotal2q.add(dat.getTotalactual2q());
                                            dat.setMoneyactual10(depTotal.get(0).getMoneyactual10());
                                            dat.setMoneyactual11(depTotal.get(0).getMoneyactual11());
                                            dat.setMoneyactual12(depTotal.get(0).getMoneyactual12());
                                            dat.setTotalactual3q(dat.getMoneyactual10().add(dat.getMoneyactual11().add(dat.getMoneyactual12())));
                                            moneyouttotal3q = moneyouttotal3q.add(dat.getTotalactual3q());
                                            dat.setMoneyactual1(depTotal.get(0).getMoneyactual1());
                                            dat.setMoneyactual2(depTotal.get(0).getMoneyactual2());
                                            dat.setMoneyactual3(depTotal.get(0).getMoneyactual3());
                                            dat.setTotalactual4q(dat.getMoneyactual1().add(dat.getMoneyactual2().add(dat.getMoneyactual3())));
                                            moneyouttotal4q = moneyouttotal4q.add(dat.getTotalactual4q());
                                            dat.setTotalactual(dat.getTotalactual1q().add(dat.getTotalactual2q().add(dat.getTotalactual3q().add(dat.getTotalactual4q()))));
                                        }
                                        else if(i==6)
                                        {
                                            dat.setAmount("经费");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("6");
                                            dat.setMoneyactual4(fundingTotal.get(0).getMoneyactual4());
                                            dat.setMoneyactual5(fundingTotal.get(0).getMoneyactual5());
                                            dat.setMoneyactual6(fundingTotal.get(0).getMoneyactual6());
                                            dat.setTotalactual1q(dat.getMoneyactual4().add(dat.getMoneyactual5().add(dat.getMoneyactual6())));
                                            moneyouttotal1q = moneyouttotal1q.add(dat.getTotalactual1q());
                                            dat.setMoneyactual7(fundingTotal.get(0).getMoneyactual7());
                                            dat.setMoneyactual8(fundingTotal.get(0).getMoneyactual8());
                                            dat.setMoneyactual9(fundingTotal.get(0).getMoneyactual9());
                                            dat.setTotalactual2q(dat.getMoneyactual7().add(dat.getMoneyactual8().add(dat.getMoneyactual9())));
                                            moneyouttotal2q = moneyouttotal2q.add(dat.getTotalactual2q());
                                            dat.setMoneyactual10(fundingTotal.get(0).getMoneyactual10());
                                            dat.setMoneyactual11(fundingTotal.get(0).getMoneyactual11());
                                            dat.setMoneyactual12(fundingTotal.get(0).getMoneyactual12());
                                            dat.setTotalactual3q(dat.getMoneyactual10().add(dat.getMoneyactual11().add(dat.getMoneyactual12())));
                                            moneyouttotal3q = moneyouttotal3q.add(dat.getTotalactual3q());
                                            dat.setMoneyactual1(fundingTotal.get(0).getMoneyactual1());
                                            dat.setMoneyactual2(fundingTotal.get(0).getMoneyactual2());
                                            dat.setMoneyactual3(fundingTotal.get(0).getMoneyactual3());
                                            dat.setTotalactual4q(dat.getMoneyactual1().add(dat.getMoneyactual2().add(dat.getMoneyactual3())));
                                            moneyouttotal4q = moneyouttotal4q.add(dat.getTotalactual4q());
                                            dat.setTotalactual(dat.getTotalactual1q().add(dat.getTotalactual2q().add(dat.getTotalactual3q().add(dat.getTotalactual4q()))));
                                        }
                                        else if(i==7)
                                        {
                                            dat.setAmount("差旅费");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("7");
                                            dat.setMoneyactual4(travelTotal.get(0).getMoneyactual4());
                                            dat.setMoneyactual5(travelTotal.get(0).getMoneyactual5());
                                            dat.setMoneyactual6(travelTotal.get(0).getMoneyactual6());
                                            dat.setTotalactual1q(dat.getMoneyactual4().add(dat.getMoneyactual5().add(dat.getMoneyactual6())));
                                            moneyouttotal1q = moneyouttotal1q.add(dat.getTotalactual1q());
                                            dat.setMoneyactual7(travelTotal.get(0).getMoneyactual7());
                                            dat.setMoneyactual8(travelTotal.get(0).getMoneyactual8());
                                            dat.setMoneyactual9(travelTotal.get(0).getMoneyactual9());
                                            dat.setTotalactual2q(dat.getMoneyactual7().add(dat.getMoneyactual8().add(dat.getMoneyactual9())));
                                            moneyouttotal2q = moneyouttotal2q.add(dat.getTotalactual2q());
                                            dat.setMoneyactual10(travelTotal.get(0).getMoneyactual10());
                                            dat.setMoneyactual11(travelTotal.get(0).getMoneyactual11());
                                            dat.setMoneyactual12(travelTotal.get(0).getMoneyactual12());
                                            dat.setTotalactual3q(dat.getMoneyactual10().add(dat.getMoneyactual11().add(dat.getMoneyactual12())));
                                            moneyouttotal3q = moneyouttotal3q.add(dat.getTotalactual3q());
                                            dat.setMoneyactual1(travelTotal.get(0).getMoneyactual1());
                                            dat.setMoneyactual2(travelTotal.get(0).getMoneyactual2());
                                            dat.setMoneyactual3(travelTotal.get(0).getMoneyactual3());
                                            dat.setTotalactual4q(dat.getMoneyactual1().add(dat.getMoneyactual2().add(dat.getMoneyactual3())));
                                            moneyouttotal4q = moneyouttotal4q.add(dat.getTotalactual4q());
                                            dat.setTotalactual(dat.getTotalactual1q().add(dat.getTotalactual2q().add(dat.getTotalactual3q().add(dat.getTotalactual4q()))));
                                        }
                                        else
                                        {
                                            dat.setAmount("边界利润率");
                                            dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                            dat.setIndextype("8");
                                            dat.setMoneyactual4(BigDecimal.ZERO);
                                            dat.setMoneyactual5(BigDecimal.ZERO);
                                            dat.setMoneyactual6((moneyintotal1q.subtract(moneyouttotal1q)).divide((moneyintotal1q.compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ONE:moneyintotal1q),2,BigDecimal.ROUND_HALF_UP));
                                            dat.setTotalactual1q(BigDecimal.ZERO);
                                            dat.setMoneyactual7(BigDecimal.ZERO);
                                            dat.setMoneyactual8(BigDecimal.ZERO);
                                            dat.setMoneyactual9((moneyintotal2q.subtract(moneyouttotal2q)).divide((moneyintotal2q.compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ONE:moneyintotal2q),2,BigDecimal.ROUND_HALF_UP));
                                            dat.setTotalactual2q(BigDecimal.ZERO);
                                            dat.setMoneyactual10(BigDecimal.ZERO);
                                            dat.setMoneyactual11(BigDecimal.ZERO);
                                            dat.setMoneyactual12((moneyintotal3q.subtract(moneyouttotal3q)).divide((moneyintotal3q.compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ONE:moneyintotal3q),2,BigDecimal.ROUND_HALF_UP));
                                            dat.setTotalactual3q(BigDecimal.ZERO);
                                            dat.setMoneyactual1(BigDecimal.ZERO);
                                            dat.setMoneyactual2(BigDecimal.ZERO);
                                            dat.setMoneyactual3((moneyintotal4q.subtract(moneyouttotal4q)).divide((moneyintotal4q.compareTo(BigDecimal.ZERO)==0 ? BigDecimal.ONE:moneyintotal4q),2,BigDecimal.ROUND_HALF_UP));
                                            dat.setTotalactual4q(BigDecimal.ZERO);
                                            dat.setTotalactual(BigDecimal.ZERO);
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
                        //RESION ADD CCM 20210810 下方合计增加经费和差旅费 fr
                        //查询部门共通填写的日志统计
                        List<DepartmentTotalVo> departmentTotalVoCommen = departmentAccountMapper.selectTotalByYearAndDep(String.valueOf(year),dv.getDepartmentId());
                        if(departmentTotalVoCommen.size() == 0)
                        {
                            departmentTotalVoCommen.add(new DepartmentTotalVo());
                        }
                        //查询表中，当前部门，当前年度，是否存在部门共通
                        List<DepartmentAccountTotal> dTotalInsert = new ArrayList<>();
                        List<DepartmentAccountTotal> dTotalUpdate = new ArrayList<>();
                        List<DepartmentAccountTotal> DepartmentAccountTotalCommon = new ArrayList<>();
                        DepartmentAccountTotalCommon = departmentAccountMapper.selectTotal(String.valueOf(year),dv.getDepartmentId(),"部门共通");
                        if(DepartmentAccountTotalCommon.size()>0)
                        {
                            //更新
                            //当前月员工工数
                            BigDecimal mountinside = BigDecimal.ZERO;
                            //当前月收入合计
                            BigDecimal moneyintotal = BigDecimal.ZERO;
                            //当前月支出合计
                            BigDecimal moneyouttotal = BigDecimal.ZERO;
                            for(DepartmentAccountTotal dattemp:DepartmentAccountTotalCommon)
                            {
                                //通过theme ，对集合进行对应月份的合计收入统计
                                switch (monthlast)
                                {
                                    case 0:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算12月的工数
                                            List<DepartmentTotalVo> dep12 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("12"))).collect(Collectors.toList());
                                            if(dep12.size()>0)
                                            {
                                                dattemp.setMoneyactual12(dep12.get(0).getMOUNT());
                                            }
                                            //第三季度 10,11,12
                                            dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual12();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual12(mountinside.multiply(personalmoney));
                                            //第三季度 10,11,12
                                            dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                            moneyouttotal = dattemp.getTotalactual3q();
                                        }

                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 1:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算1月的工数
                                            List<DepartmentTotalVo> dep1 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("01"))).collect(Collectors.toList());
                                            if(dep1.size()>0)
                                            {
                                                dattemp.setMoneyactual1(dep1.get(0).getMOUNT());
                                            }
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual1();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual1(mountinside.multiply(personalmoney));
                                        }

                                        //第四季度 1,2,3
                                        dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 2:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算2月的工数
                                            List<DepartmentTotalVo> dep2 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("02"))).collect(Collectors.toList());
                                            if(dep2.size()>0)
                                            {
                                                dattemp.setMoneyactual2(dep2.get(0).getMOUNT());
                                            }
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual2();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual2(mountinside.multiply(personalmoney));
                                        }
                                        //第四季度 1,2,3
                                        dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 3:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算3月的工数
                                            List<DepartmentTotalVo> dep3 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("03"))).collect(Collectors.toList());
                                            if(dep3.size()>0)
                                            {
                                                dattemp.setMoneyactual3(dep3.get(0).getMOUNT());
                                            }
                                            //第四季度 1,2,3
                                            dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual3();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual3(mountinside.multiply(personalmoney));
                                            //第四季度 1,2,3
                                            dattemp.setTotalactual4q(dattemp.getMoneyactual1().add(dattemp.getMoneyactual2().add(dattemp.getMoneyactual3())));
                                            moneyouttotal = dattemp.getTotalactual4q();
                                        }
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 4:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算4月的工数
                                            List<DepartmentTotalVo> dep4 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("04"))).collect(Collectors.toList());
                                            if(dep4.size()>0)
                                            {
                                                dattemp.setMoneyactual4(dep4.get(0).getMOUNT());
                                            }
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual4();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual4(mountinside.multiply(personalmoney));
                                        }
                                        //第一季度 4,5,6
                                        dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 5:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算5月的工数
                                            List<DepartmentTotalVo> dep5 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("05"))).collect(Collectors.toList());
                                            if(dep5.size()>0)
                                            {
                                                dattemp.setMoneyactual5(dep5.get(0).getMOUNT());
                                            }
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual5();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual5(mountinside.multiply(personalmoney));
                                        }
                                        //第一季度 4,5,6
                                        dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 6:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算6月的工数
                                            List<DepartmentTotalVo> dep6 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("06"))).collect(Collectors.toList());
                                            if(dep6.size()>0)
                                            {
                                                dattemp.setMoneyactual6(dep6.get(0).getMOUNT());
                                            }
                                            //第一季度 4,5,6
                                            dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual6();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual6(mountinside.multiply(personalmoney));
                                            //第一季度 4,5,6
                                            dattemp.setTotalactual1q(dattemp.getMoneyactual4().add(dattemp.getMoneyactual5().add(dattemp.getMoneyactual6())));
                                            moneyouttotal = dattemp.getTotalactual1q();
                                        }
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 7:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算7月的工数
                                            List<DepartmentTotalVo> dep7 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("07"))).collect(Collectors.toList());
                                            if(dep7.size()>0)
                                            {
                                                dattemp.setMoneyactual7(dep7.get(0).getMOUNT());
                                            }
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual7();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual7(mountinside.multiply(personalmoney));
                                        }
                                        //第二季度 7,8,9
                                        dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 8:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算8月的工数
                                            List<DepartmentTotalVo> dep8 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("08"))).collect(Collectors.toList());
                                            if(dep8.size()>0)
                                            {
                                                dattemp.setMoneyactual8(dep8.get(0).getMOUNT());
                                            }
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual8();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual8(mountinside.multiply(personalmoney));
                                        }
                                        //第二季度 7,8,9
                                        dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 9:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算9月的工数
                                            List<DepartmentTotalVo> dep9 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("09"))).collect(Collectors.toList());
                                            if(dep9.size()>0)
                                            {
                                                dattemp.setMoneyactual9(dep9.get(0).getMOUNT());
                                            }
                                            //第二季度 7,8,9
                                            dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual9();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual9(mountinside.multiply(personalmoney));
                                            //第二季度 7,8,9
                                            dattemp.setTotalactual2q(dattemp.getMoneyactual7().add(dattemp.getMoneyactual8().add(dattemp.getMoneyactual9())));
                                            moneyouttotal = dattemp.getTotalactual2q();
                                        }
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 10:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算10月的工数
                                            List<DepartmentTotalVo> dep10 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("10"))).collect(Collectors.toList());
                                            if(dep10.size()>0)
                                            {
                                                dattemp.setMoneyactual10(dep10.get(0).getMOUNT());
                                            }
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual10();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual10(mountinside.multiply(personalmoney));
                                        }
                                        //第三季度 10,11,12
                                        dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                    case 11:
                                        if(dattemp.getAmount().equals("员工工数(人月)"))
                                        {
                                            //社内员工计算11月的工数
                                            List<DepartmentTotalVo> dep11 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("11"))).collect(Collectors.toList());
                                            if(dep11.size()>0)
                                            {
                                                dattemp.setMoneyactual11(dep11.get(0).getMOUNT());
                                            }
                                            //保留本月的社内工数
                                            mountinside = dattemp.getMoneyactual11();
                                        }
                                        else if(dattemp.getAmount().equals("员工支出"))
                                        {
                                            //使用工数*全公司人件费
                                            dattemp.setMoneyactual11(mountinside.multiply(personalmoney));
                                        }
                                        //第三季度 10,11,12
                                        dattemp.setTotalactual3q(dattemp.getMoneyactual10().add(dattemp.getMoneyactual11().add(dattemp.getMoneyactual12())));
                                        //全年
                                        dattemp.setTotalactual(dattemp.getTotalactual1q().add(dattemp.getTotalactual2q().add(dattemp.getTotalactual3q().add(dattemp.getTotalactual4q()))));
                                        break;
                                }
                                dattemp.preUpdate(tokenModel);
                                dTotalUpdate.add(dattemp);
                            }
                        }
                        else
                        {
                            //插入
                            //4月员工工数
                            BigDecimal mountinside4 = BigDecimal.ZERO;
                            //5月员工工数
                            BigDecimal mountinside5 = BigDecimal.ZERO;
                            //6月员工工数
                            BigDecimal mountinside6 = BigDecimal.ZERO;
                            //7月员工工数
                            BigDecimal mountinside7 = BigDecimal.ZERO;
                            //8月员工工数
                            BigDecimal mountinside8 = BigDecimal.ZERO;
                            //9月员工工数
                            BigDecimal mountinside9 = BigDecimal.ZERO;
                            //10月员工工数
                            BigDecimal mountinside10 = BigDecimal.ZERO;
                            //11月员工工数
                            BigDecimal mountinside11 = BigDecimal.ZERO;
                            //12月员工工数
                            BigDecimal mountinside12 = BigDecimal.ZERO;
                            //1月员工工数
                            BigDecimal mountinside1 = BigDecimal.ZERO;
                            //2月员工工数
                            BigDecimal mountinside2= BigDecimal.ZERO;
                            //3月员工工数
                            BigDecimal mountinside3= BigDecimal.ZERO;
                            //第一季度收入合计
                            BigDecimal moneyintotal1q = BigDecimal.ZERO;
                            //第二季度收入合计
                            BigDecimal moneyintotal2q = BigDecimal.ZERO;
                            //第三季度收入合计
                            BigDecimal moneyintotal3q = BigDecimal.ZERO;
                            //第四季度收入合计
                            BigDecimal moneyintotal4q = BigDecimal.ZERO;
                            //第一季度支出合计
                            BigDecimal moneyouttotal1q = BigDecimal.ZERO;
                            //第二季度支出合计
                            BigDecimal moneyouttotal2q = BigDecimal.ZERO;
                            //第三季度支出合计
                            BigDecimal moneyouttotal3q = BigDecimal.ZERO;
                            //第四季度支出合计
                            BigDecimal moneyouttotal4q = BigDecimal.ZERO;
                            for(int i=1;i<9;i++)
                            {
                                //收支合计的实体
                                DepartmentAccountTotal dat = new DepartmentAccountTotal();
                                dat.setYears(String.valueOf(year));
                                dat.setDepartment(dv.getDepartmentId());
                                dat.setTheme_id("部门共通");
                                if(i==1)
                                {
                                    dat.setAmount("收入合计");
                                    dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                    dat.setIndextype("1");
                                    dat.setMoneyactual4(BigDecimal.ZERO);
                                    dat.setMoneyactual5(BigDecimal.ZERO);
                                    dat.setMoneyactual6(BigDecimal.ZERO);
                                    dat.setTotalactual1q(BigDecimal.ZERO);
                                    dat.setMoneyactual7(BigDecimal.ZERO);
                                    dat.setMoneyactual8(BigDecimal.ZERO);
                                    dat.setMoneyactual9(BigDecimal.ZERO);
                                    dat.setTotalactual2q(BigDecimal.ZERO);
                                    dat.setMoneyactual10(BigDecimal.ZERO);
                                    dat.setMoneyactual11(BigDecimal.ZERO);
                                    dat.setMoneyactual12(BigDecimal.ZERO);
                                    dat.setTotalactual3q(BigDecimal.ZERO);
                                    dat.setMoneyactual1(BigDecimal.ZERO);
                                    dat.setMoneyactual2(BigDecimal.ZERO);
                                    dat.setMoneyactual3(BigDecimal.ZERO);
                                    dat.setTotalactual4q(BigDecimal.ZERO);
                                    dat.setTotalactual(BigDecimal.ZERO);
                                }
                                else if(i==2)
                                {
                                    dat.setAmount("员工工数(人月)");
                                    dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                    dat.setIndextype("2");

                                    //社内员工计算4月的工数
                                    List<DepartmentTotalVo> dep4 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("04"))).collect(Collectors.toList());
                                    if(dep4.size()>0)
                                    {
                                        dat.setMoneyactual4(dep4.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual4(BigDecimal.ZERO);
                                    }
                                    mountinside4 = dat.getMoneyactual4();
                                    //社内员工计算5月的工数
                                    List<DepartmentTotalVo> dep5 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("05"))).collect(Collectors.toList());
                                    if(dep5.size()>0)
                                    {
                                        dat.setMoneyactual5(dep5.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual5(BigDecimal.ZERO);
                                    }
                                    mountinside4 = dat.getMoneyactual4();
                                    //社内员工计算6月的工数
                                    List<DepartmentTotalVo> dep6 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("06"))).collect(Collectors.toList());
                                    if(dep6.size()>0)
                                    {
                                        dat.setMoneyactual6(dep6.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual6(BigDecimal.ZERO);
                                    }
                                    mountinside6 = dat.getMoneyactual6();
                                    dat.setTotalactual1q(dat.getMoneyactual4().add(dat.getMoneyactual5().add(dat.getMoneyactual6())));
                                    //社内员工计算7月的工数
                                    List<DepartmentTotalVo> dep7 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("07"))).collect(Collectors.toList());
                                    if(dep7.size()>0)
                                    {
                                        dat.setMoneyactual7(dep7.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual7(BigDecimal.ZERO);
                                    }
                                    mountinside7 = dat.getMoneyactual7();
                                    //社内员工计算8月的工数
                                    List<DepartmentTotalVo> dep8 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("08"))).collect(Collectors.toList());
                                    if(dep8.size()>0)
                                    {
                                        dat.setMoneyactual8(dep8.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual8(BigDecimal.ZERO);
                                    }
                                    mountinside8 = dat.getMoneyactual8();
                                    //社内员工计算9月的工数
                                    List<DepartmentTotalVo> dep9 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("09"))).collect(Collectors.toList());
                                    if(dep9.size()>0)
                                    {
                                        dat.setMoneyactual9(dep9.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual9(BigDecimal.ZERO);
                                    }
                                    mountinside9 = dat.getMoneyactual9();
                                    dat.setTotalactual2q(dat.getMoneyactual7().add(dat.getMoneyactual8().add(dat.getMoneyactual9())));
                                    //社内员工计算10月的工数
                                    List<DepartmentTotalVo> dep10 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("10"))).collect(Collectors.toList());
                                    if(dep10.size()>0)
                                    {
                                        dat.setMoneyactual10(dep10.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual10(BigDecimal.ZERO);
                                    }
                                    mountinside10 = dat.getMoneyactual10();
                                    //社内员工计算11月的工数
                                    List<DepartmentTotalVo> dep11 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("11"))).collect(Collectors.toList());
                                    if(dep11.size()>0)
                                    {
                                        dat.setMoneyactual11(dep11.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual11(BigDecimal.ZERO);
                                    }
                                    mountinside11 = dat.getMoneyactual11();
                                    //社内员工计算12月的工数
                                    List<DepartmentTotalVo> dep12 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("12"))).collect(Collectors.toList());
                                    if(dep12.size()>0)
                                    {
                                        dat.setMoneyactual12(dep12.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual12(BigDecimal.ZERO);
                                    }
                                    mountinside12 = dat.getMoneyactual12();
                                    dat.setTotalactual3q(dat.getMoneyactual10().add(dat.getMoneyactual11().add(dat.getMoneyactual12())));
                                    //社内员工计算1月的工数
                                    List<DepartmentTotalVo> dep1 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("01"))).collect(Collectors.toList());
                                    if(dep1.size()>0)
                                    {
                                        dat.setMoneyactual1(dep1.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual1(BigDecimal.ZERO);
                                    }
                                    mountinside1 = dat.getMoneyactual1();
                                    //社内员工计算2月的工数
                                    List<DepartmentTotalVo> dep2 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("02"))).collect(Collectors.toList());
                                    if(dep2.size()>0)
                                    {
                                        dat.setMoneyactual2(dep2.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual2(BigDecimal.ZERO);
                                    }
                                    mountinside2 = dat.getMoneyactual2();
                                    //社内员工计算3月的工数
                                    List<DepartmentTotalVo> dep3 = departmentTotalVoCommen.stream().filter(item -> (item.getLOGDATE().equals("03"))).collect(Collectors.toList());
                                    if(dep3.size()>0)
                                    {
                                        dat.setMoneyactual3(dep3.get(0).getMOUNT());
                                    }
                                    else
                                    {
                                        dat.setMoneyactual3(BigDecimal.ZERO);
                                    }
                                    mountinside3 = dat.getMoneyactual3();
                                    dat.setTotalactual4q(dat.getMoneyactual1().add(dat.getMoneyactual2().add(dat.getMoneyactual3())));
                                    dat.setTotalactual(dat.getTotalactual1q().add(dat.getTotalactual2q().add(dat.getTotalactual3q().add(dat.getTotalactual4q()))));
                                }
                                else if(i==3)
                                {
                                    dat.setAmount("员工支出");
                                    dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                    dat.setIndextype("3");
                                    dat.setMoneyactual4(mountinside4.multiply(personalmoney));
                                    dat.setMoneyactual5(mountinside5.multiply(personalmoney));
                                    dat.setMoneyactual6(mountinside6.multiply(personalmoney));
                                    dat.setTotalactual1q(dat.getMoneyactual4().add(dat.getMoneyactual5().add(dat.getMoneyactual6())));
                                    moneyouttotal1q = dat.getTotalactual1q();
                                    dat.setMoneyactual7(mountinside7.multiply(personalmoney));
                                    dat.setMoneyactual8(mountinside8.multiply(personalmoney));
                                    dat.setMoneyactual9(mountinside9.multiply(personalmoney));
                                    dat.setTotalactual2q(dat.getMoneyactual7().add(dat.getMoneyactual8().add(dat.getMoneyactual9())));
                                    moneyouttotal2q = dat.getTotalactual2q();
                                    dat.setMoneyactual10(mountinside10.multiply(personalmoney));
                                    dat.setMoneyactual11(mountinside11.multiply(personalmoney));
                                    dat.setMoneyactual12(mountinside12.multiply(personalmoney));
                                    dat.setTotalactual3q(dat.getMoneyactual10().add(dat.getMoneyactual11().add(dat.getMoneyactual12())));
                                    moneyouttotal3q = dat.getTotalactual3q();
                                    dat.setMoneyactual1(mountinside1.multiply(personalmoney));
                                    dat.setMoneyactual2(mountinside2.multiply(personalmoney));
                                    dat.setMoneyactual3(mountinside3.multiply(personalmoney));
                                    dat.setTotalactual4q(dat.getMoneyactual1().add(dat.getMoneyactual2().add(dat.getMoneyactual3())));
                                    moneyouttotal4q = dat.getTotalactual4q();
                                    dat.setTotalactual(dat.getTotalactual1q().add(dat.getTotalactual2q().add(dat.getTotalactual3q().add(dat.getTotalactual4q()))));
                                }
                                else if(i==4)
                                {
                                    dat.setAmount("外注工数(人月)");
                                    dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                    dat.setIndextype("4");
                                    //社外员工计算4月的工数
                                    dat.setMoneyactual4(BigDecimal.ZERO);
                                    //社外员工计算5月的工数
                                    dat.setMoneyactual5(BigDecimal.ZERO);
                                    //社外员工计算6月的工数
                                    dat.setMoneyactual6(BigDecimal.ZERO);
                                    dat.setTotalactual1q(dat.getMoneyactual4().add(dat.getMoneyactual5().add(dat.getMoneyactual6())));
                                    //社外员工计算7月的工数
                                    dat.setMoneyactual7(BigDecimal.ZERO);
                                    //社外员工计算8月的工数
                                    dat.setMoneyactual8(BigDecimal.ZERO);
                                    //社外员工计算9月的工数
                                    dat.setMoneyactual9(BigDecimal.ZERO);
                                    dat.setTotalactual2q(dat.getMoneyactual7().add(dat.getMoneyactual8().add(dat.getMoneyactual9())));
                                    //社外员工计算10月的工数
                                    dat.setMoneyactual10(BigDecimal.ZERO);
                                    //社外员工计算11月的工数
                                    dat.setMoneyactual11(BigDecimal.ZERO);
                                    //社外员工计算12月的工数
                                    dat.setMoneyactual12(BigDecimal.ZERO);
                                    dat.setTotalactual3q(dat.getMoneyactual10().add(dat.getMoneyactual11().add(dat.getMoneyactual12())));
                                    //社外员工计算1月的工数
                                    dat.setMoneyactual1(BigDecimal.ZERO);
                                    //社外员工计算2月的工数
                                    dat.setMoneyactual2(BigDecimal.ZERO);
                                    //社外员工计算3月的工数
                                    dat.setMoneyactual3(BigDecimal.ZERO);
                                    dat.setTotalactual4q(dat.getMoneyactual1().add(dat.getMoneyactual2().add(dat.getMoneyactual3())));
                                    dat.setTotalactual(dat.getTotalactual1q().add(dat.getTotalactual2q().add(dat.getTotalactual3q().add(dat.getTotalactual4q()))));
                                }
                                else if(i==5)
                                {
                                    dat.setAmount("外注支出");
                                    dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                    dat.setIndextype("5");
                                    dat.setMoneyactual4(BigDecimal.ZERO);
                                    dat.setMoneyactual5(BigDecimal.ZERO);
                                    dat.setMoneyactual6(BigDecimal.ZERO);
                                    dat.setTotalactual1q(dat.getMoneyactual4().add(dat.getMoneyactual5().add(dat.getMoneyactual6())));
                                    moneyouttotal1q = moneyouttotal1q.add(dat.getTotalactual1q());
                                    dat.setMoneyactual7(BigDecimal.ZERO);
                                    dat.setMoneyactual8(BigDecimal.ZERO);
                                    dat.setMoneyactual9(BigDecimal.ZERO);
                                    dat.setTotalactual2q(dat.getMoneyactual7().add(dat.getMoneyactual8().add(dat.getMoneyactual9())));
                                    moneyouttotal2q = moneyouttotal2q.add(dat.getTotalactual2q());
                                    dat.setMoneyactual10(BigDecimal.ZERO);
                                    dat.setMoneyactual11(BigDecimal.ZERO);
                                    dat.setMoneyactual12(BigDecimal.ZERO);
                                    dat.setTotalactual3q(dat.getMoneyactual10().add(dat.getMoneyactual11().add(dat.getMoneyactual12())));
                                    moneyouttotal3q = moneyouttotal3q.add(dat.getTotalactual3q());
                                    dat.setMoneyactual1(BigDecimal.ZERO);
                                    dat.setMoneyactual2(BigDecimal.ZERO);
                                    dat.setMoneyactual3(BigDecimal.ZERO);
                                    dat.setTotalactual4q(dat.getMoneyactual1().add(dat.getMoneyactual2().add(dat.getMoneyactual3())));
                                    moneyouttotal4q = moneyouttotal4q.add(dat.getTotalactual4q());
                                    dat.setTotalactual(dat.getTotalactual1q().add(dat.getTotalactual2q().add(dat.getTotalactual3q().add(dat.getTotalactual4q()))));
                                }
                                else if(i==8)
                                {
                                    dat.setAmount("边界利润率");
                                    dat.setDepartmentaccounttotalid(UUID.randomUUID().toString());
                                    dat.setIndextype("8");
                                    dat.setMoneyactual4(BigDecimal.ZERO);
                                    dat.setMoneyactual5(BigDecimal.ZERO);
                                    dat.setMoneyactual6(BigDecimal.ZERO);
                                    dat.setTotalactual1q(BigDecimal.ZERO);
                                    dat.setMoneyactual7(BigDecimal.ZERO);
                                    dat.setMoneyactual8(BigDecimal.ZERO);
                                    dat.setMoneyactual9(BigDecimal.ZERO);
                                    dat.setTotalactual2q(BigDecimal.ZERO);
                                    dat.setMoneyactual10(BigDecimal.ZERO);
                                    dat.setMoneyactual11(BigDecimal.ZERO);
                                    dat.setMoneyactual12(BigDecimal.ZERO);
                                    dat.setTotalactual3q(BigDecimal.ZERO);
                                    dat.setMoneyactual1(BigDecimal.ZERO);
                                    dat.setMoneyactual2(BigDecimal.ZERO);
                                    dat.setMoneyactual3(BigDecimal.ZERO);
                                    dat.setTotalactual4q(BigDecimal.ZERO);
                                    dat.setTotalactual(BigDecimal.ZERO);
                                }
                                else
                                {
                                    continue;
                                }
                                dat.preInsert();
                                dTotalInsert.add(dat);
                            }
                        }
                        try
                        {
                            if(dTotalInsert.size()>0)
                            {
                                //新建数据
                                departmentAccountMapper.insertDepTotalAll(dTotalInsert);
                            }
                            if(dTotalUpdate.size()>0)
                            {
                                //更新数据
                                departmentAccountMapper.updateDepTotalAll(dTotalUpdate);
                            }
                        }
                        catch (Exception e)
                        {
                            System.out.println(month+"月数据生成数据异常");
                        }
                        //ENDRESION ADD CCM 20210810 下方合计增加经费和差旅费 to
                    }
//                }
            }
        }
    }

    @Override
    public Object getTable1051infoReport(String year, String department) throws Exception {
        List<DepartmentAccount> departmentReturnList = new ArrayList<>();
        OrgTree org = orgTreeService.get(new OrgTree());
        DepartmentAccount departmentAccount = new DepartmentAccount();
        departmentAccount.setYears(year);
        departmentAccount.setDepartment(department);
        List<DepartmentAccount> departmentAccountList = departmentAccountMapper.select(departmentAccount);
        Map<String,List<DepartmentAccount>> departMap = departmentAccountList.stream()
                .collect(Collectors.groupingBy(DepartmentAccount::getDepartment));
        departMap.forEach((departid,depList) -> {
            DepartmentAccountTotal departmentAccountTotal = new DepartmentAccountTotal();
            departmentAccountTotal.setYears(year);
            departmentAccountTotal.setDepartment(department);
            departmentAccountTotal.setTheme_id(departid);
            departmentReturnList.addAll(depList);
            List<DepartmentAccountTotal> departmentAccountTotalList = new ArrayList<>();
            departmentAccountTotalList = departmentAccountTotalMapper.select(departmentAccountTotal);
            departmentAccountTotalList.sort(Comparator.comparing(DepartmentAccountTotal::getIndextype));
            departmentAccountTotalList.forEach(item -> {
                DepartmentAccount copydep = new DepartmentAccount();
                BeanUtils.copyProperties(item,copydep);
                copydep.setThemename(depList.get(0).getThemename());
                copydep.setContract(depList.get(0).getContract());
                copydep.setToolsorgs(depList.get(0).getToolsorgs());
                if(depList.get(0).getContract().equals("社内"))
                {
                    OrgTree orginfo = null;
                    try {
                        orginfo = orgTreeService.getOrgInfo(org, depList.get(0).getToolsorgs());
                        copydep.setToolsorgs(orginfo.getCompanyname());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                departmentReturnList.add(copydep);
            });
        });
        DepartmentAccountTotal gtDepart = new DepartmentAccountTotal();
        gtDepart.setDepartment(department);
        gtDepart.setTheme_id("部门共通");
        List<DepartmentAccountTotal> gtDepartList = departmentAccountTotalMapper.select(gtDepart);
        gtDepartList.forEach(gtItem ->{
            DepartmentAccount copyGtdep = new DepartmentAccount();
            BeanUtils.copyProperties(gtItem,copyGtdep);
            copyGtdep.setThemename("部门共通");
            copyGtdep.setContract("社内");
            copyGtdep.setToolsorgs("");
            departmentReturnList.add(copyGtdep);
        });
        departmentReturnList.sort(Comparator.comparing(DepartmentAccount::getThemename));
        return JSONObject.toJSON(departmentReturnList);
    }
}
