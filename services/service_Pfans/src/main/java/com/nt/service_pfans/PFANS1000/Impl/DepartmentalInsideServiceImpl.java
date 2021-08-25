package com.nt.service_pfans.PFANS1000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.DepartmentalInside;
import com.nt.dao_Pfans.PFANS1000.StaffDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideBaseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideReturnVo;
import com.nt.dao_Pfans.PFANS1000.Vo.StaffWorkMonthInfoVo;
import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.dao_Pfans.PFANS6000.PjExternalInjection;
import com.nt.dao_Pfans.PFANS6000.Vo.PjExternalInjectionVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.DepartmentalInsideService;
import com.nt.service_pfans.PFANS1000.mapper.DepartmentalInsideMapper;
import com.nt.service_pfans.PFANS1000.mapper.StaffDetailMapper;
import com.nt.service_pfans.PFANS2000.PersonalCostService;
import com.nt.service_pfans.PFANS4000.mapper.PeoplewareFeeMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
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
    private DictionaryService dictionaryService;

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
        int yearnow = calnew.get(Calendar.YEAR);
        List<DepartmentVo> departmentVoList = new ArrayList<>();
        departmentVoList = orgTreeService.getAllDepartment();
        List<DepartmentalInsideBaseVo> departmentalInsideBaseVoList = departmentalInsideMapper.getBaseInfo(String.valueOf(year));
        List<String> userList = departmentalInsideBaseVoList.stream().map(DepartmentalInsideBaseVo::getName).distinct().collect(Collectors.toList());
        List<String> projectList = departmentalInsideBaseVoList.stream().map(DepartmentalInsideBaseVo::getCompanyprojects_id).distinct().collect(Collectors.toList());
        String monthStr = "";
        if (month < 10) {
            monthStr = "0" + month;
        }
        String LOG_DATE = String.valueOf(year) + '-' + monthStr;
        List<StaffWorkMonthInfoVo> staffWorkMonthInfoVoList = departmentalInsideMapper.getWorkInfo(LOG_DATE, userList, projectList);
        PeoplewareFee peoplewareFee = new PeoplewareFee();
        peoplewareFee.setYear(String.valueOf(year));
        List<PeoplewareFee> peoplewareFeeList = peoplewarefeeMapper.select(peoplewareFee);
        Map<String, Map<String, Map<String, List<StaffWorkMonthInfoVo>>>> staffGroupMap =
                staffWorkMonthInfoVoList.stream()
                        .filter(item -> !StringUtils.isNullOrEmpty(item.getGroup_id()) && !StringUtils.isNullOrEmpty(item.getProject_id()) && !StringUtils.isNullOrEmpty(item.getRank()))
                        .collect(Collectors.groupingBy(StaffWorkMonthInfoVo::getGroup_id,
                                Collectors.groupingBy(StaffWorkMonthInfoVo::getProject_id,
                                        Collectors.groupingBy(StaffWorkMonthInfoVo::getRank))));
        int finalYear = year;
        staffGroupMap.forEach((dep, depList) -> {//部门
            Map<String,PeoplewareFee> peoplewareFeeMap = new HashMap<>();
            try {
                peoplewareFeeMap  = personalCostService.getBmRanksInfo(String.valueOf(finalYear),dep);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DepartmentalInside departInsideSelect = new DepartmentalInside();
            departInsideSelect.setYears(String.valueOf(finalYear));
            departInsideSelect.setDepartment(dep);
            Map<String, PeoplewareFee> finalPeoplewareFeeMap = peoplewareFeeMap;
            depList.forEach((pro, proList) -> {//项目
                departInsideSelect.setProject_id(pro);
                List<DepartmentalInsideBaseVo> deBaseList = departmentalInsideBaseVoList.stream()
                        .filter(filItem -> !filItem.getCompanyprojects_id().equals(pro)).collect(Collectors.toList());
                proList.forEach((rank, rankList) -> {//rank
                    PeoplewareFee peoCost = finalPeoplewareFeeMap.get(ranksMap.get(rank).getCode());
                    departInsideSelect.setStaffrank(rank);
                    List<DepartmentalInside> getOldRanList = departmentalInsideMapper.select(departInsideSelect);
                    String rankSum = "0.0";
                    rankSum = rankList.stream().map(i -> new BigDecimal(i.getTime_start())).reduce(BigDecimal.ZERO, BigDecimal::add).toString();
                    StaffDetail staffDetail = new StaffDetail();
                    staffDetail.setIncondepartment(departInsideSelect.getDepartment());
                    staffDetail.setContractnumber(deBaseList.get(0).getContractnumber());
                    staffDetail.setAttf(departInsideSelect.getStaffrank());
                    List<StaffDetail> staffDetailList = staffDetailMapper.select(staffDetail);
                    if (getOldRanList.size() == 0) {
                        //新增
                        DepartmentalInside departmentalInside = new DepartmentalInside();
                        BeanUtils.copyProperties(departInsideSelect, departmentalInside);
                        departmentalInside.setDepartmentalinside_id(UUID.randomUUID().toString());
                        departmentalInside.setNumbers(rankList.get(0).getNumbers());
                        departmentalInside.setThemeinfor_id(deBaseList.get(0).getThemeinfor_id());
                        departmentalInside.setThemename(deBaseList.get(0).getThemename());
                        departmentalInside.setDivide(deBaseList.get(0).getDivide());
                        departmentalInside.setToolsorgs(deBaseList.get(0).getToolsorgs());
                        departmentalInside.setContractnumber(deBaseList.get(0).getContractnumber());
                        departmentalInside.setClaimamount(deBaseList.get(0).getClaimamount());
                        departmentalInside.setEntrycondition(deBaseList.get(0).getEntrycondition());
                        departmentalInside.setContracatamountdetail(deBaseList.get(0).getContracatamountdetail());
                        switch (month) {
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
                        if(month == 4 || month == 5 || month == 6){
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
                                                                                                    ? new BigDecimal(peoCost.getMonth4()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan06()))
                                                                            .multiply(
                                                                                    (peoFilterList.size() > 0
                                                                                            ? new BigDecimal(peoCost.getMonth4()) : BigDecimal.ZERO)
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
                                            ).setScale(2,BigDecimal.ROUND_HALF_UP)
                                    )
                            );
                        }
                        else if(month == 7 || month == 8 || month == 9){
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
                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan04()))
                                                                    .multiply(
                                                                            (peoFilterList.size() > 0
                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan05()))
                                                                                    .multiply(
                                                                                            (peoFilterList.size() > 0
                                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan06()))
                                                                            .multiply(
                                                                                    (peoFilterList.size() > 0
                                                                                            ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                            )
                                                            )
                                                    )
                                            ).subtract(
                                                    (
                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual04()))
                                                                    .multiply(
                                                                            (peoFilterList.size() > 0
                                                                                    ? new BigDecimal(peoFilterList.get(0).getMonth7()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual05()))
                                                                                    .multiply(
                                                                                            (peoFilterList.size() > 0
                                                                                                    ? new BigDecimal(peoFilterList.get(0).getMonth8()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual06()))
                                                                            .multiply(
                                                                                    (peoFilterList.size() > 0
                                                                                            ? new BigDecimal(peoFilterList.get(0).getMonth9()) : BigDecimal.ZERO)
                                                                            )
                                                            )
                                                    )
                                            ).setScale(2,BigDecimal.ROUND_HALF_UP)
                                    )
                            );
                        }
                        else if(month == 10 || month == 11 || month == 12){
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
                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan04()))
                                                                    .multiply(
                                                                            (peoFilterList.size() > 0
                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan05()))
                                                                                    .multiply(
                                                                                            (peoFilterList.size() > 0
                                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan06()))
                                                                            .multiply(
                                                                                    (peoFilterList.size() > 0
                                                                                            ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                            )
                                                            )
                                                    )
                                            ).subtract(
                                                    (
                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual04()))
                                                                    .multiply(
                                                                            (peoFilterList.size() > 0
                                                                                    ? new BigDecimal(peoFilterList.get(0).getMonth10()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual05()))
                                                                                    .multiply(
                                                                                            (peoFilterList.size() > 0
                                                                                                    ? new BigDecimal(peoFilterList.get(0).getMonth11()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual06()))
                                                                            .multiply(
                                                                                    (peoFilterList.size() > 0
                                                                                            ? new BigDecimal(peoFilterList.get(0).getMonth12()) : BigDecimal.ZERO)
                                                                            )
                                                            )
                                                    )
                                            ).setScale(2,BigDecimal.ROUND_HALF_UP)
                                    )
                            );
                        }
                        else if(month == 1 || month == 2 || month == 3){
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
                            departmentalInside.setRankdifferentthird(
                                    String.valueOf(
                                            (
                                                    (
                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan04()))
                                                                    .multiply(
                                                                            (peoFilterList.size() > 0
                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan05()))
                                                                                    .multiply(
                                                                                            (peoFilterList.size() > 0
                                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustplan06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustplan06()))
                                                                            .multiply(
                                                                                    (peoFilterList.size() > 0
                                                                                            ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                            )
                                                            )
                                                    )
                                            ).subtract(
                                                    (
                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual04()))
                                                                    .multiply(
                                                                            (peoFilterList.size() > 0
                                                                                    ? new BigDecimal(peoFilterList.get(0).getMonth1()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual05()))
                                                                                    .multiply(
                                                                                            (peoFilterList.size() > 0
                                                                                                    ? new BigDecimal(peoFilterList.get(0).getMonth2()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(departmentalInside.getStaffcustactual06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(departmentalInside.getStaffcustactual06()))
                                                                            .multiply(
                                                                                    (peoFilterList.size() > 0
                                                                                            ? new BigDecimal(peoFilterList.get(0).getMonth3()) : BigDecimal.ZERO)
                                                                            )
                                                            )
                                                    )
                                            ).setScale(2,BigDecimal.ROUND_HALF_UP)
                                    )
                            );
                        }
                        departmentalInside.preInsert(tokenModel);
                        departmentalInsideListInsert.add(departmentalInside);
                    }
                    else
                    {
                        switch (monthlast)
                        {
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
                        if(monthlast == 3 || monthlast == 4 || monthlast == 5){
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
                                                                                                    ? new BigDecimal(peoCost.getMonth4()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan06()))
                                                                            .multiply(
                                                                                    (peoFilterListUpdate.size() > 0
                                                                                            ? new BigDecimal(peoCost.getMonth4()) : BigDecimal.ZERO)
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
                        else if(monthlast == 6 || monthlast == 7 || monthlast == 8){
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
                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan04()))
                                                                    .multiply(
                                                                            (peoFilterListUpdate.size() > 0
                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan05()))
                                                                                    .multiply(
                                                                                            (peoFilterListUpdate.size() > 0
                                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan06()))
                                                                            .multiply(
                                                                                    (peoFilterListUpdate.size() > 0
                                                                                            ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                            )
                                                            )
                                                    )
                                            ).subtract(
                                                    (
                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual04()))
                                                                    .multiply(
                                                                            (peoFilterListUpdate.size() > 0
                                                                                    ? new BigDecimal(peoFilterListUpdate.get(0).getMonth7()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual05()))
                                                                                    .multiply(
                                                                                            (peoFilterListUpdate.size() > 0
                                                                                                    ? new BigDecimal(peoFilterListUpdate.get(0).getMonth8()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual06()))
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
                        else if(monthlast == 9 || monthlast == 10 || monthlast == 11){
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
                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan04()))
                                                                    .multiply(
                                                                            (peoFilterListUpdate.size() > 0
                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan05()))
                                                                                    .multiply(
                                                                                            (peoFilterListUpdate.size() > 0
                                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan06()))
                                                                            .multiply(
                                                                                    (peoFilterListUpdate.size() > 0
                                                                                            ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                            )
                                                            )
                                                    )
                                            ).subtract(
                                                    (
                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual04()))
                                                                    .multiply(
                                                                            (peoFilterListUpdate.size() > 0
                                                                                    ? new BigDecimal(peoFilterListUpdate.get(0).getMonth10()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual05()))
                                                                                    .multiply(
                                                                                            (peoFilterListUpdate.size() > 0
                                                                                                    ? new BigDecimal(peoFilterListUpdate.get(0).getMonth11()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual06()))
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
                        else if(monthlast == 0 || monthlast == 1 || monthlast == 2){
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
                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan04()))
                                                                    .multiply(
                                                                            (peoFilterListUpdate.size() > 0
                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan05()))
                                                                                    .multiply(
                                                                                            (peoFilterListUpdate.size() > 0
                                                                                                    ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustplan06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustplan06()))
                                                                            .multiply(
                                                                                    (peoFilterListUpdate.size() > 0
                                                                                            ? new BigDecimal(peoCost.getMonth7()) : BigDecimal.ZERO)
                                                                            )
                                                            )
                                                    )
                                            ).subtract(
                                                    (
                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual04())
                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual04()))
                                                                    .multiply(
                                                                            (peoFilterListUpdate.size() > 0
                                                                                    ? new BigDecimal(peoFilterListUpdate.get(0).getMonth1()) : BigDecimal.ZERO)
                                                                    )
                                                    )
                                                            .add(
                                                                    (
                                                                            (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual05())
                                                                                    ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual05()))
                                                                                    .multiply(
                                                                                            (peoFilterListUpdate.size() > 0
                                                                                                    ? new BigDecimal(peoFilterListUpdate.get(0).getMonth2()) : BigDecimal.ZERO)
                                                                                    )
                                                                    )
                                                            ).add(
                                                            (
                                                                    (StringUtils.isNullOrEmpty(getOldRanList.get(0).getStaffcustactual06())
                                                                            ? BigDecimal.ZERO : new BigDecimal(getOldRanList.get(0).getStaffcustactual06()))
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
                    }
                });
            });
        });
        try
        {
            //新建数据
            if(departmentalInsideListInsert.size()>0)
            {
                departmentalInsideMapper.insertDepInsAll(departmentalInsideListInsert);
            }
            //更新数据
            if(departmentalInsideListUnpdate.size()>0)
            {
                departmentalInsideMapper.updateDepInsAll(departmentalInsideListUnpdate);
            }
        }
        catch (Exception e)
        {
            System.out.println(month+"月数据生成异常");
        }
    }

    @Override
    public List<DepartmentalInsideReturnVo> getTableinfo(String year, String group_id) throws Exception{
        DepartmentalInside departmentalInside = new DepartmentalInside();
        departmentalInside.setYears(year);
        departmentalInside.setDepartment(group_id);
        List<DepartmentalInside> departmentalInsideList = departmentalInsideMapper.select(departmentalInside);
        TreeMap<String,List<DepartmentalInside>> treeDepList =  departmentalInsideList.stream().collect(Collectors.groupingBy(DepartmentalInside :: getStaffrank,TreeMap::new,Collectors.toList()));
        List<DepartmentalInsideReturnVo> returnList = new ArrayList<>();
        if (treeDepList.size() > 0) {
            treeDepList.forEach((key,value) -> {

            });
        }
        return null;
    }
}
