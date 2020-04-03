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
        List<Dictionary> dictionaryList = dictionaryService.getForSelect("HT006");
        for(Dictionary item:dictionaryList){
            if(item.getCode().equals(qu.getQuotation().getCurrencyposition())) {
                qu.getQuotation().setCurrencyposition(item.getValue1());
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("qu",qu.getQuotation());
        data.put("qlist",qu.getPersonfee());
        data.put("qulist",qu.getOthpersonfee());
        data.put("qualist",qu.getFruit());
        data.put("num",qu.getNumbercounts());
        if(qq.length > 0){
            data.put("statime",qq);
        } else {
            data.put("statime","");
        }
        ExcelOutPutUtil.OutPut(qu.getQuotation().getContractnumber().toUpperCase()+"_报价单","jianjishu_shoutuo.xlsx",data,response);
    }

}
