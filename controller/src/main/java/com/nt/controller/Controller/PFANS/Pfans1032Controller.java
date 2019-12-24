package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Petition;
import com.nt.service_pfans.PFANS1000.PetitionService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping(value = "/selectById", method = {RequestMethod.POST})
    public ApiResult selectById(@RequestBody Petition petition,HttpServletRequest request) throws Exception{
        if(petition==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(petitionService.selectById(petition.getPetition_id()));
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

}
