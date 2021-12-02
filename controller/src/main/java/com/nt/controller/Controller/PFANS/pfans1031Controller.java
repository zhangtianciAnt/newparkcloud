package com.nt.controller.Controller.PFANS;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Napalm;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS1000.NapalmService;
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
@RequestMapping("/napalm")
public class pfans1031Controller {
    @Autowired
    private NapalmService napalmService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Napalm napalm = new Napalm();
        napalm.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(napalmService.get(napalm));

    }

    //  add  ml  211130  分页  from
    @RequestMapping(value="/getPage",method = {RequestMethod.GET})
    public ApiResult getPage(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Napalm napalm = new Napalm();
        napalm.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(napalmService.getPage(napalm));
    }
    //  add  ml  211130  分页  to

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Napalm napalm, HttpServletRequest request) throws Exception {
        if (napalm == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(napalmService.One(napalm.getNapalm_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody Napalm napalm, HttpServletRequest request) throws Exception{
        if (napalm == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        napalmService.update(napalm,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/downLoad1", method = {RequestMethod.POST})
    public void downLoad1(@RequestBody Napalm na, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //add_fjl_0804  生成书类的覚字去掉 start
        if (na != null) {
            if (na.getContractnumber().contains("覚")) {
                na.setContractnumber(na.getContractnumber().replace("覚", ""));
            }
            if (na.getClaimnumber().contains("覚")) {
                na.setClaimnumber(na.getClaimnumber().replace("覚", ""));
            }
        }
        //add_fjl_0804  生成书类的覚字去掉 end
        //add-ws-8/13-禅道任务432
        if(na.getRegindiff()!=null&&na.getRegindiff()!=""){
            if(na.getRegindiff().equals("BP028001")){
                na.setPjnameenglish(na.getPjnamechinese());
            }else if(na.getRegindiff().equals("BP028002")){
                na.setPjnameenglish(na.getPjnamejapanese());
            }else if(na.getRegindiff().equals("BP028003")){
                na.setPjnameenglish(na.getPjnameenglish());
            }
        }else{
            if (na.getCurrencyformat().equals("PG019003")) {
                na.setPjnameenglish(na.getPjnamechinese());
            } else if (na.getCurrencyformat().equals("PG019001")) {
                na.setPjnameenglish(na.getPjnamejapanese());
            }
        }
        //add-ws-8/13-禅道任务432
        TokenModel tokenModel=tokenService.getToken(request);
        String nn[] = na.getClaimdatetime().split(" ~ ");
        List<Dictionary> dictionaryList = dictionaryService.getForSelect("PG019");
        List<Dictionary> dictionaryList1 = dictionaryService.getForSelect("HT012");
        for(Dictionary item:dictionaryList1){
            if(item.getCode().equals(na.getToto())) {

                na.setToto(item.getValue1());
            }
        }
        Map<String, Object> data = new HashMap<>();
        //20200427 add by ztc format data start
        //請求日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat str = new SimpleDateFormat("yyyy年MM月dd日");
        Date tem_date = null;
        String str_format = "";
        str_format = str.format(na.getDeliverydate());
        data.put("deliverydate", str_format);
        str_format = str.format(na.getDeliveryfinshdate());
        data.put("deliveryfinshdate", str_format);
        str_format = str.format(na.getCompletiondate());
        data.put("completiondate", str_format);
        //請求金額
        DecimalFormat df = new DecimalFormat("###,###.00");
        BigDecimal bd = new BigDecimal(na.getClaimamount());
        str_format = df.format(bd);
        na.setClaimamount(str_format);
        //20200427 add by ztc format data end
        data.put("na",na);
        if(nn.length > 0){
            //20200427 add by ztc format date start
            str_format = nn[0];
            tem_date = sdf.parse(str_format);
            nn[0] = str.format(tem_date);
            str_format = nn[1];
            tem_date = sdf.parse(str_format);
            nn[1] = str.format(tem_date);
            //20200427 add by ztc format date end
            data.put("statime",nn);
        } else {
            data.put("statime","");
        }
//        if(na.getContracttype().equals("HT008005") || na.getContracttype().equals("HT008006") || na.getContracttype().equals("HT008007") || na.getContracttype().equals("HT008008")){
//            for(Dictionary item:dictionaryList){
//                if(item.getCode().equals(na.getCurrencyformat())) {
//
//                    na.setCurrencyformat(item.getValue4());
//                }
//            }
//
//            ExcelOutPutUtil.OutPut(na.getClaimnumber().toUpperCase()+"_納品書(国内受託)","napinshu_guonei.xlsx",data,response);
//        } else if (na.getContracttype().equals("HT008001") || na.getContracttype().equals("HT008002") || na.getContracttype().equals("HT008003") || na.getContracttype().equals("HT008004")){
        if (na.getCurrencyformat().equals("PG019003")) {
            for (Dictionary item : dictionaryList) {
                if (item.getCode().equals(na.getCurrencyformat())) {

                    na.setCurrencyformat(item.getValue4());
                }
            }
            ExcelOutPutUtil.OutPut(na.getClaimnumber().toUpperCase() + "_納品書(日本受託‐技術開発-RMB)", "napinshu_rmb.xlsx", data, response);
        } else if (na.getCurrencyformat().equals("PG019001")) {
            for (Dictionary item : dictionaryList) {
                if (item.getCode().equals(na.getCurrencyformat())) {

                    na.setCurrencyformat(item.getValue4());

                }
            }
            ExcelOutPutUtil.OutPut(na.getClaimnumber().toUpperCase() + "_納品書(日本受託‐技術開発-US$)", "napinshu_us.xlsx", data, response);
        }
//        }
    }
}
