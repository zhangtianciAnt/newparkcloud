package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinCrdlInfoService;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinPurchaseSerivce;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/finpurchase")
public class AOCHUAN5002Controller {

    @Autowired
    private FinPurchaseSerivce finPurchaseSerivce;

    @Autowired
    private FinCrdlInfoService finCrdlInfoService;

    @Autowired
    private DocuruleService docuruleService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取费用表数据
     */
    @RequestMapping(value = "/getFinPurchaseList", method = {RequestMethod.POST})
    public ApiResult getFinPurchaseList(HttpServletRequest request) throws Exception {

        FinPurchase finPurchase = new FinPurchase();
        return ApiResult.success(finPurchaseSerivce.getFinPurchaseList(finPurchase));
    }

    /**
     * 获取采购表
     */
    @RequestMapping(value = "/getForm",method={RequestMethod.GET})
    public ApiResult getForm(@RequestParam String id, HttpServletRequest request) throws Exception {
        return ApiResult.success(finPurchaseSerivce.getForm(id));
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/updateStatus", method = {RequestMethod.POST})
    public ApiResult updateStatus(@RequestBody FinPurchase finPurchase, HttpServletRequest request) throws Exception {

        if (finPurchase == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //UPDATE:FIN_SALES
        //存在Check
        if (finPurchaseSerivce.existCheck(finPurchase)) {
            //唯一性Check
            if(! finPurchaseSerivce.uniqueCheck(finPurchase)) {
                finPurchaseSerivce.update(finPurchase, tokenService.getToken(request));
            }else{
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        //正常结束
        FinPurchase finPurchase1 = new FinPurchase();
        return ApiResult.success(finPurchaseSerivce.getFinPurchaseList(finPurchase1));
    }

    /**
     * 生成凭证
     */
    @RequestMapping(value = "/createCrdl", method = {RequestMethod.POST})
    public ApiResult createCrdl(@RequestBody FinPurchase finPurchase, HttpServletRequest request) throws Exception {

        if (finPurchase == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        Docurule docurule = new Docurule();
        DocuruleVo crerule = new DocuruleVo();
        List<Helprule> helpruleLst = null;

        CrdlInfo crdlInfo = new CrdlInfo();

        //获取凭证规则
        docurule = docuruleService.selectByDocutype("PZ001002");
        if(docurule == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //获取分录规则
        crerule = docuruleService.One(docurule.getDocurule_id());
        if(crerule== null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //获取辅助核算项目
        helpruleLst = docuruleService.helpOne(docurule.getDocurule_id());


        //根据规则替换
        crdlInfo=replaceRule(tokenService.getToken(request).getUserId(),finPurchase, docurule,crerule,helpruleLst);

        //生成凭证
        finCrdlInfoService.insert(crdlInfo,tokenService.getToken(request));

        //UPDATE:FIN_SALES
        //存在Check
        if (finPurchaseSerivce.existCheck(finPurchase)) {
            //唯一性Check
            if(! finPurchaseSerivce.uniqueCheck(finPurchase)) {
                finPurchaseSerivce.update(finPurchase, tokenService.getToken(request));
            }else{
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        FinPurchase finSales = new FinPurchase();
        return ApiResult.success(finPurchaseSerivce.getFinPurchaseList(finSales));
    }

    /**
     * 替换
     * @param docurule
     * @param crerule
     * @return
     */
    public CrdlInfo replaceRule(String userName,FinPurchase finPurchase, Docurule docurule, DocuruleVo crerule, List<Helprule> helpruleList){

        CrdlInfo crdlInfo = new CrdlInfo();

        Date busDate  = new Date();
        Date accDate  = new Date();
        Date crdlNoDate = new Date();

        if(("1").equals(docurule.getBusinessday())){
            busDate = finPurchase.getAp_date();
        }
        if(("1").equals(docurule.getNowday())){
            accDate = finPurchase.getAp_date();
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

        //凭证基本信息
        CredentialInformation crdl = new CredentialInformation();
        crdl.setCrdl_num(userName.substring(0,20) + sdf.format(crdlNoDate) + UUID.randomUUID().toString().substring(0,7));
        crdl.setCrdltype(docurule.getDocutype());
        crdl.setCrdlword(docurule.getDocument());
        crdl.setBus_date(busDate);
        crdl.setAcct_date(accDate);
        crdl.setAttachments(docurule.getAnnexno());
        crdl.setPush_status("PZ005001");
        crdl.setPush_status_nm("未推送");

        List<AccountingRule> actgrulist = new ArrayList<>();
        List<Crerule> creruleList = crerule.getCrerules();

        for (int i = 0; i<creruleList.size(); i++) {
            AccountingRule accountingRule = new AccountingRule();

            String remarks = "";
            if(StringUtils.isNotBlank(creruleList.get(i).getRemarks())){
                remarks = creruleList.get(i).getRemarks().replace("{0}", finPurchase.getSupplier()).replace("{1}", finPurchase.getContractnumber());
            }

            //分录
            accountingRule.setRemarks(remarks);
            accountingRule.setDebit(creruleList.get(i).getDebit());
            accountingRule.setCredit(creruleList.get(i).getCredit());
            accountingRule.setAmount(finPurchase.getPurchaseamount());
            //辅助项目
            accountingRule.setBankaccount(helpruleList.get(i).getBankaccount());
            accountingRule.setDept(helpruleList.get(i).getDepart());
            accountingRule.setIae_contg(helpruleList.get(i).getExpenditure());
            accountingRule.setAuxacctg(helpruleList.get(i).getAccounting());
            accountingRule.setMaincashflow(helpruleList.get(i).getMaincash());
            accountingRule.setAttachcashflow(helpruleList.get(i).getFlowcash());
            accountingRule.setAuxacctg_amount(finPurchase.getPurchaseamount());

            actgrulist.add(accountingRule);
        }

        crdlInfo.setCredentialInformation(crdl);
        crdlInfo.setAccountingRuleList(actgrulist);

        return crdlInfo;
    }
}
