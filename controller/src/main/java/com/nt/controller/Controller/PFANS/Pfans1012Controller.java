package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.service_pfans.PFANS2000.PublicExpenseService;
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
@RequestMapping("/publicexpense")
public class Pfans1012Controller {
    @Autowired
    private PublicExpenseService publicExpenseService;
    @Autowired
    private TokenService tokenService;

    /*
    * 列表查看
    * */
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{
        TokenModel tokenModel=tokenService.getToken(request);
        PublicExpense publicExpense=new PublicExpense();
        publicExpense.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(publicExpenseService.get(publicExpense));
    }











}
