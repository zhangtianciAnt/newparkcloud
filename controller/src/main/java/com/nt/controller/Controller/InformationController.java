package com.nt.controller.Controller;




import com.nt.dao_Org.Information;
import com.nt.service_Org.InformationService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/information")
public class InformationController {

    @Autowired
    private InformationService informationService;

    @Autowired
    private TokenService tokenService;


    @RequestMapping(value = "/saveinformation", method = {RequestMethod.POST})
    public ApiResult save(@RequestBody Information information, HttpServletRequest request) throws Exception {
        if (information == null || StringUtils.isEmpty(information)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.PARAM_ERR_02));
        }
        if(information.getStatus()==null)
        {
            TokenModel tokenModel = tokenService.getToken(request);
            information.preInsert(tokenModel);
            information.setReleaseperson(tokenModel.getUserId());
            information.setReleasetime(new Date());
            informationService.save(information);
        }
        else
        {
            if(information.getStatus().equals("1"))
            {
                TokenModel tokenModel = tokenService.getToken(request);
                information.preUpdate(tokenModel);
                information.setReleaseperson(tokenModel.getUserId());
                information.setReleasetime(new Date());
                informationService.save(information);
            }
            if(information.getStatus().equals("0"))
            {
                TokenModel tokenModel = tokenService.getToken(request);
                information.preInsert(tokenModel);
                information.setReleaseperson(tokenModel.getUserId());
                information.setReleasetime(new Date());
                informationService.save(information);
            }
        }
        return ApiResult.success();
    }

    //获取消息
    @RequestMapping(value = "/getinformation", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        Information information = new Information();
        TokenModel tokenModel = tokenService.getToken(request);
        information.setTenantid(tokenModel.getTenantId());
        information.setOwners(tokenModel.getOwnerList());
        information.setIds(tokenModel.getIdList());
        return ApiResult.success(informationService.get(information));
    }






}
