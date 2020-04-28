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
        data.put("claimamount", str_format);
        for (int i = 0; i < qu.getNumbercounts().size(); i++) {
            bd = new BigDecimal(qu.getNumbercounts().get(i).getClaimamount());
            str_format = df.format(bd);
            qu.getNumbercounts().get(i).setClaimamount(str_format);
        }


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
        ExcelOutPutUtil.OutPut(qu.getQuotation().getContractnumber().toUpperCase()+"_报价单","jianjishu_shoutuo.xlsx",data,response);
    }

}
