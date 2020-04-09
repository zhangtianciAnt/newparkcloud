package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Wages;
import com.nt.service_pfans.PFANS2000.WagesService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/wages")
public class WagesController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private WagesService wagesService;

    @RequestMapping(value = "/getWagesByGivingId", method = {RequestMethod.GET})
    public ApiResult getWagesByGivingId(HttpServletRequest request, @RequestParam String givingId) throws Exception {
        return new ApiResult(wagesService.getWagesByGivingId(givingId));
    }

    @RequestMapping(value = "/insertWages", method = {RequestMethod.POST})
    public ApiResult insertWages(HttpServletRequest request, @RequestBody List<Wages> wagesList) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return new ApiResult(wagesService.insertWages(wagesList, tokenModel));
    }
}
