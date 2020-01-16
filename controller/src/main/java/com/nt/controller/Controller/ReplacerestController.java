package com.nt.controller.Controller;

import com.nt.dao_Pfans.PFANS2000.Replacerest;
import com.nt.service_pfans.PFANS2000.ReplacerestService;
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
@RequestMapping("/replacerest")
public class ReplacerestController {

    @Autowired
    private ReplacerestService replacerestService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/getReplacerest",method = {RequestMethod.POST})
    public ApiResult getReplacerest(@RequestBody Replacerest replacerest, HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        replacerest.setOwner(tokenModel.getUserId());
        replacerest.setRecognitionstate("0");
        return ApiResult.success(replacerestService.getReplacerest(replacerest));
    }

}
