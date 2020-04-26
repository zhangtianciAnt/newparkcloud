package com.nt.controller.Controller.PFANS;

import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.PetitionService;
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
@RequestMapping("/petition")
public class Pfans1032Controller {
    @Autowired
    private PetitionService petitionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request)throws Exception{
        TokenModel tokenModel=tokenService.getToken(request);
        Petition petition=new Petition();
        petition.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(petitionService.get(petition));
    }

    @RequestMapping(value = "/one", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody Petition petition,HttpServletRequest request) throws Exception{
        if(petition==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(petitionService.one(petition.getPetition_id()));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Petition petition,HttpServletRequest request) throws Exception{
        if(petition==null){
            return  ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        petitionService.update(petition,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/downLoad1", method = {RequestMethod.POST})
    public void downLoad1(@RequestBody Petition petition, HttpServletRequest request, HttpServletResponse response) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Petition pd = petitionService.one(petition.getPetition_id());
        Map<String, Object> data = new HashMap<>();

        //20200427 add by lin format data start
        //請求日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date tem_date = null;
        String str_format = "";
        str_format = new SimpleDateFormat("dd/MM/yyyy").format(pd.getClaimdate());
        data.put("claimdate", str_format);
        //請求金額
        DecimalFormat df = new DecimalFormat("###,###.00");
        BigDecimal bd = new BigDecimal(pd.getClaimamount());
        str_format = df.format(bd);
        pd.setClaimamount(str_format);
        //20200427 add by lin format data end

        data.put("pd", pd);

        String pp[] = petition.getClaimdatetime().split(" ~ ");
        if (pp.length > 0) {
            //20200427 add by lin format date start
            str_format = pp[0];
            tem_date = sdf.parse(str_format);
            pp[0] = new SimpleDateFormat("dd/MM/yyyy").format(tem_date);
            str_format = pp[1];
            tem_date = sdf.parse(str_format);
            pp[1] = new SimpleDateFormat("dd/MM/yyyy").format(tem_date);
            //20200427 add by lin format date end
            data.put("statime", pp);
        } else {
            data.put("statime", "");
        }
        List<Dictionary> dictionaryList = dictionaryService.getForSelect("PG019");
        if (pd.getContracttype().equals("HT008005") || pd.getContracttype().equals("HT008006") || pd.getContracttype().equals("HT008007") || pd.getContracttype().equals("HT008008")) {
            for (Dictionary item : dictionaryList) {
                if (item.getCode().equals(pd.getCurrencyposition())) {

                    pd.setCurrencyposition(item.getValue4());
                }
            }

            ExcelOutPutUtil.OutPut(pd.getClaimnumber().toUpperCase() + "_請求書(国内受託)", "qingqiushu_guonei.xlsx", data, response);
        } else if (pd.getContracttype().equals("HT008001") || pd.getContracttype().equals("HT008002") || pd.getContracttype().equals("HT008003") || pd.getContracttype().equals("HT008004")) {
            if (pd.getCurrencyposition().equals("PG019003")) {
                for (Dictionary item : dictionaryList) {
                    if (item.getCode().equals(pd.getCurrencyposition())) {

                        pd.setCurrencyposition(item.getValue4());
                    }
                }
                ExcelOutPutUtil.OutPut(pd.getClaimnumber().toUpperCase() + "_請求書(日本受託-RMB)", "qingqiushu_ribenrmb.xlsx", data, response);
            } else if (pd.getCurrencyposition().equals("PG019001")) {
                for (Dictionary item : dictionaryList) {
                    if (item.getCode().equals(pd.getCurrencyposition())) {

                        pd.setCurrencyposition(item.getValue4());
                    }
                }
                ExcelOutPutUtil.OutPut(pd.getClaimnumber().toUpperCase() + "_請求書(日本受託-US$)", "qingqiushu_ribenus.xlsx", data, response);
            }
        }
    }
    /**
     * @desc 1.0~1之间的BigDecimal小数，格式化后失去前面的0,则前面直接加上0。
     * 2.传入的参数等于0，则直接返回字符串"0.00"
     * 3.大于1的小数，直接格式化返回字符串
     * @param obj 传入的小数
     * @return
     */
    public static String formatToNumber(BigDecimal obj) {
//        DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormat df = new DecimalFormat("###,###.##");
        if (obj.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        } else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
            return "0" + df.format(obj).toString();
        } else {
            return df.format(obj).toString();
        }
    }
}
