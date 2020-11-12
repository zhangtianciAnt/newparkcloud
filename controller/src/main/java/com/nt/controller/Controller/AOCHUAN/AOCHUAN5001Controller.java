package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.All;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinCrdlInfoService;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinSalesService;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/finsales")
public class AOCHUAN5001Controller {

    @Autowired
    private FinSalesService finSalesService;

    @Autowired
    private FinCrdlInfoService finCrdlInfoService;

    @Autowired
    private DocuruleService docuruleService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取费用表数据
     */
    @RequestMapping(value = "/getFinSalesList", method = {RequestMethod.POST})
    public ApiResult getFinSalesList(HttpServletRequest request) throws Exception {
        FinSales finSales = new FinSales();
        return ApiResult.success(finSalesService.getFinSalesList(finSales));
    }

    /**
     * 状态更新
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody FinSales finSales, HttpServletRequest request) throws Exception {

        if (finSales == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //UPDATE:FIN_SALES
        //存在Check
        if (finSalesService.existCheck(finSales)) {
            //唯一性Check
            if (!finSalesService.uniqueCheck(finSales)) {
                finSalesService.update(finSales, tokenService.getToken(request));
                finSalesService.updateTransportGood(finSales, tokenService.getToken(request));
            } else {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        } else {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        FinSales finSales1 = new FinSales();
        return ApiResult.success(finSalesService.getFinSalesList(finSales));
    }

    /**
     * 生成回款凭证
     */
    @RequestMapping(value = "/createCrdl", method = {RequestMethod.POST})
    public ApiResult createCrdl(@RequestBody FinSales finSales, HttpServletRequest request) throws Exception {

        if (finSales == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        Docurule docurule = new Docurule();
        List<All> accAndauxList = new ArrayList<>();

        CrdlInfo crdlInfo = new CrdlInfo();

        //获取凭证规则
        docurule = docuruleService.selectByDocutype("PZ001004");// PZ001004 - 收到国外货款
        if (docurule == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //获取分录规则 + 辅助核算项目
        accAndauxList = docuruleService.selectrule(docurule.getDocurule_id());
        if (accAndauxList == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //根据规则替换
        crdlInfo = replaceRule(tokenService.getToken(request).getUserId(), finSales, docurule, accAndauxList);

        if (crdlInfo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //生成凭证
        finCrdlInfoService.insert(crdlInfo, tokenService.getToken(request));

        //UPDATE:FIN_SALES
        //存在Check
        if (finSalesService.existCheck(finSales)) {
            //唯一性Check
            if (!finSalesService.uniqueCheck(finSales)) {
                finSales.setCredential_arrival(crdlInfo.getCredentialInformation().getCrdl_num());
                finSalesService.update(finSales, tokenService.getToken(request));
            } else {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        } else {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        FinSales finSales1 = new FinSales();
        return ApiResult.success(finSalesService.getFinSalesList(finSales1));
    }

    /**
     * 生成销售凭证
     */
    @RequestMapping(value = "/createSalesCrdl", method = {RequestMethod.POST})
    public ApiResult createSalesCrdl(@RequestBody FinSales finSales, HttpServletRequest request) throws Exception {

        if (finSales == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        Docurule docurule = new Docurule();
        List<All> accAndauxList = new ArrayList<>();

        CrdlInfo crdlInfo = new CrdlInfo();

        //获取凭证规则
        docurule = docuruleService.selectByDocutype("PZ001003"); //PZ001003 - 销售货物
        if (docurule == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //获取分录规则 + 辅助核算项目
        accAndauxList = docuruleService.selectrule(docurule.getDocurule_id());
        if (accAndauxList == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //根据规则替换
        crdlInfo = replaceRule(tokenService.getToken(request).getUserId(), finSales, docurule, accAndauxList);
        if (crdlInfo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //生成凭证
        finCrdlInfoService.insert(crdlInfo, tokenService.getToken(request));

        //UPDATE:FIN_SALES
        //存在Check
        if (finSalesService.existCheck(finSales)) {
            //唯一性Check
            if (!finSalesService.uniqueCheck(finSales)) {
                finSales.setCredential_sales(crdlInfo.getCredentialInformation().getCrdl_num());
                finSalesService.update(finSales, tokenService.getToken(request));
            } else {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        } else {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        FinSales finSales1 = new FinSales();
        return ApiResult.success(finSalesService.getFinSalesList(finSales1));
    }

    //  获取每个月确认回款总和
    @RequestMapping(value = "/getHK", method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);

        return ApiResult.success(finSalesService.getHK());

    }

    /**
     * 替换
     *
     * @param userName
     * @param finSales
     * @param docurule
     * @param accAndauxList
     * @return
     */
    public CrdlInfo replaceRule(String userName, FinSales finSales, Docurule docurule, List<All> accAndauxList) {

        CrdlInfo crdlInfo = new CrdlInfo();

        Date busDate = new Date();
        Date accDate = new Date();
        Date crdlNoDate = new Date();

        //业务日期
        if (("2").equals(docurule.getBusinessday())) {
            if ("PW001002".equals(finSales.getCredential_status())) {
                busDate = finSales.getCreateon();
            } else {
                busDate = finSales.getArrivaltime();
            }
        }
        //记账日期
        if (("2").equals(docurule.getNowday())) {
            if ("PW001002".equals(finSales.getCredential_status())) {
                busDate = finSales.getCreateon();
            } else {
                accDate = finSales.getArrivaltime();
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        //凭证基本信息
        CredentialInformation crdl = new CredentialInformation();
        crdl.setCrdl_num(userName.substring(0, 12) + sdf.format(crdlNoDate) + UUID.randomUUID().toString().substring(0, 7));//凭证编号
        crdl.setCrdltype(docurule.getDocutype());//凭证类型
        crdl.setCrdlword(docurule.getDocument());//凭证字
        crdl.setBus_date(busDate);//业务日期
        crdl.setAcct_date(accDate);//记账日期
        crdl.setAttachments(docurule.getAnnexno());//附件数
        crdl.setPush_status("PZ005001");//推送状态id
        crdl.setPush_status_nm("未推送");//推送状态
        crdl.setOrder_no(finSales.getContractnumber());//订单号

        List<AccountingRule> actgrulist = new ArrayList<>();

        for (All item : accAndauxList) {
            AccountingRule accountingRule = new AccountingRule();

//            String remarks = "";
//            if (StringUtils.isNotBlank(item.getRemarks()) && item.getRemarks().indexOf("{0}") > 0 && item.getRemarks().indexOf("{1}") > 0 && item.getRemarks().indexOf("{2}") > 0) {
////                remarks = item.getRemarks().replace("{0}", finSales.getContractnumber()).replace("{1}", finSales.getProductus()).replace("{2}", finSales.getAmount());
//                remarks = item.getRemarks().replace("{0}", finSales.getContractnumber()).replace("{1}", finSales.getProductus());
//            } else {
//                return null;
//            }

            //金额计算
            Double calAmount = 0.00;
            if (StringUtils.isNotBlank(item.getAmounttype())) {
                calAmount = amountCalculation(item.getAmounttype(), finSales);
            }
            //分录
            accountingRule.setRemarks(item.getRemarks());//摘要
            accountingRule.setAcct_code(item.getAccountid());//科目编码
            accountingRule.setDebit(item.getDebit());//借方科目
            accountingRule.setCredit(item.getCredit());//贷方科目
            accountingRule.setCurrency(finSales.getCurrency());//币种
            accountingRule.setEx_rate(finSales.getEx_rate());//汇率
            accountingRule.setTaxrate(item.getCrerate());//税率
            accountingRule.setOricurrency_amount(Double.parseDouble(finSales.getSalesamount()));//原币金额
            accountingRule.setUnit(item.getUnitname());//单位
            accountingRule.setUnit_price(Double.parseDouble(finSales.getUnitprice()));//单价
            accountingRule.setQuantity(Integer.parseInt(finSales.getAmount()));//数量
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
            accountingRule.setAuxacctg_amount(finSales.getSalesamount());//辅助账金额

            actgrulist.add(accountingRule);
        }

        crdlInfo.setCredentialInformation(crdl);
        crdlInfo.setAccountingRuleList(actgrulist);

        return crdlInfo;
    }

    /**
     * 金额计算
     *
     * @param amountType
     * @param finSales
     * @return
     */
    private Double amountCalculation(String amountType, FinSales finSales) {

        Double resultAmount = 0.00;

        switch (amountType) {
            case "0"://销售金额
                if (StringUtils.isNotBlank(finSales.getSalesamount()) && !" ".equals(finSales.getSalesamount())) {

                    if ("PY008002".equals(finSales.getCurrency())) {
                        resultAmount = Double.parseDouble(finSales.getSalesamount()) * Double.parseDouble(finSales.getEx_rate());
                    } else {
                        resultAmount = Double.parseDouble(finSales.getSalesamount());
                    }
                }
                break;
            case "4"://保费
                if (StringUtils.isNotBlank(finSales.getPremium()) && !" ".equals(finSales.getPremium())) {
                    if ("PY008002".equals(finSales.getCurrency())) {
                        resultAmount = Double.parseDouble(finSales.getPremium()) * Double.parseDouble(finSales.getEx_rate());
                    } else {
                        resultAmount = Double.parseDouble(finSales.getPremium());
                    }
                }
                break;
            case "5"://运费
                if (StringUtils.isNotBlank(finSales.getFreight()) && !" ".equals(finSales.getFreight())) {
                    if ("PY008002".equals(finSales.getCurrency())) {
                        resultAmount = Double.parseDouble(finSales.getFreight()) * Double.parseDouble(finSales.getEx_rate());
                    } else {
                        resultAmount = Double.parseDouble(finSales.getFreight());
                    }
                }
                break;
            case "6"://手续费
                if ("PY008002".equals(finSales.getCurrency())) {
                    resultAmount = finSales.getCommission_amount() * Double.parseDouble(finSales.getEx_rate());
                } else {
                    resultAmount = finSales.getCommission_amount();
                }
                break;
            case "7"://主营业务收入=应收款-（保费+运费)
                if (StringUtils.isNotBlank(finSales.getSalesamount()) && !" ".equals(finSales.getSalesamount())) {
                    Double premium = 0.00;
                    Double freight = 0.00;
                    //运费
                    if (StringUtils.isNotBlank(finSales.getPremium()) && !" ".equals(finSales.getPremium())) {
                        premium = Double.parseDouble(finSales.getPremium());
                    }
                    //保费
                    if (StringUtils.isNotBlank(finSales.getFreight()) && !" ".equals(finSales.getFreight())) {
                        freight = Double.parseDouble(finSales.getFreight());
                    }
                    if ("PY008002".equals(finSales.getCurrency())) {
                        resultAmount = (Double.parseDouble(finSales.getSalesamount()) - (premium + freight)) * Double.parseDouble(finSales.getEx_rate());
                    } else {
                        resultAmount = Double.parseDouble(finSales.getSalesamount()) - (premium + freight);
                    }
                }
                break;
            case "8"://结算款=应收款-手续费
                if (StringUtils.isNotBlank(finSales.getSalesamount()) && !" ".equals(finSales.getSalesamount())) {
                    if ("PY008002".equals(finSales.getCurrency())) {
                        resultAmount = (Double.parseDouble(finSales.getSalesamount()) - finSales.getCommission_amount()) * Double.parseDouble(finSales.getEx_rate());
                    } else {
                        resultAmount = Double.parseDouble(finSales.getSalesamount()) - finSales.getCommission_amount();
                    }
                }
                break;
        }

        return resultAmount;
    }


    /**
     * 状态更新
     */
    @RequestMapping(value = "/updateall", method = {RequestMethod.POST})
    public ApiResult updateall(@RequestBody List<FinSales> finSales, HttpServletRequest request) throws Exception {
        finSalesService.updateall(finSales, tokenService.getToken(request));
        return ApiResult.fail("保存成功");
    }


    /**
     * 状态更新未回款
     */
    @RequestMapping(value = "/updateallw", method = {RequestMethod.POST})
    public ApiResult updateallw(@RequestBody List<FinSales> finSales, HttpServletRequest request) throws Exception {
        finSalesService.updateallw(finSales, tokenService.getToken(request));
        return ApiResult.fail("保存成功");
    }


}
