package com.nt.controller.Controller.PFANS;

import com.nt.service_pfans.PFANS2000.WagesService;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wages")
public class WagesController {

    @Autowired
    private WagesService wagesService;

    @RequestMapping(value = "/getWagesByGivingId", method = {RequestMethod.GET})
    public ApiResult getWagesByGivingId(HttpServletRequest request, @RequestParam String givingId) throws Exception {
        return new ApiResult(wagesService.getWagesByGivingId(givingId));
    }
}
