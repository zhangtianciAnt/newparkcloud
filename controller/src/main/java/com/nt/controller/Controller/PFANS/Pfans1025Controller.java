package com.nt.controller.Controller.PFANS;

import cn.hutool.core.io.FileUtil;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.AwardDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.dao_Workflow.Vo.StartWorkflowVo;
import com.nt.dao_Workflow.Vo.WorkflowLogDetailVo;
import com.nt.service_Org.DictionaryService;
import com.nt.service_WorkFlow.WorkflowServices;
import com.nt.service_pfans.PFANS1000.AwardService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/award")
public class Pfans1025Controller {
    @Autowired
    private AwardService awardService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WorkflowServices workflowServices;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryService dictionaryService;

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
                    av.getAward().setPlacechinese("-");
                    av.getAward().setEquipment("-");
                    av.getAward().setEquipment("-");
                    av.getAward().setCommdepartment("-");
                    av.getAward().setCommission("-");
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
        AwardVo awardVo = awardService.selectById(av.getAward().getAward_id());
        StartWorkflowVo startWorkflowVo = new StartWorkflowVo();
        startWorkflowVo.setDataId(awardVo.getAward().getAward_id());
        List<WorkflowLogDetailVo> wfList = workflowServices.ViewWorkflow2(startWorkflowVo, tokenModel.getLocale());
        //审批流程人员
        String wfList1 = "";
        String wfList2 = "";
        String wfList3 = "";
        String wfList4 = "";
        String wfList5 = "";
        String wfList6 = "";
        Query query = new Query();
        CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
        if (wfList.size() > 0) {
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(0).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList1 = customerInfo.getUserinfo().getCustomername();
                wfList1 = sign.startGraphics2D(wfList1);
            }
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(1).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList2 = customerInfo.getUserinfo().getCustomername();
                wfList2 = sign.startGraphics2D(wfList2);
            }
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(2).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList3 = customerInfo.getUserinfo().getCustomername();
                wfList3 = sign.startGraphics2D(wfList3);
            }
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(3).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList4 = customerInfo.getUserinfo().getCustomername();
                wfList4 = sign.startGraphics2D(wfList4);
            }
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(4).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList5 = customerInfo.getUserinfo().getCustomername();
                wfList5 = sign.startGraphics2D(wfList5);
            }
            query = new Query();
            query.addCriteria(Criteria.where("userid").is(wfList.get(5).getUserId()));
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                wfList6 = customerInfo.getUserinfo().getCustomername();
            }
            //决裁日
            str_format = str.format(wfList.get(5).getEdata());
            data.put("wfList7", str_format);
        }
        data.put("wfList1", wfList1);
        data.put("wfList2", wfList2);
        data.put("wfList3", wfList3);
        data.put("wfList4", wfList4);
        data.put("wfList5", wfList5);
        data.put("wfList6", wfList6);
        //請求金額
        DecimalFormat df = new DecimalFormat("###,###.00");
        BigDecimal bd = new BigDecimal(av.getAward().getClaimamount());
        str_format = df.format(bd);
        data.put("claimamo", str_format);
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
            ExcelOutPutUtil.OutPutPdf(av.getAward().getContractnumber().toUpperCase() + "_" + av.getAward().getConjapanese() + "_決裁書(受託)", "juecaishu_shoutuo.xls", data, response);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList1);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList2);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList3);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList4);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList5);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList6);
        } else {
            ExcelOutPutUtil.OutPutPdf(av.getAward().getContractnumber().toUpperCase() + "_" + av.getAward().getConjapanese() + "_決裁書(委託)", "juecaishu_weituo.xls", data, response);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList1);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList2);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList3);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList4);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList5);
            FileUtil.del("E:\\PFANS\\image" + "/" + wfList6);
        }
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(Award award, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        award.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(awardService.get(award));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String award_id, HttpServletRequest request) throws Exception {
        if (award_id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(awardService.selectById(award_id));
    }

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
