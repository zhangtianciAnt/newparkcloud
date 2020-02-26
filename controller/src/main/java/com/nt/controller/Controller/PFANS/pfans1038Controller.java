package com.nt.controller.Controller.PFANS;


import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.PersonnelplanService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/personnelplan")
public class pfans1038Controller {

    @Autowired
    private PersonnelplanService personnelplanService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getcustomer", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request, @RequestParam String id) throws Exception {
       List<CustomerInfo> customerInfoList =  personnelplanService.SelectCustomer(id);
        return ApiResult.success(customerInfoList);
    }

}
