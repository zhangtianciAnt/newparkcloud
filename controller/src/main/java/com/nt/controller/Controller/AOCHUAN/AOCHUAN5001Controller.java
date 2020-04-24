package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN5000.CredentialInformation;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.AccountingRule;
import com.nt.dao_AOCHUAN.AOCHUAN5000.Vo.CrdlInfo;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.DocuruleVo;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinCrdlInfoService;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinSalesService;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinAuxAcctgMapper;
import com.nt.service_AOCHUAN.AOCHUAN7000.DocuruleService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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

        Docurule docurule = new Docurule();
        DocuruleVo crerule = new DocuruleVo();
        List<Helprule> helpruleLst = null;

        CrdlInfo crdlInfo = new CrdlInfo();

        //获取凭证规则
        docurule = docuruleService.selectByDocutype("PZ001001");
        //获取分录规则
        crerule = docuruleService.One(docurule.getDocurule_id());
        //获取辅助核算项目
        helpruleLst = docuruleService.helpOne(docurule.getDocurule_id());

        if(docurule == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        if(crerule== null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //根据规则替换
        crdlInfo=replaceRule(finSales, docurule,crerule,helpruleLst);

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
     * @param docurule
     * @param crerule
     * @return
     */
    public CrdlInfo replaceRule(FinSales finSales, Docurule docurule, DocuruleVo crerule, List<Helprule> helpruleList){

        CrdlInfo crdlInfo = new CrdlInfo();

        Date busDate  = new Date();
        Date accDate  = new Date();

        if(("1").equals(docurule.getBusinessday())){
            busDate = finSales.getArrivaltime();
        }
        if(("1").equals(docurule.getNowday())){
            accDate = finSales.getArrivaltime();
        }

        //凭证基本信息
        CredentialInformation crdl = new CredentialInformation();
        crdl.setCrdltype(docurule.getDocutype());
        crdl.setCrdlword(docurule.getDocument());
        crdl.setBus_date(busDate);
        crdl.setAcct_date(accDate);
        crdl.setAttachments(docurule.getAnnexno());
        crdl.setPush_status("PX005001");
        crdl.setPush_status_nm("未推送");

        List<AccountingRule> actgrulist = null;
        List<Crerule> creruleList = crerule.getCrerules();

        for (int i = 0;i<=creruleList.size()-1;i++) {
            AccountingRule accountingRule = new AccountingRule();

            String remarks = "";
            if(!creruleList.get(i).getRemarks().isEmpty()){
                remarks = creruleList.get(i).getRemarks().replace("{0}", finSales.getCustomer()).replace("{1}", finSales.getContractnumber());
            }

            //分录
            accountingRule.setRemarks(remarks);
            accountingRule.setDebit(creruleList.get(i).getDebit());
            accountingRule.setCredit(creruleList.get(i).getCredit());
            accountingRule.setAmount(finSales.getSalesamount());
            //辅助项目
            accountingRule.setBankaccount(helpruleList.get(i).getBankaccount());
            accountingRule.setDept(helpruleList.get(i).getDepart());
            accountingRule.setIae_contg(helpruleList.get(i).getExpenditure());
            accountingRule.setAuxacctg(helpruleList.get(i).getAccounting());
            accountingRule.setMaincashflow(helpruleList.get(i).getMaincash());
            accountingRule.setAttachcashflow(helpruleList.get(i).getFlowcash());
            accountingRule.setAuxacctg_amount(finSales.getSalesamount());

            actgrulist.add(accountingRule);
        }

        crdlInfo.setCredentialInformation(crdl);
        crdlInfo.setAccountingRuleList(actgrulist);

        return crdlInfo;
    }
}
