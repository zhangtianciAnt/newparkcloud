package com.nt.controller.Controller.PFANS;

import com.nt.controller.Controller.DictionaryController;
import com.nt.dao_Org.Dictionary;
import com.nt.service_Org.DictionaryService;
import com.nt.dao_Pfans.PFANS1000.Quotation;
import com.nt.dao_Pfans.PFANS1000.Vo.QuotationVo;
import com.nt.service_pfans.PFANS1000.QuotationService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quotation")
public class Pfans1027Controller {

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Quotation quotation =new Quotation();
        quotation.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(quotationService.get(quotation));
    }

    //  add  ml  211130  报价单分页  from
    @RequestMapping(value = "/getQuotation", method = {RequestMethod.GET})
    public ApiResult getQuotation(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Quotation quotation =new Quotation();
        quotation.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(quotationService.getQuotation(quotation));
    }
    //  add  ml  211130  报价单分页  to

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String quotationid, HttpServletRequest request) throws Exception {
        if (quotationid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(quotationService.selectById(quotationid));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody QuotationVo quotationVo, HttpServletRequest request) throws Exception {
        if (quotationVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        quotationService.update(quotationVo, tokenModel);
        return ApiResult.success();

    }

    @RequestMapping(value = "/downLoad", method = {RequestMethod.POST})
    public void downLoad(@RequestBody Quotation quotation, HttpServletRequest request, HttpServletResponse response) throws Exception{
        TokenModel tokenModel=tokenService.getToken(request);
        QuotationVo qu = quotationService.selectById(quotation.getQuotationid());
        //add_fjl_0804  生成书类的覚字去掉 start
        if (qu.getQuotation() != null) {
            if (qu.getQuotation().getContractnumber().contains("覚")) {
                qu.getQuotation().setContractnumber(qu.getQuotation().getContractnumber().replace("覚", ""));
            }
        }
        //add_fjl_0804  生成书类的覚字去掉 end
        //add-ws-8/13-禅道任务432
        if(qu.getQuotation().getRegindiff()!=null&&qu.getQuotation().getRegindiff()!=""){
            if(qu.getQuotation().getRegindiff().equals("BP028001")){
                qu.getQuotation().setPjnameenglish(qu.getQuotation().getPjchinese());
            }else if(qu.getQuotation().getRegindiff().equals("BP028002")){
                qu.getQuotation().setPjnameenglish(qu.getQuotation().getPjjapanese());
            }else if(qu.getQuotation().getRegindiff().equals("BP028003")){
                qu.getQuotation().setPjnameenglish(qu.getQuotation().getPjnameenglish());
            }
        }else{
            qu.getQuotation().setPjnameenglish(qu.getQuotation().getPjjapanese());
        }
        //add-ws-8/13-禅道任务432
        String qq[] = quotation.getClaimdatetime().split(" ~ ");
        List<Dictionary> dictionaryList = dictionaryService.getForSelect("PG019");
        for(Dictionary item:dictionaryList){
            if(item.getCode().equals(qu.getQuotation().getCurrencyposition())) {
                qu.getQuotation().setCurrencyposition(item.getValue4());
            }
        }
        Map<String, Object> data = new HashMap<>();
        //20200427 add by ztc format data start
        //請求日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat str = new SimpleDateFormat("dd/MM/yyyy");
        Date tem_date = null;
        String str_format = "";
        DecimalFormat df = new DecimalFormat("###,###.00");
        BigDecimal bd = new BigDecimal(qu.getQuotation().getClaimamount());
        str_format = df.format(bd);
        data.put("claimam", str_format);
        for (int i = 0; i < qu.getNumbercounts().size(); i++) {
            String str_format1 = "";
            bd = new BigDecimal(qu.getNumbercounts().get(i).getClaimamount());
            str_format1 = df.format(bd);
            qu.getNumbercounts().get(i).setClaimamount(str_format1);
        }
        //禅道179任务修改

        int scale = 2;//设置位数
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        float sum = 0f;
        float sum1 = 0f;
        for (int c = 0; c < qu.getPersonfee().size(); c++) {
            if(qu.getPersonfee().get(c).getFunctionamount1()!=null&&qu.getPersonfee().get(c).getFunctionamount1()!=""){
                BigDecimal bd3 = new BigDecimal(qu.getPersonfee().get(c).getFunctionamount1());
                bd3 = bd3.setScale(scale, roundingMode);
                sum += bd3.floatValue();
            }
            if(qu.getPersonfee().get(c).getFunctionhour1()!=null&&qu.getPersonfee().get(c).getFunctionhour1()!="") {
                BigDecimal bd4 = new BigDecimal(qu.getPersonfee().get(c).getFunctionhour1());
                bd4 = bd4.setScale(scale, roundingMode);
                sum1 += bd4.floatValue();
            }
        }
        data.put("sum",sum);
        data.put("sum1",sum1);
        //禅道179任务修改
        data.put("qu",qu.getQuotation());
        data.put("qulist",qu.getOthpersonfee());
//        data.put("qualist",qu.getFruit());
        data.put("num", qu.getNumbercounts());
        data.put("qlist", qu.getPersonfee());
        if(qq.length > 0){
            //20200427 add by ztc format date start
            str_format = qq[0];
            tem_date = sdf.parse(str_format);
            qq[0] = str.format(tem_date);
            str_format = qq[1];
            tem_date = sdf.parse(str_format);
            qq[1] = str.format(tem_date);
            data.put("statime",qq);
        } else {
            data.put("statime","");
        }
        ExcelOutPutUtil.OutPut(qu.getQuotation().getContractnumber().toUpperCase() + "_报价单", "jianjishu_shoutuo.xlsx", data, response);
    }

}
