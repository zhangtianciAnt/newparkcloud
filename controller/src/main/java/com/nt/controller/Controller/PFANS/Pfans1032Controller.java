package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Petition;
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
import java.util.Map;

@RestController
@RequestMapping("/petition")
public class Pfans1032Controller {
    @Autowired
    private PetitionService petitionService;

    @Autowired
    private TokenService tokenService;

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
        Map<String, Object> data = new HashMap<>();
        data.put("pd",pd);
        ExcelOutPutUtil.OutPut(pd.getContractnumber()+"_請求書(国内受託)","qingqiushu_guonei.xlsx",data,response);
    }
}
