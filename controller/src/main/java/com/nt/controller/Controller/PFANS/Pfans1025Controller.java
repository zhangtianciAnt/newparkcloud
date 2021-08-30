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
import com.nt.dao_Pfans.PFANS1000.Vo.ReportContractEnVo;
import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.dao_Pfans.PFANS5000.Vo.CompanyProjectsReportCheckVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.service_pfans.PFANS1000.ContractapplicationService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.service_pfans.PFANS2000.PersonalCostService;
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
        awardService.updateAwardVo(awardVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public ApiResult getList(@RequestBody Award award, HttpServletRequest request) throws Exception {
        if (award == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(awardService.get(award));
    }

    /**
     * @方法名：reportContractEn
     * @描述：导出合同相关信息
     * @创建日期：2021/06/21
     * @作者：ztc
     * @参数：[ conType:0-委托 1-受托 2-其他
     * ]
     * @返回值：List<CompanyProjectsReport>
     */
    @RequestMapping(value = "/reportContractEn", method = {RequestMethod.GET})
    public ApiResult reportContractEn(String conType) throws Exception {
        List<ReportContractEnVo> cprcList = contractapplicationService.reportContractEn(conType);
        ArrayList<Map<String, Object>> rowLists = CollUtil.newArrayList();
        String destFilePath = "";
        if (conType.equals("0")) {
            for (ReportContractEnVo item : cprcList) {
                int contNum = 0;
                int conNubNum = 0;
                if (item.getContractnumbercountList() != null) {
                    conNubNum = item.getContractnumbercountList().size();
                }
                int maxNum = conNubNum;

                for (int i = 0; i < maxNum; i++) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    if(contNum == 0){
                        row.put("契约书番号", item.getContractapplication().getContractnumber());
                        row.put("契约期间", item.getContractapplication().getContractdate());
                        row.put("延期截止日", item.getContractapplication().getExtensiondate());
                        row.put("金额", item.getContractapplication().getClaimamount());
                    }else{
                        row.put("契约书番号", "");
                        row.put("契约期间", "");
                        row.put("延期截止日", "");
                        row.put("金额", "");
                    }
                    row.put("请求方式", item.getContractnumbercountList().get(i).getClaimtype());
                    row.put("纳品预定日", item.getContractnumbercountList().get(i).getDeliverydate());
                    row.put("验收完了日", item.getContractnumbercountList().get(i).getCompletiondate());
                    row.put("请求日", item.getContractnumbercountList().get(i).getClaimdate());
                    row.put("请求金额", item.getContractnumbercountList().get(i).getClaimamount());
                    if(contNum == 0 && item.getAwardList().size() > 0){
                        String[] cladatatime = item.getAwardList().get(0).getClaimdatetime().split("~");
                        row.put("开发开始日", cladatatime[0].trim());
                        row.put("开发完了日", cladatatime[1].trim());
                        row.put("纳品预定日", item.getAwardList().get(0).getDeliverydate());
                        row.put("请求金额", item.getAwardList().get(0).getClaimamount());
                        contNum ++;
                    }else{
                        row.put("开发开始日", "");
                        row.put("开发完了日", "");
                        row.put("纳品预定日", "");
                        row.put("请求金额", "");
                    }
                    row.put("纳品回数", item.getContractnumbercountList().get(i).getClaimtype());
                    row.put("纳品预定日", item.getContractnumbercountList().get(i).getDeliverydate());
                    row.put("验收完了日", item.getContractnumbercountList().get(i).getCompletiondate());
                    row.put("请求日", item.getContractnumbercountList().get(i).getClaimdate());
                    row.put("请求金额", item.getContractnumbercountList().get(i).getClaimamount());
                    rowLists.add(row);
                }
            }
            destFilePath = "D:/" + "委托合同.xlsx";
        }else if(conType.equals("1")){
            for (ReportContractEnVo item : cprcList) {
                int conaNum = 0;

                int conNubNum = 0;
                int compNum = 0;
                int napNum = 0;
                int petNum = 0;
                if (item.getContractnumbercountList() != null) {
                    conNubNum = item.getContractnumbercountList().size();
                }
                if (item.getContractcompoundList() != null) {
                    compNum = item.getContractcompoundList().size();
                }
                if (item.getNapalmList() != null) {
                    napNum = item.getNapalmList().size();
                }
                if (item.getPetitionList() != null) {
                    petNum = item.getPetitionList().size();
                }
                int maxNumOne = ((maxNumOne =(conNubNum > compNum) ? conNubNum : compNum) > napNum ? maxNumOne : napNum);
                int maxNum = maxNumOne > petNum ? maxNumOne : petNum;
                for (int i = 0; i < maxNum; i++) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    if(conaNum == 0){
                        row.put("契约书番号", item.getContractapplication().getContractnumber());
                        row.put("契约期间", item.getContractapplication().getClaimdatetime());
                        row.put("延期截止日", item.getContractapplication().getExtensiondate());
                        row.put("金额", item.getContractapplication().getClaimamount());
                    }else{
                        row.put("契约书番号", "");
                        row.put("契约期间", "");
                        row.put("延期截止日", "");
                        row.put("金额", "");
                    }
                    if(i < conNubNum){
                        row.put("合同请求方式", item.getContractnumbercountList().get(i).getClaimtype());
                        row.put("合同请求期间", item.getContractnumbercountList().get(i).getClaimdatetimeqh());
                        row.put("合同纳品预定日", item.getContractnumbercountList().get(i).getDeliverydate());
                        row.put("合同验收完了日", item.getContractnumbercountList().get(i).getCompletiondate());
                        row.put("合同请求日", item.getContractnumbercountList().get(i).getClaimdate());
                        row.put("合同请求金额", item.getContractnumbercountList().get(i).getClaimamount());
                    }else {
                        row.put("合同请求方式", "");
                        row.put("合同请求期间", "");
                        row.put("合同纳品预定日", "");
                        row.put("合同验收完了日", "");
                        row.put("合同请求日", "");
                        row.put("合同请求金额", "");
                    }

                    if(i < compNum){
                        row.put("复合合同请求方式", item.getContractcompoundList().get(i).getClaimtype());
                        row.put("复合合同部门", item.getContractcompoundList().get(i).getGroup_id());
                        row.put("复合合同请求金额", item.getContractcompoundList().get(i).getClaimamount());
                        row.put("复合合同分配金额", item.getContractcompoundList().get(i).getContractrequestamount());
                    }else {
                        row.put("复合合同请求方式", "");
                        row.put("复合合同部门", "");
                        row.put("复合合同请求金额", "");
                        row.put("复合合同分配金额", "");
                    }

                    if(conaNum == 0 && item.getQuotationList().size() > 0){
                        row.put("报价单开发开始日", item.getQuotationList().get(0).getStartdate());
                        row.put("报价单开发完了日", item.getQuotationList().get(0).getEnddate());
                        row.put("报价单请求金额", item.getAwardList().get(0).getClaimamount());
                    }else{
                        row.put("报价单开发开始日", "");
                        row.put("报价单开发完了日", "");
                        row.put("报价单请求金额", "");
                    }

                    if(conaNum == 0 && item.getContractList().size() > 0){
                        row.put("契约书开发开始日", item.getContractList().get(0).getOpeningdate());
                        row.put("契约书开发完了日", item.getContractList().get(0).getEnddate());
                        row.put("契约书请求金额", item.getContractList().get(0).getClaimamount());
                    }else{
                        row.put("契约书开发开始日", "");
                        row.put("契约书开发完了日", "");
                        row.put("契约书请求金额", "");
                    }

                    if(conaNum == 0 && item.getAwardList().size() > 0){
                        String[] cladatatime = item.getAwardList().get(0).getClaimdatetime().split("~");
                        row.put("决裁书开发开始日", cladatatime[0].trim());
                        row.put("决裁书开发完了日", cladatatime[1].trim());
                        row.put("决裁书纳品预定日", item.getAwardList().get(0).getDeliverydate());
                        row.put("决裁书请求金额", item.getAwardList().get(0).getClaimamount());
                        conaNum ++;
                    }else{
                        row.put("决裁书开发开始日", "");
                        row.put("决裁书开发完了日", "");
                        row.put("决裁书纳品预定日", "");
                        row.put("决裁书请求金额", "");
                    }

                    if(i < conNubNum){
                        row.put("决裁书纳品回数", item.getContractnumbercountList().get(i).getClaimtype());
                        row.put("决裁书纳品预定日", item.getContractnumbercountList().get(i).getDeliverydate());
                        row.put("决裁书验收完了日", item.getContractnumbercountList().get(i).getCompletiondate());
                        row.put("决裁书请求日", item.getContractnumbercountList().get(i).getClaimdate());
                        row.put("决裁书请求金额", item.getContractnumbercountList().get(i).getClaimamount());
                    }else{
                        row.put("决裁书纳品回数", "");
                        row.put("决裁书纳品预定日", "");
                        row.put("决裁书验收完了日", "");
                        row.put("决裁书请求日", "");
                        row.put("决裁书请求金额", "");
                    }


                    if(i < napNum){
                        row.put("纳品书开发开始日", item.getNapalmList().get(i).getOpeningdate());
                        row.put("纳品书开发完了日", item.getNapalmList().get(i).getEnddate());
                        row.put("纳品书请求番号", item.getNapalmList().get(i).getClaimnumber());
                        row.put("纳品书请求日", item.getNapalmList().get(i).getClaimamount());
                        row.put("纳品书请求契约种类", item.getNapalmList().get(i).getClaimtype());
                        row.put("纳品书纳品作成日", item.getNapalmList().get(i).getDeliveryfinshdate());
                        row.put("纳品书纳品预定日", item.getNapalmList().get(i).getDeliverydate());
                        row.put("纳品书验收完了日", item.getNapalmList().get(i).getCompletiondate());
                        row.put("纳品书请求金额", item.getNapalmList().get(i).getClaimamount());
                    }else {
                        row.put("纳品书开发开始日", "");
                        row.put("纳品书开发完了日", "");
                        row.put("纳品书请求番号", "");
                        row.put("纳品书请求日", "");
                        row.put("纳品书请求契约种类", "");
                        row.put("纳品书纳品作成日", "");
                        row.put("纳品书纳品预定日", "");
                        row.put("纳品书验收完了日", "");
                        row.put("纳品书请求金额", "");
                    }

                    if(i < petNum){
                        String[] cladatatime = item.getPetitionList().get(i).getClaimdatetime().split("~");
                        row.put("请求书开发开始日", cladatatime[0].trim());
                        row.put("请求书开发完了日", cladatatime[1].trim());
                        row.put("请求书请求番号", item.getPetitionList().get(i).getClaimnumber());
                        row.put("请求书请求日", item.getPetitionList().get(i).getClaimdate());
                        row.put("请求书请求金额", item.getPetitionList().get(i).getClaimamount());
                    }else {
                        row.put("请求书开发开始日", "");
                        row.put("请求书开发完了日", "");
                        row.put("请求书请求番号", "");
                        row.put("请求书请求日", "");
                        row.put("请求书请求金额", "");
                    }
                    rowLists.add(row);
                }
            }
            destFilePath = "D:/" + "受托合同.xlsx";
        }else if (conType.equals("2")) {
            for (ReportContractEnVo item : cprcList) {
                int conaNum = 0;
                int conNubNum = 0;
                if (item.getContractnumbercountList() != null) {
                    conNubNum = item.getContractnumbercountList().size();
                }
                int maxNum = conNubNum;
                for (int i = 0; i < maxNum; i++) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    if(conaNum == 0){
                        row.put("契约书番号", item.getContractapplication().getContractnumber());
                        row.put("契约期间", item.getContractapplication().getContractdate());
                        row.put("延期截止日", item.getContractapplication().getExtensiondate());
                        row.put("金额", item.getContractapplication().getClaimamount());
                    }else{
                        row.put("契约书番号", "");
                        row.put("契约期间", "");
                        row.put("延期截止日", "");
                        row.put("金额", "");
                    }
                    row.put("请求方式", item.getContractnumbercountList().get(i).getClaimtype());
                    row.put("纳品预定日", item.getContractnumbercountList().get(i).getDeliverydate());
                    row.put("验收完了日", item.getContractnumbercountList().get(i).getCompletiondate());
                    row.put("请求日", item.getContractnumbercountList().get(i).getClaimdate());
                    row.put("请求金额", item.getContractnumbercountList().get(i).getClaimamount());
                    if(conaNum == 0 && item.getAwardList().size() > 0){
                        String[] cladatatime = item.getAwardList().get(0).getClaimdatetime().split("~");
                        row.put("开发开始日", cladatatime[0].trim());
                        row.put("开发完了日", cladatatime[1].trim());
                        row.put("纳品预定日", item.getAwardList().get(0).getDeliverydate());
                        row.put("请求金额", item.getAwardList().get(0).getClaimamount());
                        conaNum ++;
                    }else{
                        row.put("开发开始日", "");
                        row.put("开发完了日", "");
                        row.put("纳品预定日", "");
                        row.put("请求金额", "");
                    }
                    row.put("纳品回数", item.getContractnumbercountList().get(i).getClaimtype());
                    row.put("纳品预定日", item.getContractnumbercountList().get(i).getDeliverydate());
                    row.put("验收完了日", item.getContractnumbercountList().get(i).getCompletiondate());
                    row.put("请求日", item.getContractnumbercountList().get(i).getClaimdate());
                    row.put("请求金额", item.getContractnumbercountList().get(i).getClaimamount());
                    rowLists.add(row);
                }
            }
            destFilePath = "D:/" + "其他合同.xlsx";
        }
            ExcelWriter writer = ExcelUtil.getWriter(destFilePath, "相关信息");
            writer.write(rowLists);
            writer.close();
            return ApiResult.success();
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
    public ApiResult getPersonalBm(@RequestParam String years,@RequestParam String companyen, HttpServletRequest request) throws Exception {
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        HashMap<String,String> companyid = new HashMap<>();
        for(DepartmentVo vo : allDepartment){
            companyid.put(vo.getDepartmentEn(),vo.getDepartmentId());
        }
        List<Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
        HashMap<String, String> dicList = new HashMap<>();
        for(Dictionary dic : dictionaryRank){
            dicList.put(dic.getCode(),dic.getValue1());
        }
        HashMap<String, String> HashMap = null;
        if(!StringUtils.isNullOrEmpty(years) && !StringUtils.isNullOrEmpty(companyen)){
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
            Map<String, PeoplewareFee> bmRanksInfo = personalCostService.getBmRanksInfo(yearss, companyid.get(companyen));
            HashMap = new HashMap<>();
            //前台数组重复标识
            HashMap.put(companyen,companyen);
            for(String key : bmRanksInfo.keySet()){
                HashMap.put(dicList.get(key),bmRanksInfo.get(key).getMonth4() + "~" + bmRanksInfo.get(key).getMonth7());
            }
        }
        return ApiResult.success(HashMap);

    }
}
