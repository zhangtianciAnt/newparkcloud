package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.All;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinCrdlInfoService;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinSalesService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.utils.*;
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

        if (finSales == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //UPDATE:FIN_SALES
        //存在Check
        if (finSalesService.existCheck(finSales)) {
            //唯一性Check
            if(! finSalesService.uniqueCheck(finSales)) {
                finSalesService.update(finSales, tokenService.getToken(request));
                finSalesService.updateTransportGood(finSales,tokenService.getToken(request));
            }else{
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        FinSales finSales1 = new FinSales();
        return ApiResult.success(finSalesService.getFinSalesList(finSales));
    }

    /**
     * 生成凭证
     */
    @RequestMapping(value = "/createCrdl", method = {RequestMethod.POST})
    public ApiResult createCrdl(@RequestBody FinSales finSales, HttpServletRequest request) throws Exception {

        if (finSales == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        Docurule docurule = new Docurule();
        List<All> accAndauxList  = new ArrayList<>();

        CrdlInfo crdlInfo = new CrdlInfo();

        //获取凭证规则
        docurule = docuruleService.selectByDocutype("PZ001001");
        if(docurule == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //获取分录规则 + 辅助核算项目
        accAndauxList = docuruleService.selectrule(docurule.getDocurule_id());
        if(accAndauxList== null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //根据规则替换
        crdlInfo=replaceRule(tokenService.getToken(request).getUserId(),finSales, docurule,accAndauxList);

        //生成凭证
        finCrdlInfoService.insert(crdlInfo,tokenService.getToken(request));

        //UPDATE:FIN_SALES
        //存在Check
        if (finSalesService.existCheck(finSales)) {
            //唯一性Check
            if(! finSalesService.uniqueCheck(finSales)) {
                finSalesService.update(finSales, tokenService.getToken(request));
            }else{
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        FinSales finSales1 = new FinSales();
        return ApiResult.success(finSalesService.getFinSalesList(finSales));
    }

    /**
     * 替换
     * @param userName
     * @param finSales
     * @param docurule
     * @param accAndauxList
     * @return
     */
    public CrdlInfo replaceRule(String userName,FinSales finSales, Docurule docurule, List<All> accAndauxList){

        CrdlInfo crdlInfo = new CrdlInfo();

        Date busDate  = new Date();
        Date accDate  = new Date();
        Date crdlNoDate = new Date();

        //业务日期
        if(("1").equals(docurule.getBusinessday())){
            busDate = finSales.getArrivaltime();
        }
        //记账日期
        if(("1").equals(docurule.getNowday())){
            accDate = finSales.getArrivaltime();
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
        crdl.setCurrency(finSales.getCurrency());

        List<AccountingRule> actgrulist = new ArrayList<>();

        for (All item:accAndauxList) {
            AccountingRule accountingRule = new AccountingRule();

            String remarks = "";
            if(StringUtils.isNotBlank(item.getRemarks())){
                remarks = item.getRemarks().replace("{0}", finSales.getCustomer()).replace("{1}", finSales.getContractnumber());
            }

            //分录
            accountingRule.setRemarks(remarks);
            accountingRule.setAcct_code(item.getAccountid());
            accountingRule.setDebit(item.getDebit());
            accountingRule.setCredit(item.getCredit());
            accountingRule.setTaxrate(item.getCrerate());
            accountingRule.setAmount(finSales.getSalesamount());
            System.out.println(finSales.getSalesamount());
            //辅助项目
            accountingRule.setBankaccount_code(item.getBankaccountid());
            accountingRule.setDept_code(item.getDepartid());
            accountingRule.setIae_contg_code(item.getExpenditureid());
            accountingRule.setAuxacctg_code(item.getAccountingid());
            accountingRule.setMaincash_code(item.getMaincashid());
            accountingRule.setAttachcash_code(item.getFlowcashid());
            accountingRule.setBankaccount(item.getBankaccount());
            accountingRule.setDept(item.getDepart());
            accountingRule.setIae_contg(item.getExpenditure());
            accountingRule.setAuxacctg(item.getAccounting());
            accountingRule.setMaincashflow(item.getMaincash());
            accountingRule.setAttachcashflow(item.getFlowcash());
            accountingRule.setAuxacctg_amount(finSales.getSalesamount());

            actgrulist.add(accountingRule);
        }

        crdlInfo.setCredentialInformation(crdl);
        crdlInfo.setAccountingRuleList(actgrulist);

        return crdlInfo;
    }

    //  获取每个月确认回款总和
    @RequestMapping(value="/getHK",method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);

        return ApiResult.success(finSalesService.getHK());

    }
}
