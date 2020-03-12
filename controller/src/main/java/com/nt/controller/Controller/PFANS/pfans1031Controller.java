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
        TokenModel tokenModel=tokenService.getToken(request);
        List<Dictionary> dictionaryList = dictionaryService.getForSelect("HT006");
        List<Dictionary> dictionaryList1 = dictionaryService.getForSelect("HT012");
        for(Dictionary item:dictionaryList1){
            if(item.getCode().equals(na.getToto())) {

                na.setToto(item.getValue1());
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("na",na);
        if(na.getContracttype().equals("HT008005") || na.getContracttype().equals("HT008006") || na.getContracttype().equals("HT008007") || na.getContracttype().equals("HT008008")){
            ExcelOutPutUtil.OutPut(na.getClaimnumber().toUpperCase()+"_納品書(国内受託)","napinshu_guonei.xlsx",data,response);
        } else if (na.getContracttype().equals("HT008001") || na.getContracttype().equals("HT008002") || na.getContracttype().equals("HT008003") || na.getContracttype().equals("HT008004")){
            if (na.getCurrencyformat().equals("HT006001")){
                for(Dictionary item:dictionaryList){
                    if(item.getCode().equals(na.getCurrencyformat())) {

                        na.setCurrencyformat(item.getValue1());
                    }
                }
                ExcelOutPutUtil.OutPut(na.getClaimnumber().toUpperCase()+"_納品書(日本受託‐技術開発-RMB)","napinshu_rmb.xlsx",data,response);
            } else if (na.getCurrencyformat().equals("HT006002")){
                for(Dictionary item:dictionaryList){
                    if(item.getCode().equals(na.getCurrencyformat())) {

                        na.setCurrencyformat(item.getValue1());

                    }
                }
                ExcelOutPutUtil.OutPut(na.getClaimnumber().toUpperCase()+"_納品書(日本受託‐技術開発-US$)","napinshu_us.xlsx",data,response);
            }
        }
    }
}
