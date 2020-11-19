package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.All;
import com.nt.dao_Org.Dictionary;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.SupplierbaseinforMapper;
import com.nt.service_AOCHUAN.AOCHUAN4000.mapper.ProductsMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinCrdlInfoService;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinPurchaseSerivce;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.service_Org.DictionaryService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private SupplierbaseinforMapper supplierbaseinforMapper;

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取费用表数据
     */
    @RequestMapping(value = "/getFinPurchaseList", method = {RequestMethod.POST})
    public ApiResult getFinPurchaseList(HttpServletRequest request) throws Exception {
        return ApiResult.success(finPurchaseSerivce.getFinPurchaseList());
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
//        if (finPurchaseSerivce.existCheck(finPurchase)) {
//            //唯一性Check
//            if(! finPurchaseSerivce.uniqueCheck(finPurchase)) {
                finPurchaseSerivce.update(finPurchase, tokenService.getToken(request));
                finPurchaseSerivce.updateTransportGood(finPurchase, tokenService.getToken(request));
//            }else{
//                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
//            }
//        }else{
//            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
//        }
        //正常结束
        return ApiResult.success(finPurchaseSerivce.getFinPurchaseList());
    }



    /**
     * 审批更新
     */
    @RequestMapping(value = "/updatesp", method = {RequestMethod.POST})
    public ApiResult updatesp(@RequestBody FinPurchase finPurchase, HttpServletRequest request) throws Exception {

        if (finPurchase == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        finPurchaseSerivce.update(finPurchase,tokenService.getToken(request));
        return ApiResult.success();
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
        return ApiResult.success(finPurchaseSerivce.getFinPurchaseList());
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
        return ApiResult.success(finPurchaseSerivce.getFinPurchaseList());
    }

    /**
     * 替换
     * @param userName
     * @param finPurchase
     * @param docurule
     * @param accAndauxList
     * @return
     */
    public CrdlInfo replaceRule(String userName,FinPurchase finPurchase, Docurule docurule,List<All> accAndauxList) throws Exception {

        CrdlInfo crdlInfo = new CrdlInfo();

        Date busDate  = new Date();
        Date accDate  = new Date();
        Date crdlNoDate = new Date();
        //业务日期
        if(("2").equals(docurule.getBusinessday())){
            busDate = finPurchase.getAp_date();
        }
        //记账日期
        if(("2").equals(docurule.getNowday())){
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
        Double rate = 0.00;
        if(StringUtils.isNotEmpty(finPurchase.getCurrency1())){
            com.nt.dao_Org.Dictionary dictionary = new com.nt.dao_Org.Dictionary();
            dictionary.setCode(finPurchase.getEx_rate());
            List<Dictionary> dir = dictionaryService.getDictionaryList(dictionary);
            if(dir.size()>0){
                rate = Double.parseDouble(dir.get(0).getValue2());
            }
        }
        for (All item:accAndauxList) {
            AccountingRule accountingRule = new AccountingRule();

            //摘要替换  凭证类型+单号+产品名+数量+单位
            String remarks = "";
            if(StringUtils.isNotBlank(item.getRemarks()) && item.getRemarks().indexOf("{0}")>0 && item.getRemarks().indexOf("{1}")>0 && item.getRemarks().indexOf("{2}")>0&& item.getRemarks().indexOf("{3}")>0){
                if(StringUtils.isNotBlank(finPurchase.getSuppliercn())){
                    remarks = item.getRemarks().replace("{0}", finPurchase.getSuppliercn());
                }
                if(StringUtils.isNotBlank(finPurchase.getProducten())){
                    Products products = productsMapper.selectByPrimaryKey(finPurchase.getProducten());
                    if(products != null){
                        remarks = remarks.replace("{1}", products.getChinaname());
                    }
                }
                if(StringUtils.isNotBlank(finPurchase.getPurchase_amount())){
                    remarks = remarks.replace("{2}", finPurchase.getPurchase_amount());
                }
                if(StringUtils.isNotBlank(finPurchase.getUnit1())){
                    com.nt.dao_Org.Dictionary dictionary = new com.nt.dao_Org.Dictionary();
                    dictionary.setCode(finPurchase.getUnit1());
                    List<Dictionary> dir = dictionaryService.getDictionaryList(dictionary);
                    if(dir.size()>0){
                        remarks = remarks.replace("{3}", dir.get(0).getValue1());
                    }

                }
//                remarks = item.getRemarks().replace("{0}", finPurchase.getSuppliercn()).replace("{1}", finPurchase.getContractnumber());
            }

            //金额计算
            Double resultAmount = 0.00;//原币金额
            int purchase_amount = 0;//数量
            Double unitprice = 0.00;//单价
            Double hisAmount = 0.00;//采购金额
            if(StringUtils.isNotBlank(item.getAmounttype())) {
                String tar = "";
                if(StringUtils.isNotEmpty(item.getCrerate())){
                    tar = item.getCrerate();
                    com.nt.dao_Org.Dictionary dictionary = new com.nt.dao_Org.Dictionary();
                    dictionary.setCode(item.getCrerate());
                    List<Dictionary> dir = dictionaryService.getDictionaryList(dictionary);
                    if(dir.size()>0){
                        tar = dir.get(0).getValue2();
                    }
                }
                Map<Object,Double> mp = amountCalculation(item.getAmounttype(), tar, finPurchase,docurule,rate);
                resultAmount = mp.get("resultAmount");
                purchase_amount = mp.get("purchase_amount").intValue();
                unitprice = mp.get("unitprice");
                hisAmount = mp.get("hisAmount");
            }
            //分录
            accountingRule.setRemarks(remarks);//摘要
            accountingRule.setAcct_code(item.getAccountid());//科目编码
            accountingRule.setDebit(item.getDebit());//借方科目
            accountingRule.setCredit(item.getCredit());//贷方科目
            accountingRule.setCurrency(finPurchase.getCurrency1());//币种
//            accountingRule.setEx_rate(finPurchase.getEx_rate());//汇率
            accountingRule.setTaxrate(item.getCrerate());//税率
            accountingRule.setUnit(finPurchase.getUnit1());//单位
            accountingRule.setUnit_price(unitprice);//单价
            accountingRule.setQuantity(purchase_amount);//数量
            accountingRule.setOricurrency_amount(resultAmount);//原币金额
            accountingRule.setAmount(hisAmount);//金额
            accountingRule.setAmounttype(item.getAmounttype());//区分科目名

            String dim = "";
            if(StringUtils.isNotEmpty(item.getDimension())){//核算维度
                String dimSplit[] = item.getDimension().split("/");
                if(dimSplit.length > 0){
                    for(String di:dimSplit){
                        if(di.equals("供应商")){
                            Supplierbaseinfor supplierbaseinfor = supplierbaseinforMapper.selectByPrimaryKey(finPurchase.getSupplier());
                            if(supplierbaseinfor != null){
                                dim = supplierbaseinfor.getSuppliernamecn()+"/";
                                accountingRule.setFdetailid__fflex4(supplierbaseinfor.getSupnumber());
                            }
                        }
                        if(di.equals("物料")){
                            Products productsList = productsMapper.selectByPrimaryKey(finPurchase.getProducten());
                            if(productsList != null){
                                dim = dim +productsList.getChinaname()+"/";
                                accountingRule.setFdetailid__fflex8(productsList.getPronumber());
                            }
                        }
                    }
                }
            }
            if(dim != ""){//去掉末尾"/"
                dim = dim.substring(0,dim.length()-1);
            }
            accountingRule.setDimension(dim);//核算维度
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
        Double amoun1 = 0.00;
        Double amoun2 = 0.00;
        for(AccountingRule a: actgrulist){
            if(a.getAmounttype().equals("1")){
                amoun1 = a.getAmount();
            } else if(a.getAmounttype().equals("2")){
                amoun2 = a.getAmount();
            }
        }
        Double amoun3 = amoun1 - amoun2;
        for(AccountingRule a: actgrulist){
            if(a.getAmounttype().equals("3")){
                a.setAmount(amoun3);
                a.setOricurrency_amount(amoun3);
                a.setUnit_price(a.getOricurrency_amount()/a.getQuantity());
            }
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
    private Map<Object,Double> amountCalculation(String amountType, String tax, FinPurchase finPurchase,Docurule docurule,Double rate) throws ParseException {
        Map<Object,Double> dataMap = new HashMap<>();
        String realpay = "";
        Double resultAmount = 0.00;//原币金额
        Double purchase_amount = 0.00;//数量
        Double unitprice = 0.00;//单价
        Double hisAmount = 0.00;
        if(StringUtils.isNotEmpty(docurule.getDocutype())){
            if(docurule.getDocutype().equals("PZ001001") && StringUtils.isNotEmpty(finPurchase.getRealpay())){//付款凭证
                realpay = finPurchase.getRealpay();//应付金额
            } else if(docurule.getDocutype().equals("PZ001002") && finPurchase.getSumamount() != null){//收到发票凭证
//                realpay = finPurchase.getRealamount();//实付金额
                realpay = String.valueOf(finPurchase.getSumamount());//采购总金额
            }
        }

        switch (amountType) {
            case "1"://采购金额Purchaseamount
                if (StringUtils.isNotBlank(realpay) && !" ".equals(realpay)) {
                        resultAmount = Double.parseDouble(realpay);
                        hisAmount = Double.parseDouble(realpay) * rate;
                }
                break;
            case "2"://税费 = 采购金额/(1+增值税率)*增值税率
                if (StringUtils.isNotBlank(realpay) && !" ".equals(realpay)) {

                    Double pAmount = Double.parseDouble(realpay);
                    Double pAmounts = Double.parseDouble(realpay) * rate;
                    NumberFormat nf =  NumberFormat.getPercentInstance();
                    Number percent = nf.parse(tax);//13%

                    resultAmount = pAmount/(1+percent.doubleValue())*percent.doubleValue();
                    hisAmount = pAmounts/(1+percent.doubleValue())*percent.doubleValue();
                }
                break;
            case "3"://库存商品
                if (StringUtils.isNotBlank(finPurchase.getUnitprice1()) && !" ".equals(finPurchase.getUnitprice1())) {
                    Integer count = 0;
                    if (StringUtils.isNotBlank(finPurchase.getPurchase_amount()) && !" ".equals(finPurchase.getPurchase_amount())) {
                        count = Integer.parseInt(finPurchase.getPurchase_amount());
                        unitprice = Double.parseDouble(finPurchase.getUnitprice1());
                    }
                    resultAmount = Double.parseDouble(finPurchase.getUnitprice1()) * count;
                    hisAmount = Double.parseDouble(finPurchase.getUnitprice1()) * count * rate;
                    purchase_amount = Double.parseDouble(finPurchase.getPurchase_amount());
                }
                break;
        }

        dataMap.put("resultAmount",resultAmount);
        dataMap.put("hisAmount",hisAmount);
        dataMap.put("purchase_amount",purchase_amount);
        dataMap.put("unitprice",unitprice);
        return dataMap;
    }
}
