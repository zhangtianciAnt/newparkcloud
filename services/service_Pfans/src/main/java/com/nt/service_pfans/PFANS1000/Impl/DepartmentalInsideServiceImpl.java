package com.nt.service_pfans.PFANS1000.Impl;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.DepartmentalInside;
import com.nt.dao_Pfans.PFANS1000.StaffDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideBaseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideReturnVo;
import com.nt.dao_Pfans.PFANS1000.Vo.StaffWorkMonthInfoVo;
import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.dao_Pfans.PFANS5000.Vo.LogPersonReturnVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.DepartmentalInsideService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentalInsideMapper;
import com.nt.service_pfans.PFANS1000.mapper.StaffDetailMapper;
import com.nt.service_pfans.PFANS2000.PersonalCostService;
import com.nt.service_pfans.PFANS4000.mapper.PeoplewareFeeMapper;
import com.nt.service_pfans.PFANS5000.mapper.CompanyProjectsMapper;
import com.nt.service_pfans.PFANS5000.mapper.ProjectsystemMapper;
import com.nt.utils.BigDecimalUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentalInsideServiceImpl implements DepartmentalInsideService {

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private DepartmentalInsideMapper departmentalInsideMapper;

    @Autowired
    private StaffDetailMapper staffDetailMapper;

    @Autowired
    private PeoplewareFeeMapper peoplewarefeeMapper;

    @Autowired
    private PersonalCostService personalCostService;

    @Autowired
    private CompanyProjectsMapper companyProjectsMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ProjectsystemMapper projectsystemMapper;

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";


    @Override
    public void insert() throws Exception {
        List<DepartmentalInside> departmentalInsideListInsert = new ArrayList<>();
        List<DepartmentalInside> departmentalInsideListUnpdate = new ArrayList<>();
        List<Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
        Map<String,Dictionary> ranksMap = dictionaryRank.stream().collect(Collectors.toMap(Dictionary::getValue1, a -> a,(k1,k2)->k1));
        TokenModel tokenModel = new TokenModel();
        Calendar calendar = Calendar.getInstance();
        int year = 0;
        int monthlast = calendar.get(Calendar.MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (month >= 1 && month <= 4) {
            if (day >= 10) {
                year = calendar.get(Calendar.YEAR);
            } else {
                year = calendar.get(Calendar.YEAR) - 1;
            }
        } else {
            year = calendar.get(Calendar.YEAR);
        }
        Calendar calnew = Calendar.getInstance();
        List<DepartmentalInsideBaseVo> departmentalInsideBaseVoList = departmentalInsideMapper.getBaseInfo(String.valueOf(year));
        //没关联项目的合同
        List<DepartmentalInsideBaseVo> unconnectedList = departmentalInsideBaseVoList.stream()
                .filter(uncl ->(StringUtils.isNullOrEmpty(uncl.getCompanyprojects_id()))).collect(Collectors.toList());

        List<DepartmentalInsideBaseVo> disUnRusltList = unconnectedList.stream()
                .filter(distinctAnt(DepartmentalInsideBaseVo::getContractnumber))
                .collect(Collectors.toList());
        if(disUnRusltList.size() > 0){
            int finalYearDis = year;
            disUnRusltList.forEach(durl ->{
                DepartmentalInside disDepartInside = new DepartmentalInside();
                disDepartInside.setDepartmentalinside_id(UUID.randomUUID().toString());
                disDepartInside.setDepartment(durl.getGroup_id());
                disDepartInside.setThemeinfor_id(durl.getThemeinfor_id());
                disDepartInside.setThemename(durl.getThemename());
                disDepartInside.setDivide(durl.getDivide());
                disDepartInside.setToolsorgs(durl.getToolsorgs());
                disDepartInside.setContractnumber(durl.getContractnumber());
                disDepartInside.setClaimamount(durl.getClaimamount());
                disDepartInside.setEntrycondition(durl.getEntrycondition());
                disDepartInside.setContracatamountdetail(durl.getContracatamountdetail());
                disDepartInside.setYears(String.valueOf(finalYearDis));
                disDepartInside.preInsert(tokenModel);
                departmentalInsideListInsert.add(disDepartInside);
            });
        }

        //关联项目的合同
        List<DepartmentalInsideBaseVo> connectedList = departmentalInsideBaseVoList.stream()
                .filter(uncl ->(!StringUtils.isNullOrEmpty(uncl.getCompanyprojects_id()))).collect(Collectors.toList());


        Map<String, List<DepartmentalInsideBaseVo>> groupThemList = connectedList.stream()
                .collect(Collectors.groupingBy(DepartmentalInsideBaseVo::getThemeinfor_id));
        String monthStr = "";
        if (monthlast < 10) {
            monthStr = "0" + monthlast;
        }else{
            monthStr = String.valueOf(monthlast);
        }
        for (Map.Entry<String, List<DepartmentalInsideBaseVo>> entryDep : groupThemList.entrySet()) {
            List<String> departList = new ArrayList<>();
            departList = entryDep.getValue().stream().map(DepartmentalInsideBaseVo::getGroup_id).distinct().collect(Collectors.toList());
            List<String> projectList = new ArrayList<>();;
            projectList = entryDep.getValue().stream().map(DepartmentalInsideBaseVo::getCompanyprojects_id).distinct().collect(Collectors.toList());
            String LOG_DATE = String.valueOf(year) + "-" + monthStr;
            List<StaffWorkMonthInfoVo> staffWorkMonthInfoVoList = departmentalInsideMapper.getWorkInfo(LOG_DATE, departList, projectList);
            List<StaffWorkMonthInfoVo> inSwmIList = staffWorkMonthInfoVoList.stream().filter(ins ->
                !("1").equals(ins.getType())
            ).collect(Collectors.toList());
            List<StaffWorkMonthInfoVo> outSwmIList = staffWorkMonthInfoVoList.stream().filter(out ->
                    !StringUtils.isNullOrEmpty(out.getType()) && ("1").equals(out.getType())
            ).collect(Collectors.toList());
            List<Projectsystem> getTypeTwoList = projectsystemMapper.getTypeTwo(projectList);
            getTypeTwoList.forEach(tywo ->{
                try {
                    if(this.checkIn(tywo,LOG_DATE)){

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
            });
            PeoplewareFee peoplewareFee = new PeoplewareFee();
            peoplewareFee.setYear(String.valueOf(year));
            List<PeoplewareFee> peoplewareFeeList = peoplewarefeeMapper.select(peoplewareFee);
            Map<String, Map<String, Map<String, List<StaffWorkMonthInfoVo>>>> staffGroupMap =
                    inSwmIList.stream()
                            .filter(item -> !StringUtils.isNullOrEmpty(item.getGroup_id()) && !StringUtils.isNullOrEmpty(item.getProject_id()) && !StringUtils.isNullOrEmpty(item.getRank()))
                            .collect(Collectors.groupingBy(StaffWorkMonthInfoVo::getGroup_id,
                                    Collectors.groupingBy(StaffWorkMonthInfoVo::getProject_id,
                                            Collectors.groupingBy(StaffWorkMonthInfoVo::getRank))));
            int finalYear = year;
            staffGroupMap.forEach((dep, depList) -> {//部门
                Map<String, PeoplewareFee> peoplewareFeeMap = new HashMap<>();
                //人件费 获取实际成本变更 ztc fr
                PeoplewareFee peoFee = new PeoplewareFee();
                peoFee.setYear(String.valueOf(finalYear));
                peoFee.setGroupid(dep);
                List<PeoplewareFee> peoFeeList = peoplewarefeeMapper.select(peoFee);
                peoFeeList.forEach(poem -> {
                    peoplewareFeeMap.put(poem.getRanks(),poem);
                });
//                try {
//                    peoplewareFeeMap.put(dep,peoFeeList);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                //人件费 获取实际成本变更 ztc to
                DepartmentalInside departInsideSelect = new DepartmentalInside();
                departInsideSelect.setYears(String.valueOf(finalYear));
                departInsideSelect.setDepartment(dep);
                Map<String, PeoplewareFee> finalPeoplewareFeeMap = peoplewareFeeMap;
                depList.forEach((pro, proList) -> {//项目
                    departInsideSelect.setProject_id(pro);
                    List<DepartmentalInsideBaseVo> deBaseList = entryDep.getValue().stream()
                            .filter(filItem -> !filItem.getCompanyprojects_id().equals(pro)).collect(Collectors.toList());
                    if(deBaseList.size() != 0) {
                        proList.forEach((rank, rankList) -> {//rank
                            PeoplewareFee peoCost = finalPeoplewareFeeMap.get(ranksMap.get(rank).getValue1());
                            if (peoCost != null) {
                                departInsideSelect.setStaffrank(rank);
                                List<DepartmentalInside> getOldRanList = departmentalInsideMapper.select(departInsideSelect);
                                List<StaffWorkMonthInfoVo> rangRankList = new ArrayList<>();
                                rangRankList = rankList.stream().filter(range -> range.getTime_start() != null).collect(Collectors.toList());
                                String rankSum = "0.0";
                                rankSum = rangRankList.stream().map(i -> new BigDecimal(i.getTime_start())).reduce(BigDecimal.ZERO, BigDecimal::add).toString();
                                StaffDetail staffDetail = new StaffDetail();
                                try {
                                    departInsideSelect.setNumbers(this.projectListFee().get(pro));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                staffDetail.setIncondepartment(departInsideSelect.getDepartment());
                                staffDetail.setContractnumber(deBaseList.get(0).getContractnumber());
                                staffDetail.setAttf(departInsideSelect.getStaffrank());
                                List<StaffDetail> staffDetailList = staffDetailMapper.select(staffDetail);
                                if (getOldRanList.size() == 0) {
                                    //新增
                                    DepartmentalInside departmentalInside = new DepartmentalInside();
                                    BeanUtils.copyProperties(departInsideSelect, departmentalInside);
                                    departmentalInside.setDepartmentalinside_id(UUID.randomUUID().toString());
                                    departmentalInside.setThemeinfor_id(deBaseList.get(0).getThemeinfor_id());
                                    departmentalInside.setThemename(deBaseList.get(0).getThemename());
                                    departmentalInside.setDivide(deBaseList.get(0).getDivide());
                                    departmentalInside.setToolsorgs(deBaseList.get(0).getToolsorgs());
                                    departmentalInside.setContractnumber(deBaseList.get(0).getContractnumber());
                                    departmentalInside.setClaimamount(deBaseList.get(0).getClaimamount());
                                    departmentalInside.setEntrycondition(deBaseList.get(0).getEntrycondition());
                                    departmentalInside.setContracatamountdetail(deBaseList.get(0).getContracatamountdetail());
                                    switch (monthlast) {
                                        case 4:
                                            departmentalInside.setStaffcustactual04(rankSum);
                                            departmentalInside.setStaffcustplan04(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork04() : "0.0");
                                            break;
                                        case 5:
                                            departmentalInside.setStaffcustactual05(rankSum);
                                            departmentalInside.setStaffcustplan05(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork05() : "0.0");
                                            break;
                                        case 6:
                                            departmentalInside.setStaffcustactual06(rankSum);
                                            departmentalInside.setStaffcustplan06(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork06() : "0.0");
                                            break;
                                        case 7:
                                            departmentalInside.setStaffcustactual07(rankSum);
                                            departmentalInside.setStaffcustplan07(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork07() : "0.0");
                                            break;
                                        case 8:
                                            departmentalInside.setStaffcustactual08(rankSum);
                                            departmentalInside.setStaffcustplan08(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork08() : "0.0");
                                            break;
                                        case 9:
                                            departmentalInside.setStaffcustactual09(rankSum);
                                            departmentalInside.setStaffcustplan09(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork09() : "0.0");
                                            break;
                                        case 10:
                                            departmentalInside.setStaffcustactual10(rankSum);
                                            departmentalInside.setStaffcustplan10(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork10() : "0.0");
                                            break;
                                        case 11:
                                            departmentalInside.setStaffcustactual11(rankSum);
                                            departmentalInside.setStaffcustplan11(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork11() : "0.0");
                                            break;
                                        case 12:
                                            departmentalInside.setStaffcustactual12(rankSum);
                                            departmentalInside.setStaffcustplan12(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork12() : "0.0");
                                            break;
                                        case 1:
                                            departmentalInside.setStaffcustactual01(rankSum);
                                            departmentalInside.setStaffcustplan01(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork01() : "0.0");
                                            break;
                                        case 2:
                                            departmentalInside.setStaffcustactual02(rankSum);
                                            departmentalInside.setStaffcustplan02(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork02() : "0.0");
                                            break;
                                        case 3:
                                            departmentalInside.setStaffcustactual03(rankSum);
                                            departmentalInside.setStaffcustplan03(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork03() : "0.0");
                                            break;
                                    }
                                    List<PeoplewareFee> peoFilterList = peoplewareFeeList.stream().filter(peoplewareFee1 ->
                                            peoplewareFee1.getGroupid().equals(departmentalInside.getDepartment())
                                                    && peoplewareFee1.getRanks().equals(departmentalInside.getStaffrank())
                                    ).collect(Collectors.toList());
                                    if (monthlast == 4 || monthlast == 5 || monthlast == 6) {
                                        departmentalInside.setWorkdifferentfirst(
                                                String.valueOf(
                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan04())
                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan04()))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan05())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan05())))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan06())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan06())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual04())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual04())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual05())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual05())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual06())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual06())))
                                                )
                                        );
                                        departmentalInside.setRankdifferentfirst(
                                                String.valueOf(
                                                        (
                                                                (
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan04())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan04()))
                                                                                .multiply(
                                                                                        (peoFilterList.size() > 0
                                                                                                ? new BigDecimal(peoCost.getMonth4()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan05())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan05()))
                                                                                                .multiply(
                                                                                                        (peoFilterList.size() > 0
                                                                                                                ? new BigDecimal(peoCost.getMonth5()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan06())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan06()))
                                                                                        .multiply(
                                                                                                (peoFilterList.size() > 0
                                                                                                        ? new BigDecimal(peoCost.getMonth6()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).subtract(
                                                                (
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual04())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual04()))
                                                                                .multiply(
                                                                                        (peoFilterList.size() > 0
                                                                                                ? new BigDecimal(peoFilterList.get(0).getMonth4()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual05())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual05()))
                                                                                                .multiply(
                                                                                                        (peoFilterList.size() > 0
                                                                                                                ? new BigDecimal(peoFilterList.get(0).getMonth5()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual06())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual06()))
                                                                                        .multiply(
                                                                                                (peoFilterList.size() > 0
                                                                                                        ? new BigDecimal(peoFilterList.get(0).getMonth6()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).setScale(2, BigDecimal.ROUND_HALF_UP)
                                                )
                                        );
                                    }
                                    else if (monthlast == 7 || monthlast == 8 || monthlast == 9) {
                                        departmentalInside.setWorkdifferentsecond(
                                                String.valueOf(
                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan07())
                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan07()))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan08())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan08())))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan09())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan09())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual07())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual07())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual08())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual08())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual09())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual09())))
                                                )
                                        );
                                        departmentalInside.setRankdifferentsecond(
                                                String.valueOf(
                                                        (
                                                                (
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan07())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan07()))
                                                                                .multiply(
                                                                                        (peoFilterList.size() > 0
                                                                                                ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan08())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan08()))
                                                                                                .multiply(
                                                                                                        (peoFilterList.size() > 0
                                                                                                                ? new BigDecimal(peoCost.getMonth8()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan09())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan09()))
                                                                                        .multiply(
                                                                                                (peoFilterList.size() > 0
                                                                                                        ? new BigDecimal(peoCost.getMonth9()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).subtract(
                                                                (
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual07())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual07()))
                                                                                .multiply(
                                                                                        (peoFilterList.size() > 0
                                                                                                ? new BigDecimal(peoFilterList.get(0).getMonth7()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual08())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual08()))
                                                                                                .multiply(
                                                                                                        (peoFilterList.size() > 0
                                                                                                                ? new BigDecimal(peoFilterList.get(0).getMonth8()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual09())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual09()))
                                                                                        .multiply(
                                                                                                (peoFilterList.size() > 0
                                                                                                        ? new BigDecimal(peoFilterList.get(0).getMonth9()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).setScale(2, BigDecimal.ROUND_HALF_UP)
                                                )
                                        );
                                    }
                                    else if (monthlast == 10 || monthlast == 11 || monthlast == 12) {
                                        departmentalInside.setWorkdifferentthird(
                                                String.valueOf(
                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan10())
                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan10()))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan11())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan11())))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan12())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan12())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual10())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual10())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual11())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual11())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual12())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual12())))
                                                )
                                        );
                                        departmentalInside.setRankdifferentthird(
                                                String.valueOf(
                                                        (
                                                                (
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan10())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan10()))
                                                                                .multiply(
                                                                                        (peoFilterList.size() > 0
                                                                                                ? new BigDecimal(peoCost.getMonth10()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan11())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan11()))
                                                                                                .multiply(
                                                                                                        (peoFilterList.size() > 0
                                                                                                                ? new BigDecimal(peoCost.getMonth11()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan12())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan12()))
                                                                                        .multiply(
                                                                                                (peoFilterList.size() > 0
                                                                                                        ? new BigDecimal(peoCost.getMonth12()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).subtract(
                                                                (
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual10())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual10()))
                                                                                .multiply(
                                                                                        (peoFilterList.size() > 0
                                                                                                ? new BigDecimal(peoFilterList.get(0).getMonth10()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual11())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual11()))
                                                                                                .multiply(
                                                                                                        (peoFilterList.size() > 0
                                                                                                                ? new BigDecimal(peoFilterList.get(0).getMonth11()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual12())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual12()))
                                                                                        .multiply(
                                                                                                (peoFilterList.size() > 0
                                                                                                        ? new BigDecimal(peoFilterList.get(0).getMonth12()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).setScale(2, BigDecimal.ROUND_HALF_UP)
                                                )
                                        );
                                    }
                                    else if (monthlast == 1 || monthlast == 2 || monthlast == 3) {
                                        departmentalInside.setWorkdifferentfourth(
                                                String.valueOf(
                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan01())
                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan01()))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan02())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan02())))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan03())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan03())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual01())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual01())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual02())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual02())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual03())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual03())))
                                                )
                                        );
                                        departmentalInside.setRankdifferentfourth(
                                                String.valueOf(
                                                        (
                                                                (
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan01())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan01()))
                                                                                .multiply(
                                                                                        (peoFilterList.size() > 0
                                                                                                ? new BigDecimal(peoCost.getMonth1()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan02())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan02()))
                                                                                                .multiply(
                                                                                                        (peoFilterList.size() > 0
                                                                                                                ? new BigDecimal(peoCost.getMonth2()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan03())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan03()))
                                                                                        .multiply(
                                                                                                (peoFilterList.size() > 0
                                                                                                        ? new BigDecimal(peoCost.getMonth3()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).subtract(
                                                                (
                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual01())
                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual01()))
                                                                                .multiply(
                                                                                        (peoFilterList.size() > 0
                                                                                                ? new BigDecimal(peoFilterList.get(0).getMonth1()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual02())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual02()))
                                                                                                .multiply(
                                                                                                        (peoFilterList.size() > 0
                                                                                                                ? new BigDecimal(peoFilterList.get(0).getMonth2()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual03())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual03()))
                                                                                        .multiply(
                                                                                                (peoFilterList.size() > 0
                                                                                                        ? new BigDecimal(peoFilterList.get(0).getMonth3()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).setScale(2, BigDecimal.ROUND_HALF_UP)
                                                )
                                        );
                                    }
                                    departmentalInside.setWorkdifferentofyear(this.sumWorkAnt(departmentalInside));
                                    departmentalInside.setRankdifferentofyear(this.sumRankAnt(departmentalInside));
                                    departmentalInside.preInsert(tokenModel);
                                    departmentalInsideListInsert.add(departmentalInside);
                                }
                                else {
                                    switch (monthlast) {
                                        case 0:
                                            getOldRanList.get(0).setStaffcustactual12(rankSum);
                                            getOldRanList.get(0).setStaffcustplan12(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork12() : "0.0");
                                            break;
                                        case 1:
                                            getOldRanList.get(0).setStaffcustactual01(rankSum);
                                            getOldRanList.get(0).setStaffcustplan01(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork01() : "0.0");
                                            break;
                                        case 2:
                                            getOldRanList.get(0).setStaffcustactual02(rankSum);
                                            getOldRanList.get(0).setStaffcustplan02(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork02() : "0.0");
                                            break;
                                        case 3:
                                            getOldRanList.get(0).setStaffcustactual03(rankSum);
                                            getOldRanList.get(0).setStaffcustplan03(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork03() : "0.0");
                                            break;
                                        case 4:
                                            getOldRanList.get(0).setStaffcustactual04(rankSum);
                                            getOldRanList.get(0).setStaffcustplan04(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork04() : "0.0");
                                            break;
                                        case 5:
                                            getOldRanList.get(0).setStaffcustactual05(rankSum);
                                            getOldRanList.get(0).setStaffcustplan05(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork05() : "0.0");
                                            break;
                                        case 6:
                                            getOldRanList.get(0).setStaffcustactual06(rankSum);
                                            getOldRanList.get(0).setStaffcustplan06(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork06() : "0.0");
                                            break;
                                        case 7:
                                            getOldRanList.get(0).setStaffcustactual07(rankSum);
                                            getOldRanList.get(0).setStaffcustplan07(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork07() : "0.0");
                                            break;
                                        case 8:
                                            getOldRanList.get(0).setStaffcustactual08(rankSum);
                                            getOldRanList.get(0).setStaffcustplan08(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork08() : "0.0");
                                            break;
                                        case 9:
                                            getOldRanList.get(0).setStaffcustactual09(rankSum);
                                            getOldRanList.get(0).setStaffcustplan09(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork09() : "0.0");
                                            break;
                                        case 10:
                                            getOldRanList.get(0).setStaffcustactual10(rankSum);
                                            getOldRanList.get(0).setStaffcustplan10(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork10() : "0.0");
                                            break;
                                        case 11:
                                            getOldRanList.get(0).setStaffcustactual11(rankSum);
                                            getOldRanList.get(0).setStaffcustplan11(staffDetailList.size() > 0 ?
                                                    staffDetailList.get(0).getInwork11() : "0.0");
                                            break;
                                    }
                                    List<PeoplewareFee> peoFilterListUpdate = peoplewareFeeList.stream().filter(peoplewareFee1 ->
                                            peoplewareFee1.getGroupid().equals(getOldRanList.get(0).getDepartment())
                                                    && peoplewareFee1.getRanks().equals(getOldRanList.get(0).getStaffrank())
                                    ).collect(Collectors.toList());
                                    if (monthlast == 3 || monthlast == 4 || monthlast == 5) {
                                        getOldRanList.get(0).setWorkdifferentfirst(
                                                String.valueOf(
                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan04())
                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan04()))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan05())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan05())))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan06())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan06())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual04())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual04())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual05())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual05())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual06())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual06())))
                                                )
                                        );
                                        getOldRanList.get(0).setRankdifferentfirst(
                                                String.valueOf(
                                                        (
                                                                (
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan04())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan04()))
                                                                                .multiply(
                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                ? new BigDecimal(peoCost.getMonth4()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan05())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan05()))
                                                                                                .multiply(
                                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                                ? new BigDecimal(peoCost.getMonth5()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan06())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan06()))
                                                                                        .multiply(
                                                                                                (peoFilterListUpdate.size() > 0
                                                                                                        ? new BigDecimal(peoCost.getMonth6()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).subtract(
                                                                (
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual04())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual04()))
                                                                                .multiply(
                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                ? new BigDecimal(peoFilterListUpdate.get(0).getMonth4()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual05())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual05()))
                                                                                                .multiply(
                                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                                ? new BigDecimal(peoFilterListUpdate.get(0).getMonth5()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual06())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual06()))
                                                                                        .multiply(
                                                                                                (peoFilterListUpdate.size() > 0
                                                                                                        ? new BigDecimal(peoFilterListUpdate.get(0).getMonth6()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        )
                                                )
                                        );
                                    }
                                    else if (monthlast == 6 || monthlast == 7 || monthlast == 8) {
                                        getOldRanList.get(0).setWorkdifferentsecond(
                                                String.valueOf(
                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan07())
                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan07()))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan08())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan08())))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan09())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan09())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual07())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual07())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual08())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual08())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual09())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual09())))
                                                )
                                        );
                                        getOldRanList.get(0).setRankdifferentsecond(
                                                String.valueOf(
                                                        (
                                                                (
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan07())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan07()))
                                                                                .multiply(
                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan08())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan08()))
                                                                                                .multiply(
                                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                                ? new BigDecimal(peoCost.getMonth8()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan09())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan09()))
                                                                                        .multiply(
                                                                                                (peoFilterListUpdate.size() > 0
                                                                                                        ? new BigDecimal(peoCost.getMonth9()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).subtract(
                                                                (
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual07())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual07()))
                                                                                .multiply(
                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                ? new BigDecimal(peoFilterListUpdate.get(0).getMonth7()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual08())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual08()))
                                                                                                .multiply(
                                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                                ? new BigDecimal(peoFilterListUpdate.get(0).getMonth8()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual09())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual09()))
                                                                                        .multiply(
                                                                                                (peoFilterListUpdate.size() > 0
                                                                                                        ? new BigDecimal(peoFilterListUpdate.get(0).getMonth9()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        )
                                                )
                                        );
                                    }
                                    else if (monthlast == 9 || monthlast == 10 || monthlast == 11) {
                                        getOldRanList.get(0).setWorkdifferentthird(
                                                String.valueOf(
                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan10())
                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan10()))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan11())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan11())))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan12())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan12())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual10())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual10())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual11())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual11())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual12())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual12())))
                                                )
                                        );
                                        getOldRanList.get(0).setRankdifferentthird(
                                                String.valueOf(
                                                        (
                                                                (
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan10())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan10()))
                                                                                .multiply(
                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                ? new BigDecimal(peoCost.getMonth10()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan11())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan11()))
                                                                                                .multiply(
                                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                                ? new BigDecimal(peoCost.getMonth11()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan12())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan12()))
                                                                                        .multiply(
                                                                                                (peoFilterListUpdate.size() > 0
                                                                                                        ? new BigDecimal(peoCost.getMonth12()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).subtract(
                                                                (
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual10())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual10()))
                                                                                .multiply(
                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                ? new BigDecimal(peoFilterListUpdate.get(0).getMonth10()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual11())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual11()))
                                                                                                .multiply(
                                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                                ? new BigDecimal(peoFilterListUpdate.get(0).getMonth11()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual12())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual12()))
                                                                                        .multiply(
                                                                                                (peoFilterListUpdate.size() > 0
                                                                                                        ? new BigDecimal(peoFilterListUpdate.get(0).getMonth12()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        )
                                                )
                                        );
                                    }
                                    else if (monthlast == 0 || monthlast == 1 || monthlast == 2) {
                                        getOldRanList.get(0).setWorkdifferentfourth(
                                                String.valueOf(
                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan01())
                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan01()))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan02())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan02())))
                                                                .add(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan03())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan03())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual01())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual01())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual02())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual02())))
                                                                .subtract(
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual03())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual03())))
                                                )
                                        );
                                        getOldRanList.get(0).setRankdifferentthird(
                                                String.valueOf(
                                                        (
                                                                (
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan01())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan01()))
                                                                                .multiply(
                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                ? new BigDecimal(peoCost.getMonth1()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan02())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan02()))
                                                                                                .multiply(
                                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                                ? new BigDecimal(peoCost.getMonth2()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan03())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan03()))
                                                                                        .multiply(
                                                                                                (peoFilterListUpdate.size() > 0
                                                                                                        ? new BigDecimal(peoCost.getMonth3()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        ).subtract(
                                                                (
                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual01())
                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual01()))
                                                                                .multiply(
                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                ? new BigDecimal(peoFilterListUpdate.get(0).getMonth1()) : BigDecimal.ZERO)
                                                                                )
                                                                )
                                                                        .add(
                                                                                (
                                                                                        (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual02())
                                                                                                ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual02()))
                                                                                                .multiply(
                                                                                                        (peoFilterListUpdate.size() > 0
                                                                                                                ? new BigDecimal(peoFilterListUpdate.get(0).getMonth2()) : BigDecimal.ZERO)
                                                                                                )
                                                                                )
                                                                        ).add(
                                                                        (
                                                                                (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual03())
                                                                                        ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual03()))
                                                                                        .multiply(
                                                                                                (peoFilterListUpdate.size() > 0
                                                                                                        ? new BigDecimal(peoFilterListUpdate.get(0).getMonth3()) : BigDecimal.ZERO)
                                                                                        )
                                                                        )
                                                                )
                                                        )
                                                )
                                        );
                                    }
                                    getOldRanList.get(0).setWorkdifferentofyear(this.sumWorkAnt(getOldRanList.get(0)));
                                    getOldRanList.get(0).setRankdifferentofyear(this.sumRankAnt(getOldRanList.get(0)));
                                }
                            }
                        });
                    }
                });
            });
        }
        try {
            //新建数据
            if (departmentalInsideListInsert.size() > 0) {
                departmentalInsideMapper.insertDepInsAll(departmentalInsideListInsert);
            }
            //更新数据
            if (departmentalInsideListUnpdate.size() > 0) {
                departmentalInsideMapper.updateDepInsAll(departmentalInsideListUnpdate);
            }
        } catch (Exception e) {
            System.out.println(month + "月数据生成异常");
        }
    }

    @Override
    public List<DepartmentalInsideReturnVo> getTableinfo(String year, String group_id) throws Exception{
        DepartmentalInside departmentalInside = new DepartmentalInside();
        departmentalInside.setYears(year);
        departmentalInside.setDepartment(group_id);
        List<DepartmentalInside> departmentalInsideList = departmentalInsideMapper.select(departmentalInside);
//        Map<String, Map<String, List<DepartmentalInside>>> filterMap =
//                departmentalInsideList.stream()
//                        .collect(Collectors.groupingBy(DepartmentalInside::getContractnumber,
//                                Collectors.groupingBy(DepartmentalInside::getProject_id)));
        Map<String, List<DepartmentalInside>> filterMap =
                departmentalInsideList.stream()
                        .collect(Collectors.groupingBy(DepartmentalInside::getContractnumber));
        for(DepartmentalInside depart : departmentalInsideList){
            if(depart.getEntrycondition().equals("HT004001")){
                depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getContracatamountdetail() + "-废弃" + "】");
                depart.setClaimamount("-");
            }else{
                if(filterMap.get(depart.getContractnumber()).size() == 1 && !StringUtils.isNullOrEmpty(depart.getProject_id())){
                    depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getContracatamountdetail() + "】");
                }else{
                    depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getClaimamount() + "】");
                }
            }
        }
        TreeMap<String,List<DepartmentalInside>> treeDepList =  departmentalInsideList.stream().collect(Collectors.groupingBy(DepartmentalInside :: getThemeinfor_id,TreeMap::new,Collectors.toList()));
        List<DepartmentalInside> depInsList = new ArrayList<>();
        DepartmentalInsideReturnVo deInsideReturnHjVo = new DepartmentalInsideReturnVo();
        List<DepartmentalInsideReturnVo> returnList = new ArrayList<>();
        BigDecimal planHj04 = BigDecimal.ZERO;
        BigDecimal acalHj04 = BigDecimal.ZERO;
        BigDecimal planHj05 = BigDecimal.ZERO;
        BigDecimal acalHj05 = BigDecimal.ZERO;
        BigDecimal planHj06 = BigDecimal.ZERO;
        BigDecimal acalHj06 = BigDecimal.ZERO;
//        BigDecimal workHj01 = BigDecimal.ZERO;
//        BigDecimal rankHj01 = BigDecimal.ZERO;
        BigDecimal planHj07 = BigDecimal.ZERO;
        BigDecimal acalHj07 = BigDecimal.ZERO;
        BigDecimal planHj08 = BigDecimal.ZERO;
        BigDecimal acalHj08 = BigDecimal.ZERO;
        BigDecimal planHj09 = BigDecimal.ZERO;
        BigDecimal acalHj09 = BigDecimal.ZERO;
//        BigDecimal workHj02 = BigDecimal.ZERO;
//        BigDecimal rankHj02 = BigDecimal.ZERO;
        BigDecimal planHj10 = BigDecimal.ZERO;
        BigDecimal acalHj10 = BigDecimal.ZERO;
        BigDecimal planHj11 = BigDecimal.ZERO;
        BigDecimal acalHj11 = BigDecimal.ZERO;
        BigDecimal planHj12 = BigDecimal.ZERO;
        BigDecimal acalHj12 = BigDecimal.ZERO;
//        BigDecimal workHj03 = BigDecimal.ZERO;
//        BigDecimal rankHj03 = BigDecimal.ZERO;
        BigDecimal planHj01 = BigDecimal.ZERO;
        BigDecimal acalHj01 = BigDecimal.ZERO;
        BigDecimal planHj02 = BigDecimal.ZERO;
        BigDecimal acalHj02 = BigDecimal.ZERO;
        BigDecimal planHj03 = BigDecimal.ZERO;
        BigDecimal acalHj03 = BigDecimal.ZERO;
//        BigDecimal workHj04 = BigDecimal.ZERO;
//        BigDecimal rankHj04 = BigDecimal.ZERO;
        BigDecimal workHjoy = BigDecimal.ZERO;
        BigDecimal rankHjoy = BigDecimal.ZERO;
        if(treeDepList.size() > 0){
            for (List<DepartmentalInside> value : treeDepList.values()) {
                DepartmentalInsideReturnVo departmentalInsideReturnVo = new DepartmentalInsideReturnVo();
                departmentalInsideReturnVo.setThemeinfor_id(value.get(0).getThemeinfor_id());
                departmentalInsideReturnVo.setThemename(value.get(0).getThemename());
                departmentalInsideReturnVo.setDivide(value.get(0).getDivide());
                departmentalInsideReturnVo.setToolsorgs(value.get(0).getToolsorgs());
                departmentalInsideReturnVo.setContractnumber("-");
                departmentalInsideReturnVo.setClaimamount("-");
                departmentalInsideReturnVo.setContracatamountdetail("-");
                departmentalInsideReturnVo.setClaimtype("-");
                departmentalInsideReturnVo.setProject_id("-");
                departmentalInsideReturnVo.setNumbers("-");
                departmentalInsideReturnVo.setStaffrank("-");
                departmentalInsideReturnVo.setStaffcustplan04("-");
                departmentalInsideReturnVo.setStaffcustactual04("-");
                departmentalInsideReturnVo.setStaffcustplan05("-");
                departmentalInsideReturnVo.setStaffcustactual05("-");
                departmentalInsideReturnVo.setStaffcustplan06("-");
                departmentalInsideReturnVo.setStaffcustactual06("-");
//                departmentalInsideReturnVo.setWorkdifferentfirst("-");
//                departmentalInsideReturnVo.setRankdifferentfirst("-");
                departmentalInsideReturnVo.setStaffcustplan07("-");
                departmentalInsideReturnVo.setStaffcustactual07("-");
                departmentalInsideReturnVo.setStaffcustplan08("-");
                departmentalInsideReturnVo.setStaffcustactual08("-");
                departmentalInsideReturnVo.setStaffcustplan09("-");
                departmentalInsideReturnVo.setStaffcustactual09("-");
//                departmentalInsideReturnVo.setWorkdifferentsecond("-");
//                departmentalInsideReturnVo.setRankdifferentsecond("-");
                departmentalInsideReturnVo.setStaffcustplan10("-");
                departmentalInsideReturnVo.setStaffcustactual10("-");
                departmentalInsideReturnVo.setStaffcustplan11("-");
                departmentalInsideReturnVo.setStaffcustactual11("-");
                departmentalInsideReturnVo.setStaffcustplan12("-");
                departmentalInsideReturnVo.setStaffcustactual12("-");
//                departmentalInsideReturnVo.setWorkdifferentthird("-");
//                departmentalInsideReturnVo.setRankdifferentthird("-");
                departmentalInsideReturnVo.setStaffcustplan01("-");
                departmentalInsideReturnVo.setStaffcustactual01("-");
                departmentalInsideReturnVo.setStaffcustplan02("-");
                departmentalInsideReturnVo.setStaffcustactual02("-");
                departmentalInsideReturnVo.setStaffcustplan03("-");
                departmentalInsideReturnVo.setStaffcustactual03("-");
//                departmentalInsideReturnVo.setWorkdifferentfourth("-");
//                departmentalInsideReturnVo.setRankdifferentfourth("-");
                departmentalInsideReturnVo.setWorkdifferentofyear("-");
                departmentalInsideReturnVo.setRankdifferentofyear("-");
                BigDecimal planXj04 = BigDecimal.ZERO;
                BigDecimal acalXj04 = BigDecimal.ZERO;
                BigDecimal planXj05 = BigDecimal.ZERO;
                BigDecimal acalXj05 = BigDecimal.ZERO;
                BigDecimal acalXj06 = BigDecimal.ZERO;
                BigDecimal planXj06 = BigDecimal.ZERO;
//                BigDecimal workXj01 = BigDecimal.ZERO;
//                BigDecimal rankXj01 = BigDecimal.ZERO;
                BigDecimal planXj07 = BigDecimal.ZERO;
                BigDecimal acalXj07 = BigDecimal.ZERO;
                BigDecimal planXj08 = BigDecimal.ZERO;
                BigDecimal acalXj08 = BigDecimal.ZERO;
                BigDecimal planXj09 = BigDecimal.ZERO;
                BigDecimal acalXj09 = BigDecimal.ZERO;
//                BigDecimal workXj02 = BigDecimal.ZERO;
//                BigDecimal rankXj02 = BigDecimal.ZERO;
                BigDecimal planXj10 = BigDecimal.ZERO;
                BigDecimal acalXj10 = BigDecimal.ZERO;
                BigDecimal planXj11 = BigDecimal.ZERO;
                BigDecimal acalXj11 = BigDecimal.ZERO;
                BigDecimal planXj12 = BigDecimal.ZERO;
                BigDecimal acalXj12 = BigDecimal.ZERO;
//                BigDecimal workXj03 = BigDecimal.ZERO;
//                BigDecimal rankXj03 = BigDecimal.ZERO;
                BigDecimal planXj01 = BigDecimal.ZERO;
                BigDecimal acalXj01 = BigDecimal.ZERO;
                BigDecimal planXj02 = BigDecimal.ZERO;
                BigDecimal acalXj02 = BigDecimal.ZERO;
                BigDecimal planXj03 = BigDecimal.ZERO;
                BigDecimal acalXj03 = BigDecimal.ZERO;
//                BigDecimal workXj04 = BigDecimal.ZERO;
//                BigDecimal rankXj04 = BigDecimal.ZERO;
                BigDecimal workXjoy = BigDecimal.ZERO;
                BigDecimal rankXjoy = BigDecimal.ZERO;
                List<DepartmentalInside> reslutValue = new ArrayList<>();
                List<DepartmentalInside> unValue = value.stream()
                        .filter(unval ->(!StringUtils.isNullOrEmpty(unval.getProject_id()))).collect(Collectors.toList());
                unValue.sort(Comparator.comparing(DepartmentalInside::getClaimamount)
                        .thenComparing(DepartmentalInside::getProject_id).thenComparing(DepartmentalInside::getStaffrank));
                List<DepartmentalInside> inValue = value.stream()
                        .filter(unval ->(StringUtils.isNullOrEmpty(unval.getProject_id()))).collect(Collectors.toList());
                reslutValue.addAll(inValue);
                reslutValue.addAll(unValue);
//                value.sort(Comparator.comparing(DepartmentalInside::getClaimamount)
//                        .thenComparing(DepartmentalInside::getProject_id).thenComparing(DepartmentalInside::getStaffrank));
                for(DepartmentalInside getXj : unValue){
                    planXj04 = planXj04.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan04()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan04()));
                    acalXj04 = acalXj04.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual04()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual04()));
                    planXj05 = planXj05.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan05()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan05()));
                    acalXj05 = acalXj05.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual05()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual05()));
                    planXj06 = planXj06.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan06()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan06()));
                    acalXj06 = acalXj06.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual06()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual06()));
//                    workXj01 = workXj01.add(StringUtils.isNullOrEmpty(getXj.getWorkdifferentfirst()) ? BigDecimal.ZERO : new BigDecimal(getXj.getWorkdifferentfirst()));
//                    rankXj01 = rankXj01.add(StringUtils.isNullOrEmpty(getXj.getRankdifferentfirst()) ? BigDecimal.ZERO : new BigDecimal(getXj.getRankdifferentfirst()));
                    planXj07 = planXj07.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan07()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan07()));
                    acalXj07 = acalXj07.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual07()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual07()));
                    planXj08 = planXj08.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan08()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan08()));
                    acalXj08 = acalXj08.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual08()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual08()));
                    planXj09 = planXj09.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan09()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan09()));
                    acalXj09 = acalXj09.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual09()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual09()));
//                    workXj02 = workXj02.add(StringUtils.isNullOrEmpty(getXj.getWorkdifferentsecond()) ? BigDecimal.ZERO : new BigDecimal(getXj.getWorkdifferentsecond()));
//                    rankXj02 = rankXj02.add(StringUtils.isNullOrEmpty(getXj.getRankdifferentsecond()) ? BigDecimal.ZERO : new BigDecimal(getXj.getRankdifferentsecond()));
                    planXj10 = planXj10.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan10()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan10()));
                    acalXj10 = acalXj10.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual10()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual10()));
                    planXj11 = planXj11.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan11()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan11()));
                    acalXj11 = acalXj11.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual11()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual11()));
                    planXj12 = planXj12.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan12()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan12()));
                    acalXj12 = acalXj12.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual12()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual12()));
//                    workXj03 = workXj03.add(StringUtils.isNullOrEmpty(getXj.getWorkdifferentthird()) ? BigDecimal.ZERO : new BigDecimal(getXj.getWorkdifferentthird()));
//                    rankXj03 = rankXj03.add(StringUtils.isNullOrEmpty(getXj.getRankdifferentthird()) ? BigDecimal.ZERO : new BigDecimal(getXj.getRankdifferentthird()));
                    planXj01 = planXj01.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan01()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan01()));
                    acalXj01 = acalXj01.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual01()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual01()));
                    planXj02 = planXj02.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan02()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan02()));
                    acalXj02 = acalXj02.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual02()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual02()));
                    planXj03 = planXj03.add(StringUtils.isNullOrEmpty(getXj.getStaffcustplan03()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustplan03()));
                    acalXj03 = acalXj03.add(StringUtils.isNullOrEmpty(getXj.getStaffcustactual03()) ? BigDecimal.ZERO : new BigDecimal(getXj.getStaffcustactual03()));
//                    workXj04 = workXj04.add(StringUtils.isNullOrEmpty(getXj.getWorkdifferentfourth()) ? BigDecimal.ZERO : new BigDecimal(getXj.getWorkdifferentfourth()));
//                    rankXj04 = rankXj04.add(StringUtils.isNullOrEmpty(getXj.getRankdifferentfourth()) ? BigDecimal.ZERO : new BigDecimal(getXj.getRankdifferentfourth()));
                    workXjoy = workXjoy.add(StringUtils.isNullOrEmpty(getXj.getWorkdifferentofyear()) ? BigDecimal.ZERO : new BigDecimal(getXj.getWorkdifferentofyear()));
                    rankXjoy = rankXjoy.add(StringUtils.isNullOrEmpty(getXj.getRankdifferentofyear()) ? BigDecimal.ZERO : new BigDecimal(getXj.getRankdifferentofyear()));
                };
                DepartmentalInside depInsListXj = new DepartmentalInside();
                depInsListXj.setThemename("小计");
                depInsListXj.setStaffcustplan04((planXj04).toString());
                depInsListXj.setStaffcustactual04((acalXj04).toString());
                depInsListXj.setStaffcustplan05((planXj05).toString());
                depInsListXj.setStaffcustactual05((acalXj05).toString());
                depInsListXj.setStaffcustplan06((planXj06).toString());
                depInsListXj.setStaffcustactual06((acalXj06).toString());
//                depInsListXj.setWorkdifferentfirst((workXj01).toString());
//                depInsListXj.setRankdifferentfirst((rankXj01).toString());
                depInsListXj.setStaffcustplan07((planXj07).toString());
                depInsListXj.setStaffcustactual07((acalXj07).toString());
                depInsListXj.setStaffcustplan08((planXj08).toString());
                depInsListXj.setStaffcustactual08((acalXj08).toString());
                depInsListXj.setStaffcustplan09((planXj09).toString());
                depInsListXj.setStaffcustactual09((acalXj09).toString());
//                depInsListXj.setWorkdifferentsecond((workXj02).toString());
//                depInsListXj.setRankdifferentsecond((rankXj02).toString());
                depInsListXj.setStaffcustplan10((planXj10).toString());
                depInsListXj.setStaffcustactual10((acalXj10).toString());
                depInsListXj.setStaffcustplan11((planXj11).toString());
                depInsListXj.setStaffcustactual11((acalXj11).toString());
                depInsListXj.setStaffcustplan12((planXj12).toString());
                depInsListXj.setStaffcustactual12((acalXj12).toString());
//                depInsListXj.setWorkdifferentthird((workXj03).toString());
//                depInsListXj.setRankdifferentthird((rankXj03).toString());
                depInsListXj.setStaffcustplan01((planXj01).toString());
                depInsListXj.setStaffcustactual01((acalXj01).toString());
                depInsListXj.setStaffcustplan02((planXj02).toString());
                depInsListXj.setStaffcustactual02((acalXj02).toString());
                depInsListXj.setStaffcustplan03((planXj03).toString());
                depInsListXj.setStaffcustactual03((acalXj03).toString());
//                depInsListXj.setWorkdifferentfourth((workXj04).toString());
//                depInsListXj.setRankdifferentfourth((rankXj04).toString());
                depInsListXj.setWorkdifferentofyear((workXjoy).toString());
                depInsListXj.setRankdifferentofyear((rankXjoy).toString());
                reslutValue.add(depInsListXj);
                departmentalInsideReturnVo.setDepartmentalInsideList(reslutValue);
                returnList.add(departmentalInsideReturnVo);
                planHj04 = planHj04.add(planXj04);
                acalHj04 = acalHj04.add(acalXj04);
                planHj05 = planHj05.add(planXj05);
                acalHj05 = acalHj05.add(acalXj05);
                planHj06 = planHj06.add(planXj06);
                acalHj06 = acalHj06.add(acalXj06);
//                workHj01 = workHj01.add(workXj01);
//                rankHj01 = rankHj01.add(rankXj01);
                planHj07 = planHj07.add(planXj07);
                acalHj07 = acalHj07.add(acalXj07);
                planHj08 = planHj08.add(planXj08);
                acalHj08 = acalHj08.add(acalXj08);
                planHj09 = planHj09.add(planXj09);
                acalHj09 = acalHj09.add(acalXj09);
//                workHj02 = workHj02.add(workXj02);
//                rankHj02 = rankHj02.add(rankXj02);
                planHj10 = planHj10.add(planXj10);
                acalHj10 = acalHj10.add(acalXj10);
                planHj11 = planHj11.add(planXj11);
                acalHj11 = acalHj11.add(acalXj11);
                planHj12 = planHj12.add(planXj12);
                acalHj12 = acalHj12.add(acalXj12);
//                workHj03 = workHj03.add(workXj03);
//                rankHj03 = rankHj03.add(rankXj03);
                planHj01 = planHj01.add(planXj01);
                acalHj01 = acalHj01.add(acalXj01);
                planHj02 = planHj02.add(planXj02);
                acalHj02 = acalHj02.add(acalXj02);
                planHj03 = planHj03.add(planXj03);
                acalHj03 = acalHj03.add(acalXj03);
//                workHj04 = workHj04.add(workXj04);
//                rankHj04 = rankHj04.add(rankXj04);
                workHjoy = workHjoy.add(workXjoy);
                rankHjoy = rankHjoy.add(rankXjoy);
            }
            deInsideReturnHjVo.setThemename("合计");
            deInsideReturnHjVo.setStaffcustplan04((planHj04).toString());
            deInsideReturnHjVo.setStaffcustactual04((acalHj04).toString());
            deInsideReturnHjVo.setStaffcustplan05((planHj05).toString());
            deInsideReturnHjVo.setStaffcustactual05((acalHj05).toString());
            deInsideReturnHjVo.setStaffcustplan06((planHj06).toString());
            deInsideReturnHjVo.setStaffcustactual06((acalHj06).toString());
//            deInsideReturnHjVo.setWorkdifferentfirst((workHj01).toString());
//            deInsideReturnHjVo.setRankdifferentfirst((rankHj01).toString());
            deInsideReturnHjVo.setStaffcustplan07((planHj07).toString());
            deInsideReturnHjVo.setStaffcustactual07((acalHj07).toString());
            deInsideReturnHjVo.setStaffcustplan08((planHj08).toString());
            deInsideReturnHjVo.setStaffcustactual08((acalHj08).toString());
            deInsideReturnHjVo.setStaffcustplan09((planHj09).toString());
            deInsideReturnHjVo.setStaffcustactual09((acalHj09).toString());
//            deInsideReturnHjVo.setWorkdifferentsecond((workHj02).toString());
//            deInsideReturnHjVo.setRankdifferentsecond((rankHj02).toString());
            deInsideReturnHjVo.setStaffcustplan10((planHj10).toString());
            deInsideReturnHjVo.setStaffcustactual10((acalHj10).toString());
            deInsideReturnHjVo.setStaffcustplan11((planHj11).toString());
            deInsideReturnHjVo.setStaffcustactual11((acalHj11).toString());
            deInsideReturnHjVo.setStaffcustplan12((planHj12).toString());
            deInsideReturnHjVo.setStaffcustactual12((acalHj12).toString());
//            deInsideReturnHjVo.setWorkdifferentthird((workHj03).toString());
//            deInsideReturnHjVo.setRankdifferentthird((rankHj03).toString());
            deInsideReturnHjVo.setStaffcustplan01((planHj01).toString());
            deInsideReturnHjVo.setStaffcustactual01((acalHj01).toString());
            deInsideReturnHjVo.setStaffcustplan02((planHj02).toString());
            deInsideReturnHjVo.setStaffcustactual02((acalHj02).toString());
            deInsideReturnHjVo.setStaffcustplan03((planHj03).toString());
            deInsideReturnHjVo.setStaffcustactual03((acalHj03).toString());
//            deInsideReturnHjVo.setWorkdifferentfourth((workHj04).toString());
//            deInsideReturnHjVo.setRankdifferentfourth((rankHj04).toString());
            deInsideReturnHjVo.setWorkdifferentofyear((workHjoy).toString());
            deInsideReturnHjVo.setRankdifferentofyear((rankHjoy).toString());
            returnList.add(deInsideReturnHjVo);
        }
        return returnList;
    }

    //region scc add 21/8/31 部门项目报表 from
    @Override
    public Object getTableinfoReport(String year, String group_id) throws Exception {
        DepartmentalInside departmentalInside = new DepartmentalInside();
        departmentalInside.setYears(year);
        departmentalInside.setDepartment(group_id);
        List<DepartmentalInside> departmentalInsideList = departmentalInsideMapper.select(departmentalInside);
        Map<String, Map<String, List<DepartmentalInside>>> filterMap =
                departmentalInsideList.stream()
                        .collect(Collectors.groupingBy(DepartmentalInside::getContractnumber,
                                Collectors.groupingBy(DepartmentalInside::getProject_id)));
        List<Dictionary> dictionaryDivide = dictionaryService.getForSelect("PJ063");
        Map<String,Dictionary> divideMap = dictionaryDivide.stream().collect(Collectors.toMap(Dictionary::getCode, a -> a,(k1,k2)->k1));
        for(DepartmentalInside depart : departmentalInsideList){
            if(depart.getEntrycondition().equals("HT004001")){
                depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getContracatamountdetail() + "-废弃" + "】");
            }else{
                if(filterMap.get(depart.getContractnumber()).size() > 1){
                    depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getContracatamountdetail() + "】");
                }else{
                    depart.setContractnumber(depart.getContractnumber() + "-" + "【" + depart.getClaimamount() + "】");
                }
            }
            if(!StringUtils.isNullOrEmpty(depart.getDivide())){
                depart.setDivide(divideMap.get(depart.getDivide()).getValue1());
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan04())){
                depart.setStaffcustplan04("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan05())){
                depart.setStaffcustplan05("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan06())){
                depart.setStaffcustplan06("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan07())){
                depart.setStaffcustplan07("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan08())){
                depart.setStaffcustplan08("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan09())){
                depart.setStaffcustplan09("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan10())){
                depart.setStaffcustplan10("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan11())){
                depart.setStaffcustplan11("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan12())){
                depart.setStaffcustplan12("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan01())){
                depart.setStaffcustplan01("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan02())){
                depart.setStaffcustplan02("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustplan03())){
                depart.setStaffcustplan03("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual04())){
                depart.setStaffcustactual04("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual05())){
                depart.setStaffcustactual05("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual06())){
                depart.setStaffcustactual06("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual07())){
                depart.setStaffcustactual07("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual08())){
                depart.setStaffcustactual08("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual09())){
                depart.setStaffcustactual09("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual10())){
                depart.setStaffcustactual10("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual11())){
                depart.setStaffcustactual11("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual12())){
                depart.setStaffcustactual12("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual01())){
                depart.setStaffcustactual01("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual02())){
                depart.setStaffcustactual02("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getStaffcustactual03())){
                depart.setStaffcustactual03("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getWorkdifferentfirst())){
                depart.setWorkdifferentfirst("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getRankdifferentfirst())){
                depart.setRankdifferentfirst("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getWorkdifferentsecond())){
                depart.setWorkdifferentsecond("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getRankdifferentsecond())){
                depart.setRankdifferentsecond("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getWorkdifferentthird())){
                depart.setWorkdifferentthird("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getRankdifferentthird())){
                depart.setRankdifferentthird("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getWorkdifferentfourth())){
                depart.setWorkdifferentfourth("0");
            }
            if(StringUtils.isNullOrEmpty(depart.getRankdifferentfourth())){
                depart.setRankdifferentfourth("0");
            }
        }
        HashMap<String,List<DepartmentalInside>> DepList =  departmentalInsideList.stream().collect(Collectors.groupingBy(DepartmentalInside :: getThemename,HashMap::new,Collectors.toList()));
        ArrayList<DepartmentalInside> temporary = new ArrayList<>();
        BigDecimal toplanXj04 = BigDecimal.ZERO;
        BigDecimal toacalXj04 = BigDecimal.ZERO;
        BigDecimal toplanXj05 = BigDecimal.ZERO;
        BigDecimal toacalXj05 = BigDecimal.ZERO;
        BigDecimal toacalXj06 = BigDecimal.ZERO;
        BigDecimal toplanXj06 = BigDecimal.ZERO;
        BigDecimal toworkXj01 = BigDecimal.ZERO;
        BigDecimal torankXj01 = BigDecimal.ZERO;
        BigDecimal toplanXj07 = BigDecimal.ZERO;
        BigDecimal toacalXj07 = BigDecimal.ZERO;
        BigDecimal toplanXj08 = BigDecimal.ZERO;
        BigDecimal toacalXj08 = BigDecimal.ZERO;
        BigDecimal toplanXj09 = BigDecimal.ZERO;
        BigDecimal toacalXj09 = BigDecimal.ZERO;
        BigDecimal toworkXj02 = BigDecimal.ZERO;
        BigDecimal torankXj02 = BigDecimal.ZERO;
        BigDecimal toplanXj10 = BigDecimal.ZERO;
        BigDecimal toacalXj10 = BigDecimal.ZERO;
        BigDecimal toplanXj11 = BigDecimal.ZERO;
        BigDecimal toacalXj11 = BigDecimal.ZERO;
        BigDecimal toplanXj12 = BigDecimal.ZERO;
        BigDecimal toacalXj12 = BigDecimal.ZERO;
        BigDecimal toworkXj03 = BigDecimal.ZERO;
        BigDecimal torankXj03 = BigDecimal.ZERO;
        BigDecimal toplanXj01 = BigDecimal.ZERO;
        BigDecimal toacalXj01 = BigDecimal.ZERO;
        BigDecimal toplanXj02 = BigDecimal.ZERO;
        BigDecimal toacalXj02 = BigDecimal.ZERO;
        BigDecimal toplanXj03 = BigDecimal.ZERO;
        BigDecimal toacalXj03 = BigDecimal.ZERO;
        BigDecimal toworkXj04 = BigDecimal.ZERO;
        BigDecimal torankXj04 = BigDecimal.ZERO;
        for(String departmentOfKey : DepList.keySet()){
            BigDecimal planXj04 = BigDecimal.ZERO;
            BigDecimal acalXj04 = BigDecimal.ZERO;
            BigDecimal planXj05 = BigDecimal.ZERO;
            BigDecimal acalXj05 = BigDecimal.ZERO;
            BigDecimal acalXj06 = BigDecimal.ZERO;
            BigDecimal planXj06 = BigDecimal.ZERO;
            BigDecimal workXj01 = BigDecimal.ZERO;
            BigDecimal rankXj01 = BigDecimal.ZERO;
            BigDecimal planXj07 = BigDecimal.ZERO;
            BigDecimal acalXj07 = BigDecimal.ZERO;
            BigDecimal planXj08 = BigDecimal.ZERO;
            BigDecimal acalXj08 = BigDecimal.ZERO;
            BigDecimal planXj09 = BigDecimal.ZERO;
            BigDecimal acalXj09 = BigDecimal.ZERO;
            BigDecimal workXj02 = BigDecimal.ZERO;
            BigDecimal rankXj02 = BigDecimal.ZERO;
            BigDecimal planXj10 = BigDecimal.ZERO;
            BigDecimal acalXj10 = BigDecimal.ZERO;
            BigDecimal planXj11 = BigDecimal.ZERO;
            BigDecimal acalXj11 = BigDecimal.ZERO;
            BigDecimal planXj12 = BigDecimal.ZERO;
            BigDecimal acalXj12 = BigDecimal.ZERO;
            BigDecimal workXj03 = BigDecimal.ZERO;
            BigDecimal rankXj03 = BigDecimal.ZERO;
            BigDecimal planXj01 = BigDecimal.ZERO;
            BigDecimal acalXj01 = BigDecimal.ZERO;
            BigDecimal planXj02 = BigDecimal.ZERO;
            BigDecimal acalXj02 = BigDecimal.ZERO;
            BigDecimal planXj03 = BigDecimal.ZERO;
            BigDecimal acalXj03 = BigDecimal.ZERO;
            BigDecimal workXj04 = BigDecimal.ZERO;
            BigDecimal rankXj04 = BigDecimal.ZERO;
            for(DepartmentalInside dep : DepList.get(departmentOfKey)){
                planXj04 = planXj04.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan04()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan04()));
                planXj05 = planXj05.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan05()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan05()));
                planXj06 = planXj06.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan06()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan06()));
                planXj07 = planXj07.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan07()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan07()));
                planXj08 = planXj08.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan08()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan08()));
                planXj09 = planXj09.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan09()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan09()));
                planXj10 = planXj10.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan10()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan10()));
                planXj11 = planXj11.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan11()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan11()));
                planXj12 = planXj12.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan12()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan12()));
                planXj01 = planXj01.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan01()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan01()));
                planXj02 = planXj02.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan02()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan02()));
                planXj03 = planXj03.add(StringUtils.isNullOrEmpty(dep.getStaffcustplan03()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustplan03()));
                acalXj04 = acalXj04.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual04()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual04()));
                acalXj05 = acalXj05.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual05()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual05()));
                acalXj06 = acalXj06.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual06()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual06()));
                acalXj07 = acalXj07.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual07()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual07()));
                acalXj08 = acalXj08.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual08()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual08()));
                acalXj09 = acalXj09.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual09()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual09()));
                acalXj10 = acalXj10.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual10()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual10()));
                acalXj11 = acalXj11.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual11()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual11()));
                acalXj12 = acalXj12.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual12()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual12()));
                acalXj01 = acalXj01.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual01()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual01()));
                acalXj02 = acalXj02.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual02()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual02()));
                acalXj03 = acalXj03.add(StringUtils.isNullOrEmpty(dep.getStaffcustactual03()) ? BigDecimal.ZERO : new BigDecimal(dep.getStaffcustactual03()));
                workXj01 = workXj01.add(StringUtils.isNullOrEmpty(dep.getWorkdifferentfirst()) ? BigDecimal.ZERO : new BigDecimal(dep.getWorkdifferentfirst()));
                rankXj01 = rankXj01.add(StringUtils.isNullOrEmpty(dep.getRankdifferentfirst()) ? BigDecimal.ZERO : new BigDecimal(dep.getRankdifferentfirst()));
                workXj02 = workXj02.add(StringUtils.isNullOrEmpty(dep.getWorkdifferentsecond()) ? BigDecimal.ZERO : new BigDecimal(dep.getWorkdifferentsecond()));
                rankXj02 = rankXj02.add(StringUtils.isNullOrEmpty(dep.getRankdifferentsecond()) ? BigDecimal.ZERO : new BigDecimal(dep.getRankdifferentsecond()));
                workXj03 = workXj03.add(StringUtils.isNullOrEmpty(dep.getWorkdifferentthird()) ? BigDecimal.ZERO : new BigDecimal(dep.getWorkdifferentthird()));
                rankXj03 = rankXj03.add(StringUtils.isNullOrEmpty(dep.getRankdifferentthird()) ? BigDecimal.ZERO : new BigDecimal(dep.getRankdifferentthird()));
                workXj04 = workXj04.add(StringUtils.isNullOrEmpty(dep.getWorkdifferentfourth()) ? BigDecimal.ZERO : new BigDecimal(dep.getWorkdifferentfourth()));
                rankXj04 = rankXj04.add(StringUtils.isNullOrEmpty(dep.getRankdifferentfourth()) ? BigDecimal.ZERO : new BigDecimal(dep.getRankdifferentfourth()));
            }
            StringBuilder sb = new StringBuilder(departmentOfKey);
            sb.append("小计");
            DepartmentalInside inside = new DepartmentalInside("","","",DepList.get(departmentOfKey).get(0).getThemeinfor_id(),sb.toString(),"","","","","","","","","","",planXj04.toString(),acalXj04.toString(),
                    planXj05.toString(),acalXj05.toString(),planXj06.toString(),acalXj06.toString(),workXj01.toString(),rankXj01.toString(),planXj07.toString(),acalXj07.toString(),
                    planXj08.toString(),acalXj08.toString(),planXj09.toString(),acalXj09.toString(),workXj02.toString(),rankXj02.toString(),planXj10.toString(),acalXj10.toString(),planXj11.toString(),acalXj11.toString(),
                    planXj12.toString(),acalXj12.toString(),workXj03.toString(),rankXj03.toString(),planXj01.toString(),acalXj01.toString(),planXj02.toString(),acalXj02.toString(),planXj03.toString(),acalXj03.toString(),
                    workXj04.toString(),rankXj04.toString(),"","");
            temporary.add(inside);
            toplanXj04 = toplanXj04.add(planXj04);
            toacalXj04 = toacalXj04.add(acalXj04);
            toplanXj05 = toplanXj05.add(planXj05);
            toacalXj05 = toacalXj05.add(acalXj05);
            toacalXj06 = toacalXj06.add(acalXj06);
            toplanXj06 = toplanXj06.add(planXj06);
            toworkXj01 = toworkXj01.add(workXj01);
            torankXj01 = torankXj01.add(rankXj01);
            toplanXj07 = toplanXj07.add(planXj07);
            toacalXj07 = toacalXj07.add(acalXj07);
            toplanXj08 = toplanXj08.add(planXj08);
            toacalXj08 = toacalXj08.add(acalXj08);
            toplanXj09 = toplanXj09.add(planXj09);
            toacalXj09 = toacalXj09.add(acalXj09);
            toworkXj02 = toworkXj02.add(workXj02);
            torankXj02 = torankXj02.add(rankXj02);
            toplanXj10 = toplanXj10.add(planXj10);
            toacalXj10 = toacalXj10.add(acalXj10);
            toplanXj11 = toplanXj11.add(planXj11);
            toacalXj11 = toacalXj11.add(acalXj11);
            toplanXj12 = toplanXj12.add(planXj12);
            toacalXj12 = toacalXj12.add(acalXj12);
            toworkXj03 = toworkXj03.add(workXj03);
            torankXj03 = torankXj03.add(rankXj03);
            toplanXj01 = toplanXj01.add(planXj01);
            toacalXj01 = toacalXj01.add(acalXj01);
            toplanXj02 = toplanXj02.add(planXj02);
            toacalXj02 = toacalXj02.add(acalXj02);
            toplanXj03 = toplanXj03.add(planXj03);
            toacalXj03 = toacalXj03.add(acalXj03);
            toworkXj04 = toworkXj04.add(workXj04);
            torankXj04 = torankXj04.add(rankXj04);
        }
        temporary.add(new DepartmentalInside("","","","","合计","","","","","","","","","","",toplanXj04.toString(),toacalXj04.toString(),
                toplanXj05.toString(),toacalXj05.toString(),toplanXj06.toString(),toacalXj06.toString(),toworkXj01.toString(),torankXj01.toString(),toplanXj07.toString(),toacalXj07.toString(),
                toplanXj08.toString(),toacalXj08.toString(),toplanXj09.toString(),toacalXj09.toString(),toworkXj02.toString(),torankXj02.toString(),toplanXj10.toString(),toacalXj10.toString(),toplanXj11.toString(),toacalXj11.toString(),
                toplanXj12.toString(),toacalXj12.toString(),toworkXj03.toString(),torankXj03.toString(),toplanXj01.toString(),toacalXj01.toString(),toplanXj02.toString(),toacalXj02.toString(),toplanXj03.toString(),toacalXj03.toString(),
                toworkXj04.toString(),torankXj04.toString(),"",""));
        departmentalInsideList.addAll(temporary);
        departmentalInsideList.sort(Comparator.comparing(DepartmentalInside::getThemeinfor_id).reversed());
        return JSONObject.toJSON(departmentalInsideList);
    }
    //endregion scc add 21/8/31 部门项目报表 to

    public static <T> Predicate<T> distinctAnt(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    public String sumWorkAnt(DepartmentalInside departmentalInside) {
        String sumWork = BigDecimalUtils.sum(
                departmentalInside.getWorkdifferentfirst(),departmentalInside.getWorkdifferentsecond(),
                departmentalInside.getWorkdifferentthird(),departmentalInside.getWorkdifferentfourth()
        );
        return sumWork;
    }
    public String sumRankAnt(DepartmentalInside departmentalInside) {
        String sumRank = BigDecimalUtils.sum(
                departmentalInside.getRankdifferentfirst(),departmentalInside.getRankdifferentsecond(),
                departmentalInside.getRankdifferentthird(),departmentalInside.getRankdifferentfourth()
        );
        return sumRank;
    }

    public Map<String,String>  projectListFee() throws Exception {
        Map<String,String> proMap = new HashMap<>();
        List<CompanyProjects> prList = companyProjectsMapper.selectAll();
        prList.forEach(pro ->{
            proMap.put(pro.getCompanyprojects_id(),pro.getNumbers());
        });
        return proMap;
    }

    public boolean checkIn(Projectsystem projectsystem,String dateAnt) throws Exception {
        boolean flag = false;
        Date regDate = this.strToDate(dateAnt);
        if(!projectsystem.getAdmissiontime().after(regDate) && !regDate.after(projectsystem.getExittime())){
            flag = true;
        }
        return flag;
    }

    public static Date strToDate(String str) {
        if (StringUtils.isNullOrEmpty(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
