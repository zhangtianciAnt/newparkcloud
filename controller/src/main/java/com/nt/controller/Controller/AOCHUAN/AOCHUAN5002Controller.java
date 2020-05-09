package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.All;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinCrdlInfoService;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinPurchaseSerivce;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;
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
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody FinPurchase finPurchase, HttpServletRequest request) throws Exception {

        if (finPurchase == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //UPDATE:FIN_SALES
        //存在Check
        if (finPurchaseSerivce.existCheck(finPurchase)) {
            //唯一性Check
            if(! finPurchaseSerivce.uniqueCheck(finPurchase)) {
                finPurchaseSerivce.update(finPurchase, tokenService.getToken(request));
                finPurchaseSerivce.updateTransportGood(finPurchase, tokenService.getToken(request));
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
     * 生成付款凭证
     */
    @RequestMapping(value = "/createCrdl", method = {RequestMethod.POST})
    public ApiResult createCrdl(@RequestBody FinPurchase finPurchase, HttpServletRequest request) throws Exception {

        if (finPurchase == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        Docurule docurule = new Docurule();
        List<All> accAndauxList  = new ArrayList<>();

        CrdlInfo crdlInfo = new CrdlInfo();

        //获取凭证规则
        docurule = docuruleService.selectByDocutype("PZ001001");// PZ001001 - 支付货款
        if(docurule == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //获取分录规则+辅助核算项目
        accAndauxList = docuruleService.selectrule(docurule.getDocurule_id());
        if(accAndauxList== null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //根据规则替换
        crdlInfo=replaceRule(tokenService.getToken(request).getUserId(),finPurchase, docurule,accAndauxList);

        if(crdlInfo == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        //生成凭证
        finCrdlInfoService.insert(crdlInfo,tokenService.getToken(request));

        //UPDATE:FIN_SALES
        //存在Check
        if (finPurchaseSerivce.existCheck(finPurchase)) {
            //唯一性Check
            if(! finPurchaseSerivce.uniqueCheck(finPurchase)) {
                finPurchase.setCredential_pay(crdlInfo.getCredentialInformation().getCrdl_num());
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
     * 生成发票凭证
     */
    @RequestMapping(value = "/createInvoiceCrdl", method = {RequestMethod.POST})
    public ApiResult createInvoiceCrdl(@RequestBody FinPurchase finPurchase, HttpServletRequest request) throws Exception {

        if (finPurchase == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        Docurule docurule = new Docurule();
        List<All> accAndauxList  = new ArrayList<>();

        CrdlInfo crdlInfo = new CrdlInfo();

        //获取凭证规则
        docurule = docuruleService.selectByDocutype("PZ001002"); //PZ001002 - 收到货物发票
        if(docurule == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //获取分录规则 + 辅助核算项目
        accAndauxList = docuruleService.selectrule(docurule.getDocurule_id());
        if(accAndauxList== null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //根据规则替换
        crdlInfo=replaceRule(tokenService.getToken(request).getUserId(),finPurchase, docurule,accAndauxList);
        if(crdlInfo == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //生成凭证
        finCrdlInfoService.insert(crdlInfo,tokenService.getToken(request));

        //UPDATE:FIN_SALES
        //存在Check
        if (finPurchaseSerivce.existCheck(finPurchase)) {
            //唯一性Check
            if(! finPurchaseSerivce.uniqueCheck(finPurchase)) {
                finPurchase.setCredential_invoice(crdlInfo.getCredentialInformation().getCrdl_num());
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
     * 替换
     * @param userName
     * @param finPurchase
     * @param docurule
     * @param accAndauxList
     * @return
     */
    public CrdlInfo replaceRule(String userName,FinPurchase finPurchase, Docurule docurule,List<All> accAndauxList) throws ParseException {

        CrdlInfo crdlInfo = new CrdlInfo();

        Date busDate  = new Date();
        Date accDate  = new Date();
        Date crdlNoDate = new Date();
        //业务日期
        if(("1").equals(docurule.getBusinessday())){
            busDate = finPurchase.getAp_date();
        }
        //记账日期
        if(("1").equals(docurule.getNowday())){
            accDate = finPurchase.getAp_date();
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

        //凭证基本信息
        CredentialInformation crdl = new CredentialInformation();
        crdl.setCrdl_num(userName.substring(0,12) + sdf.format(crdlNoDate) + UUID.randomUUID().toString().substring(0,7));//凭证编号
        crdl.setCrdltype(docurule.getDocutype());//凭证类型
        crdl.setCrdlword(docurule.getDocument());//凭证字
        crdl.setBus_date(busDate);//业务日期
        crdl.setAcct_date(accDate);//记账日期
        crdl.setAttachments(docurule.getAnnexno());//附件数
        crdl.setPush_status("PZ005001");//推送状态id
        crdl.setPush_status_nm("未推送");//推送状态
        crdl.setOrder_no(finPurchase.getContractnumber());//订单号

        List<AccountingRule> actgrulist = new ArrayList<>();

        for (All item:accAndauxList) {
            AccountingRule accountingRule = new AccountingRule();

            String remarks = "";
            if(StringUtils.isNotBlank(item.getRemarks()) && item.getRemarks().indexOf("{0}")>0 && item.getRemarks().indexOf("{1}")>0 && item.getRemarks().indexOf("{2}")>0){
                remarks = item.getRemarks().replace("{0}", finPurchase.getSupplier()).replace("{1}", finPurchase.getContractnumber());
            }
            else{
                return null;
            }

            //金额计算
            Double calAmount = 0.00;
            if(StringUtils.isNotBlank(item.getAmounttype())) {
                calAmount = amountCalculation(item.getAmounttype(), item.getCrerate(), finPurchase);
            }
            //分录
            accountingRule.setRemarks(remarks);//摘要
            accountingRule.setAcct_code(item.getAccountid());//科目编码
            accountingRule.setDebit(item.getDebit());//借方科目
            accountingRule.setCredit(item.getCredit());//贷方科目
            accountingRule.setCurrency(finPurchase.getCurrency1());//币种
            accountingRule.setEx_rate(finPurchase.getEx_rate());//汇率
            accountingRule.setTaxrate(item.getCrerate());//税率
            accountingRule.setOricurrency_amount(Double.parseDouble(finPurchase.getPurchaseamount()));//原币金额
            accountingRule.setUnit(item.getUnit());//单位
            accountingRule.setUnit_price(Double.parseDouble(finPurchase.getUnitprice1()));//单价
            accountingRule.setQuantity(Integer.parseInt(finPurchase.getPurchase_amount()));//数量
            accountingRule.setAmount(calAmount);//金额
            accountingRule.setRowindex(item.getRowindex());//行号
            //辅助项目
            accountingRule.setBankaccount_code(item.getBankaccountid());//银行账号id
            accountingRule.setDept_code(item.getDepartid());//部门id
            accountingRule.setIae_contg_code(item.getExpenditureid());//收支内容id
            accountingRule.setAuxacctg_code(item.getAccountingid());//核算项目id
            accountingRule.setMaincash_code(item.getMaincashid());//主金流id
            accountingRule.setAttachcash_code(item.getFlowcashid());//附现金流id
            accountingRule.setBankaccount(item.getBankaccount());//银行账号
            accountingRule.setDept(item.getDepart());//部门
            accountingRule.setIae_contg(item.getExpenditure());//收支内容
            accountingRule.setAuxacctg(item.getAccounting());//核算项目
            accountingRule.setMaincashflow(item.getMaincash());//主金流
            accountingRule.setAttachcashflow(item.getFlowcash());//附现金流
            accountingRule.setAuxacctg_amount(finPurchase.getPurchaseamount());//辅助账金额

            actgrulist.add(accountingRule);
        }

        crdlInfo.setCredentialInformation(crdl);
        crdlInfo.setAccountingRuleList(actgrulist);

        return crdlInfo;
    }

    /**
     * 金额计算
     * @param amountType
     * @param finPurchase
     * @return
     */
    private Double amountCalculation(String amountType, String tax, FinPurchase finPurchase) throws ParseException {

        Double resultAmount = 0.00;

        switch (amountType) {
            case "1"://采购金额
                if (StringUtils.isNotBlank(finPurchase.getPurchaseamount()) && !" ".equals(finPurchase.getPurchaseamount())) {
                        resultAmount = Double.parseDouble(finPurchase.getPurchaseamount());
                }
                break;
            case "2"://税费 = 采购金额/(1+增值税率)*增值税率
                if (StringUtils.isNotBlank(finPurchase.getPurchaseamount()) && !" ".equals(finPurchase.getPurchaseamount())) {

                    Double pAmount = Double.parseDouble(finPurchase.getPurchaseamount());
                    NumberFormat nf =  NumberFormat.getPercentInstance();
                    Number percent = nf.parse(tax);

                    resultAmount = pAmount/(1+percent.doubleValue())*percent.doubleValue();
                }
                break;
            case "3"://库存商品
                if (StringUtils.isNotBlank(finPurchase.getUnitprice1()) && !" ".equals(finPurchase.getUnitprice1())) {
                    Integer count = 0;
                    if (StringUtils.isNotBlank(finPurchase.getPurchase_amount()) && !" ".equals(finPurchase.getPurchase_amount())) {
                        count = Integer.parseInt(finPurchase.getPurchase_amount());
                    }
                    resultAmount = Double.parseDouble(finPurchase.getUnitprice1()) * count;
                }
                break;
        }

        return resultAmount;
    }
}
