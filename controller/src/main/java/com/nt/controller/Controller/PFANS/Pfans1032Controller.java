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
        TokenModel tokenModel=tokenService.getToken(request);
        Petition pd = petitionService.one(petition.getPetition_id());
        List<Dictionary> dictionaryList = dictionaryService.getForSelect("HT006");
        Map<String, Object> data = new HashMap<>();
        data.put("pd",pd);
        if (pd.getContracttype().equals("HT008005") || pd.getContracttype().equals("HT008006") || pd.getContracttype().equals("HT008007") || pd.getContracttype().equals("HT008008")){
            ExcelOutPutUtil.OutPut(pd.getClaimnumber().toUpperCase()+"_請求書(国内受託)","qingqiushu_guonei.xlsx",data,response);
        } else if (pd.getContracttype().equals("HT008001") || pd.getContracttype().equals("HT008002") || pd.getContracttype().equals("HT008003") || pd.getContracttype().equals("HT008004")){
            if(pd.getCurrencyposition().equals("HT006001")){
                for(Dictionary item:dictionaryList){
                    if(item.getCode().equals(pd.getCurrencyposition())) {

                        pd.setCurrencyposition(item.getValue1());
                    }
                }
                ExcelOutPutUtil.OutPut(pd.getClaimnumber().toUpperCase()+"_請求書(日本受託-RMB)","qingqiushu_ribenrmb.xlsx",data,response);
            }else if(pd.getCurrencyposition().equals("HT006002")){
                for(Dictionary item:dictionaryList){
                    if(item.getCode().equals(pd.getCurrencyposition())) {

                        pd.setCurrencyposition(item.getValue1());
                    }
                }
                ExcelOutPutUtil.OutPut(pd.getClaimnumber().toUpperCase()+"_請求書(日本受託-US$)","qingqiushu_ribenus.xlsx",data,response);
            }
        }
    }

}
