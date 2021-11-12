package com.nt.controller.Controller.PFANS;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.service_pfans.PFANS1000.ContractapplicationService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS2000.PersonalCostService;
import com.nt.service_pfans.PFANS4000.mapper.PeoplewareFeeMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/award")
public class Pfans1025Controller {
    @Autowired
    private ContractapplicationMapper contractapplicationMapper;
    @Autowired
    private IndividualMapper individualmapper;
    @Autowired
    private AwardService awardService;
    @Autowired
    private PolicyContractMapper policycontractmapper;
    @Autowired
    private PetitionMapper petitionMapper;

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private QuotationMapper quotationMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private NapalmMapper napalmMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private NonJudgmentMapper nonJudgmentMapper;

    @Autowired
    private PeoplewareFeeMapper peoplewareFeeMapper;

    @Autowired
    private ContractapplicationService contractapplicationService;

    @Autowired
    private OrgTreeService orgtreeservice;

    @Autowired
    private PersonalCostService personalCostService;

    @Autowired
    private OrgTreeService orgTreeService;

    @RequestMapping(value = "/generateJxls", method = {RequestMethod.POST})
    public void generateJxls(@RequestBody AwardVo av, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        AwardVo nu = awardService.selectById(av.getAward().getAward_id());
        //add_fjl_0804  生成书类的覚字去掉 start
        if (av.getAward() != null) {
            if (av.getAward().getContractnumber().contains("覚")) {
                av.getAward().setContractnumber(av.getAward().getContractnumber().replace("覚", ""));
            }
        }
        //add_fjl_0804  生成书类的覚字去掉 end
        //add-ws-8/13-禅道任务432
        if (av.getAward().getRegindiff() != null && av.getAward().getRegindiff() != "") {
            if (av.getAward().getRegindiff().equals("BP028001")) {
                av.getAward().setPjnameenglish(av.getAward().getPjnamechinese());
            } else if (av.getAward().getRegindiff().equals("BP028002")) {
                av.getAward().setPjnameenglish(av.getAward().getPjnamejapanese());
            } else if (av.getAward().getRegindiff().equals("BP028003")) {
                av.getAward().setPjnameenglish(av.getAward().getPjnameenglish());
            }
        } else {
            av.getAward().setPjnameenglish(av.getAward().getPjnamejapanese());
        }
        //add-ws-8/13-禅道任务432
        String aa[] = av.getAward().getClaimdatetime().split(" ~ ");
        List<Map<String, String>> grouplist = (List<Map<String, String>>) av.getGroupN();
        List<AwardDetail> adlist = av.getAwardDetail();
        if (grouplist != null) {
            for (Map<String, String> user : grouplist) {
                String groupid = user.get("groupid");
                String groupname = user.get("groupname");
                for (AwardDetail grn : adlist) {
                    if (groupid.equals(grn.getDepart())) {
                        grn.setDepart(groupname);
                    }
                }
            }
        }
//        List<Dictionary> dictionaryList = dictionaryService.getForSelect("HT006");
//        for(Dictionary item:dictionaryList){
//            if(item.getCode().equals(av.getAward().getCurrencyposition())) {
//
//                av.getAward().setCurrencyposition(item.getValue1());
//            }
//        }
        List<Dictionary> curList = dictionaryService.getForSelect("PG019");
        for (Dictionary item : curList) {
            if (item.getCode().equals(av.getAward().getCurrencyposition())) {
                av.getAward().setCurrencyposition(item.getValue4());
            }
        }
        if (adlist.size() > 0) {
            for (AwardDetail al : adlist) {
                List<Dictionary> aList = dictionaryService.getForSelect("JY002");
                for (Dictionary ite : aList) {
                    if (ite.getCode().equals(al.getBudgetcode())) {
                        al.setBudgetcode(ite.getValue3());
                    }
                }
            }
        }
//        List<Dictionary> planList = dictionaryService.getForSelect("HT018");
//        for(Dictionary item:planList){
//            if(item.getCode().equals(av.getAward().getPlan())) {
//
//                av.getAward().setPlan(item.getValue1());
//            }
//        }
        List<Dictionary> valuationList = dictionaryService.getForSelect("HT005");
        for (Dictionary item : valuationList) {
            if (item.getCode().equals(av.getAward().getValuation())) {
                av.getAward().setValuation(item.getValue1());
            }
            if (item.getCode().equals(av.getAward().getIndividual())) {
                av.getAward().setIndividual(item.getValue1());
            }
        }
        Map<String, Object> data = new HashMap<>();
        //add-ws-只有委托决裁的情况下进行赋值判断
        if (Integer.valueOf(av.getAward().getMaketype()) == 7) {
            //add-ztc-判断字段值进行赋值
            //add-ws-判断非空
            if (av.getAward().getExtrinsic() != null && av.getAward().getExtrinsic() != "") {
                //add-ws-判断非空
                if (Integer.valueOf(av.getAward().getExtrinsic()) == 1) {
                    av.getAward().setExtrinsic("有");
                } else {
                    av.getAward().setExtrinsic("-");
                }
                //add-ws-判断非空
            }
            if (av.getAward().getPlan() != null && av.getAward().getPlan() != "") {
                //add-ws-判断非空
                if (Integer.valueOf(av.getAward().getPlan()) == 1) {
                    av.getAward().setPlan("外");
                } else {
                    av.getAward().setPlan("内");
                }
                //add-ws-判断非空
            }
            //add-ws-判断非空
            //add-ztc-判断字段值进行赋值
        }
        //add-ws-只有委托决裁的情况下进行赋值判断
        //20200427 add by ztc format data start
        //請求日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat str = new SimpleDateFormat("dd/MM/yyyy");
        Date tem_date = null;
        String str_format = "";
        str_format = str.format(av.getAward().getDraftingdate());
        data.put("draftingdate", str_format);
        str_format = str.format(av.getAward().getScheduleddate());
        data.put("scheduleddate", str_format);
        //請求金額
        DecimalFormat df = new DecimalFormat("###,###.00");
        BigDecimal bd = new BigDecimal(av.getAward().getClaimamount());
        str_format = df.format(bd);
        data.put("claimamo", str_format);
        bd = new BigDecimal(av.getAward().getNumbermoth());
        str_format = df.format(bd);
        data.put("numbermoth", str_format);
        if (av.getAward().getSarmb() != null) {
            bd = new BigDecimal(av.getAward().getSarmb());
            str_format = df.format(bd);
            if (str_format.equals(".00")) {
                str_format = "0";
            }
            av.getAward().setSarmb(str_format);
        }
        if (av.getAward().getMaketype().equals("4")) {
            bd = new BigDecimal(av.getAward().getCommission());
            str_format = df.format(bd);
            if (str_format.equals(".00")) {
                str_format = "0";
            }
            av.getAward().setCommission(str_format);
        }
        if (av.getAward().getTotal() != null) {
            bd = new BigDecimal(av.getAward().getTotal());
            str_format = df.format(bd);
            if (str_format.equals(".00")) {
                str_format = "0";
            }
            av.getAward().setTotal(str_format);
        }
        for (int h = 0; h < nu.getNumbercounts().size(); h++) {
            bd = new BigDecimal(nu.getNumbercounts().get(h).getClaimamount());
            str_format = df.format(bd);
            if (str_format.equals(".00")) {
                str_format = "0";
            }
            nu.getNumbercounts().get(h).setClaimamount(str_format);
        }
        for (int k = 0; k < av.getAwardDetail().size(); k++) {
            bd = new BigDecimal(av.getAwardDetail().get(k).getAwardmoney());
            str_format = df.format(bd);
            if (str_format.equals(".00")) {
                str_format = "0";
            }
            av.getAwardDetail().get(k).setAwardmoney(str_format);
        }
        //20200427 add by ztc format data end
        data.put("aw", av.getAward());
        data.put("alist", av.getAwardDetail());
        data.put("num", nu.getNumbercounts());
        data.put("sta", av.getStaffDetail());
        if (aa.length > 0) {
            //20200427 add by ztc format date start
            str_format = aa[0];
            if (str_format.equals(".00")) {
                str_format = "0";
            }
            tem_date = sdf.parse(str_format);
            aa[0] = str.format(tem_date);
            str_format = aa[1];
            if (str_format.equals(".00")) {
                str_format = "0";
            }
            tem_date = sdf.parse(str_format);
            aa[1] = str.format(tem_date);
            data.put("statime", aa);
        } else {
            data.put("statime", "");
        }
        if (av.getAward().getMaketype().equals("4")) {
            ExcelOutPutUtil.OutPut(av.getAward().getContractnumber().toUpperCase() + "_" + av.getAward().getConjapanese() + "_決裁書(受託)", "juecaishu_shoutuo.xlsx", data, response);
        } else {
            ExcelOutPutUtil.OutPut(av.getAward().getContractnumber().toUpperCase() + "_" + av.getAward().getConjapanese() + "_決裁書(委託)", "juecaishu_weituo.xlsx", data, response);
        }
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(Award award, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        award.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(awardService.get(award));
    }

    // add-ws-7/14-禅道144任务
    @RequestMapping(value = "/get2", method = {RequestMethod.GET})
    public ApiResult get2(Award award, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        List<Award> Awardlist = awardMapper.selectAll();
        List<Award> awardList = new ArrayList<>();
        List<Award> awardList2 = new ArrayList<>();
        Awardlist = Awardlist.stream().filter(item -> (item.getMaketype().equals("7"))).collect(Collectors.toList());
        List<Contractapplication> contractapplicationlist = contractapplicationMapper.selectAll();
        contractapplicationlist = contractapplicationlist.stream().filter(item -> (item.getState().equals("有效"))).collect(Collectors.toList());
        for (Contractapplication list : contractapplicationlist) {
            awardList2 = Awardlist.stream().filter(item -> (item.getContractnumber().equals(list.getContractnumber()))).collect(Collectors.toList());
            awardList.addAll(0, awardList2);
        }
        return ApiResult.success(awardList);
    }

    @RequestMapping(value = "/checkby", method = {RequestMethod.POST})
    public ApiResult checkby(@RequestBody AwardVo awardvo, HttpServletRequest request) throws Exception {
        if (awardvo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        List<PolicyContract> policycontractlist2 = new ArrayList<>();
        String year = "";
        String month = "";
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(awardvo.getAward().getDates())) {
            year = awardvo.getAward().getDates().substring(0, 4);
            month = awardvo.getAward().getDates().substring(5, 7);
        }
        PolicyContract policycontract = new PolicyContract();
        policycontract.setPolicycontract_id(awardvo.getAward().getPolicycontract_id());
        List<PolicyContract> policycontractlist = policycontractmapper.select(policycontract);
        List<PolicyContract> policycontractlist3 = policycontractmapper.selectAll();
        policycontractlist3.addAll(0, policycontractlist3);
        if (policycontractlist.size() > 0) {
            if (policycontractlist.get(0).getYearss().equals(year)) {
                if (policycontractlist.get(0).getCycle().equals("0")) {
                    policycontractlist2 = policycontractlist;
                } else if (policycontractlist.get(0).getCycle().equals("1")) {
                    if (4 <= Integer.valueOf(month) && Integer.valueOf(month) <= 9) {
                        policycontractlist2 = policycontractlist;
                    } else {
                        policycontractlist2 = policycontractlist3;
                    }

                } else if (policycontractlist.get(0).getCycle().equals("2")) {
                    if ((10 <= Integer.valueOf(month) && Integer.valueOf(month) <= 12) || (1 <= Integer.valueOf(month) && Integer.valueOf(month) <= 3)) {
                        policycontractlist2 = policycontractlist;
                    } else {
                        policycontractlist2 = policycontractlist3;
                    }
                } else if (policycontractlist.get(0).getCycle().equals("3")) {
                    if (4 <= Integer.valueOf(month) && Integer.valueOf(month) <= 6) {
                        policycontractlist2 = policycontractlist;
                    } else {
                        policycontractlist2 = policycontractlist3;
                    }
                } else if (policycontractlist.get(0).getCycle().equals("4")) {
                    if (7 <= Integer.valueOf(month) && Integer.valueOf(month) <= 9) {
                        policycontractlist2 = policycontractlist;
                    } else {
                        policycontractlist2 = policycontractlist3;
                    }
                } else if (policycontractlist.get(0).getCycle().equals("5")) {
                    if (9 <= Integer.valueOf(month) && Integer.valueOf(month) <= 12) {
                        policycontractlist2 = policycontractlist;
                    } else {
                        policycontractlist2 = policycontractlist3;
                    }
                } else if (policycontractlist.get(0).getCycle().equals("6")) {
                    if (1 <= Integer.valueOf(month) && Integer.valueOf(month) <= 3) {
                        policycontractlist2 = policycontractlist;
                    } else {
                        policycontractlist2 = policycontractlist3;
                    }
                }
            }
        }
        return ApiResult.success(policycontractlist2);
    }

    // add-ws-7/14-禅道144任务
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String award_id, HttpServletRequest request) throws Exception {
        if (award_id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(awardService.selectById(award_id));
    }

    // 禅道任务152
    @RequestMapping(value = "/selectById2", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody Award award, HttpServletRequest request) throws Exception {
        if (award == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(awardService.One(award));
    }

    @RequestMapping(value = "/selectList", method = {RequestMethod.POST})
    public ApiResult selectList(@RequestBody Award award, HttpServletRequest request) throws Exception {
        if (award == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        Map<String, Object> data = new HashMap<>();
        Quotation quotation = new Quotation();
        quotation.setContractnumber(award.getContractnumber());
        List<Quotation> quolist = quotationMapper.select(quotation);
        NonJudgment nonJudgment = new NonJudgment();
        nonJudgment.setContractnumber(award.getContractnumber());
        List<NonJudgment> nonlist = nonJudgmentMapper.select(nonJudgment);
        Contract contract = new Contract();
        contract.setContractnumber(award.getContractnumber());
        List<Contract> conlist = contractMapper.select(contract);
        Award award1 = new Award();
        award1.setContractnumber(award.getContractnumber());
        List<Award> awardlist = awardMapper.select(award1);
        Napalm napalm = new Napalm();
        napalm.setContractnumber(award.getContractnumber());
        List<Napalm> naplist = napalmMapper.select(napalm);
        Petition petition = new Petition();
        petition.setContractnumber(award.getContractnumber());
        List<Petition> petilist = petitionMapper.select(petition);
        //add-ws-7/22-禅道341任务
        Individual individual = new Individual();
        individual.setContractnumber(award.getContractnumber());
        List<Individual> individuallist = individualmapper.select(individual);
        //add-ws-7/22-禅道341任务
        data.put("individual", individuallist);
        data.put("quolist", quolist);
        data.put("nonlist", nonlist);
        data.put("conlist", conlist);
        data.put("awardlist", awardlist);
        data.put("naplist", naplist);
        data.put("petilist", petilist);
        return ApiResult.success(data);
    }
    // 禅道任务152

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody AwardVo awardVo, HttpServletRequest request) throws Exception {
        if (awardVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        //修改决裁书保存 返回接口类型 1103 fr
        boolean flag = awardService.updateAwardVo(awardVo, tokenModel);
        if(flag){
            return ApiResult.success("该决裁中部门存在事业计划内金额不够，自动转成事业计划外！");
        }else{
            return ApiResult.success("");
        }
        //修改决裁书保存 返回接口类型 1103 to
    }

    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public ApiResult getList(@RequestBody Award award, HttpServletRequest request) throws Exception {
        if (award == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(awardService.get(award));
    }

    /**
     *
     * 决裁书数据结转
     */
    @RequestMapping(value = "/dataCarryover", method = {RequestMethod.POST})
    public ApiResult dataCarryover(@RequestBody Award award, HttpServletRequest request) throws Exception {
        OrgTree newOrgInfo = orgtreeservice.get(new OrgTree());
        Award award1 = new Award();
        award1.setAward_id(award.getAward_id());
        if(award.getMaketype() != "9") {//其他决裁书中group ID为空，不更新
            award1.setGroup_id(award.getGroup_id());
        }
        if (award == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        OrgTree orginfo = orgtreeservice.getOrgInfo(newOrgInfo, award.getGroup_id());
        award1.setDeployment(orginfo.getCompanyname());
        awardService.dataCarryover(award1, tokenModel);
        return ApiResult.success();

    }

    /**
     * 受托合同，详情，部门下拉框数据源
     * */
    //region scc add 21/8/20 from
    @GetMapping(value = "/getcompanyen")
    public ApiResult getCompanyen(HttpServletRequest request) throws Exception {
        return ApiResult.success(awardService.getCompanyen());
    }
    //endregion scc add 21/8/20 to

    /**
     * 受托合同，详情，RANK下拉框数据源
     * */
    @GetMapping(value = "/getRanks")
    public ApiResult getRanks(HttpServletRequest request) throws Exception{
        List<Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
        List<Dictionary> collect = dictionaryRank.stream().filter(item -> (!item.getValue1().equals("R11A") && !item.getValue1().equals("R11B") && !item.getValue1().equals("R10"))).collect(Collectors.toList());
        List<String> ranks = new ArrayList<>();
        for(Dictionary ran : collect){
            ranks.add(ran.getValue1());
        }
        return ApiResult.success(ranks);
    }

    /**
     * 获取成本
     * */
    @RequestMapping(value = "/getPersonalBm", method = {RequestMethod.GET})
    public ApiResult getPersonalBm(@RequestParam String years , HttpServletRequest request) throws Exception {
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        HashMap<String,String> companyid = new HashMap<>();//部门简称-部门ID，键值对
        //人件费 获取实际成本变更 ztc fr
        HashMap<String,String> companySort = new HashMap<>();//部门简称-部门ID，键值对
        Map<String,Map<String,List<PeoplewareFee>>> resultMap= new HashMap<>();
        //人件费 获取实际成本变更 ztc to
        for(DepartmentVo vo : allDepartment){
            companyid.put(vo.getDepartmentEn(),vo.getDepartmentId());
            //人件费 获取实际成本变更 ztc fr
            companySort.put(vo.getDepartmentId(),vo.getDepartmentEn());
            //人件费 获取实际成本变更 ztc to
        }
        List<Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
        HashMap<String, String> dicList = new HashMap<>();
        //人件费 获取实际成本变更 ztc fr
        List<String> ranks = new ArrayList<>();
        for(Dictionary dic : dictionaryRank){
            dicList.put(dic.getCode(),dic.getValue1());
            ranks.add(dic.getValue1());
        }
        Map<String,Map<String,List<PeoplewareFee>>> resultsOf= new HashMap<>();
        //人件费 获取实际成本变更 ztc to
        if(!StringUtils.isNullOrEmpty(years)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(years);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int now_year = 0;
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            if(month >= 1 && month <= 4) {
                //时间大于4月10日的，属于新年度，小于10日，属于旧年度
                if(day >=10)
                {
                    now_year = calendar.get(Calendar.YEAR);
                }
                else
                {
                    now_year = calendar.get(Calendar.YEAR) - 1;
                }
            }
            else
            {
                now_year = calendar.get(Calendar.YEAR);
            }
            String yearss = String.valueOf(now_year);
            //人件费 获取实际成本变更 ztc fr
            PeoplewareFee plefee = new PeoplewareFee();
            plefee.setYear(yearss);
            List<String> orgList = new ArrayList<>();
            List<PeoplewareFee> peowafeList = peoplewareFeeMapper.select(plefee);
            Map<String,Map<String,List<PeoplewareFee>>> getPeoRanksMap = peowafeList.stream()
                    .filter(item -> !StringUtils.isNullOrEmpty(item.getGroupid()) && !StringUtils.isNullOrEmpty(item.getRanks()))
                    .collect(Collectors.groupingBy(PeoplewareFee::getGroupid,Collectors.groupingBy(PeoplewareFee::getRanks)));
            getPeoRanksMap.forEach((gro,groList) ->{
                orgList.add(companySort.get(gro));
                HashMap<String,List<PeoplewareFee>> costOf = new HashMap<>();
                groList.forEach((itrank,rankList)->{
                    costOf.put(itrank,rankList);
                });
                resultsOf.put(companySort.get(gro),costOf);
            });
            //取部门差集
            List<String> companyen = companyid.keySet().stream().collect(Collectors.toList());
            List<String> reduceOrg = companyen.stream().filter(item -> !orgList.contains(item)).collect(Collectors.toList());
            Map<String,List<PeoplewareFee>> dicrankMap = new HashMap<>();
            ranks.forEach(rankdic -> {
                List<PeoplewareFee> dicranksList = new ArrayList<>();
                PeoplewareFee rankFee = new PeoplewareFee(null, null, null, rankdic, "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                dicranksList.add(rankFee);
                dicrankMap.put(rankdic,dicranksList);
            });
            Map<String,Map<String,List<PeoplewareFee>>> recostOf= new HashMap<>();
            reduceOrg.forEach(reOrg -> {
                recostOf.put(reOrg,dicrankMap);
            });
            resultMap.putAll(resultsOf);
            resultMap.putAll(recostOf);
//            for(String companyenKey : companyen){
//                costOf = new HashMap<>();
//                Map<String, PeoplewareFee> bmRanksInfo = personalCostService.getBmRanksInfo(yearss, companyid.get(companyenKey));
//                for(String key : bmRanksInfo.keySet()){
//                    costOf.put(dicList.get(key), bmRanksInfo.get(key).getMonth4() + "~" + bmRanksInfo.get(key).getMonth7());
//                }
//                resultsOf.put(companyenKey,costOf);
//            }
        }
        return ApiResult.success(resultMap);
        //人件费 获取实际成本变更 ztc to
    }

    //PSDCD_PFANS_20210723_XQ_086 委托决裁报销明细自动带出 ztc fr
    @RequestMapping(value = "/getAwardEntr", method = {RequestMethod.POST})
    public ApiResult getAwardEntr(@RequestBody List<String> awardIdList , HttpServletRequest request) throws Exception {
        return ApiResult.success(awardService.getAwardEntr(awardIdList));
    }
    //PSDCD_PFANS_20210723_XQ_086 委托决裁报销明细自动带出 ztc to



}
