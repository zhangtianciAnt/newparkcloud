package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS1000.Vo.AwardVo;
import com.nt.service_pfans.PFANS1000.AwardService;
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
@RequestMapping("/award")
public class Pfans1025Controller {
    @Autowired
    private AwardService awardService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{
        TokenModel tokenModel=tokenService.getToken(request);
        Award award=new Award();
        award.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(awardService.get(award));
    }

    @RequestMapping(value = "/selectById",method = {RequestMethod.GET})
    public ApiResult selectById(String award_id,HttpServletRequest request) throws Exception{
        if(award_id==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(awardService.selectById(award_id));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody AwardVo awardVo,HttpServletRequest request)throws Exception{
        if(awardVo==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        awardService.updateAwardVo(awardVo,tokenModel);
        return ApiResult.success();
    }


}
