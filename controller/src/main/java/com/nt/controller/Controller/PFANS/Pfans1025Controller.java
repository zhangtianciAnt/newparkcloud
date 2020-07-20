package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.*;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.service_pfans.PFANS1000.mapper.*;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/award")
public class Pfans1025Controller {
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


    @RequestMapping(value = "/generateJxls", method = {RequestMethod.POST})
    public void generateJxls(@RequestBody AwardVo av, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        AwardVo nu = awardService.selectById(av.getAward().getAward_id());
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
        Awardlist = Awardlist.stream().filter(item -> (item.getMaketype().equals("7"))).collect(Collectors.toList());
        return ApiResult.success(Awardlist);
    }

    @RequestMapping(value = "/checkby", method = {RequestMethod.POST})
    public ApiResult checkby(@RequestBody AwardVo awardvo, HttpServletRequest request) throws Exception {
        if (awardvo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        SimpleDateFormat sf = new SimpleDateFormat("MM");
        List<PolicyContract> policycontractlist2 = new ArrayList<>();
        Date draftingdate = awardvo.getAward().getDraftingdate();
        PolicyContract policycontract = new PolicyContract();
        policycontract.setPolicycontract_id(awardvo.getAward().getPolicycontract_id());
        List<PolicyContract> policycontractlist = policycontractmapper.select(policycontract);
        List<PolicyContract> policycontractlist3 = policycontractmapper.selectAll();
        if (policycontractlist.size() > 0) {
            if (policycontractlist.get(0).getCycle().equals("0")) {
                policycontractlist2 = policycontractlist;
            } else if (policycontractlist.get(0).getCycle().equals("1")) {
                if (4 <= Integer.valueOf(sf.format(draftingdate)) && Integer.valueOf(sf.format(draftingdate)) <= 9) {
                    policycontractlist2 = policycontractlist;
                } else {
                    policycontractlist2 = policycontractlist3;
                }
            } else if (policycontractlist.get(0).getCycle().equals("2")) {
                if ((10 <= Integer.valueOf(sf.format(draftingdate)) && Integer.valueOf(sf.format(draftingdate)) <= 12) || (1 <= Integer.valueOf(sf.format(draftingdate)) && Integer.valueOf(sf.format(draftingdate)) <= 3)) {
                    policycontractlist2 = policycontractlist;
                } else {
                    policycontractlist2 = policycontractlist3;
                }
            } else if (policycontractlist.get(0).getCycle().equals("3")) {
                if (4 <= Integer.valueOf(sf.format(draftingdate)) && Integer.valueOf(sf.format(draftingdate)) <= 6) {
                    policycontractlist2 = policycontractlist;
                } else {
                    policycontractlist2 = policycontractlist3;
                }
            } else if (policycontractlist.get(0).getCycle().equals("4")) {
                if (7 <= Integer.valueOf(sf.format(draftingdate)) && Integer.valueOf(sf.format(draftingdate)) <= 9) {
                    policycontractlist2 = policycontractlist;
                } else {
                    policycontractlist2 = policycontractlist3;
                }
            } else if (policycontractlist.get(0).getCycle().equals("5")) {
                if (9 <= Integer.valueOf(sf.format(draftingdate)) && Integer.valueOf(sf.format(draftingdate)) <= 12) {
                    policycontractlist2 = policycontractlist;
                } else {
                    policycontractlist2 = policycontractlist3;
                }
            } else if (policycontractlist.get(0).getCycle().equals("6")) {
                if (1 <= Integer.valueOf(sf.format(draftingdate)) && Integer.valueOf(sf.format(draftingdate)) <= 3) {
                    policycontractlist2 = policycontractlist;
                } else {
                    policycontractlist2 = policycontractlist3;
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


}
