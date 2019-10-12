package com.nt.controller.Controller;

import com.nt.service_pfans.PFANS8000.InformationDeliveryService;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/informationdelivery")
public class InformationDeliveryController {
    //查找信息发布
    @Autowired
    private InformationDeliveryService informationService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getInformation() throws Exception{
       return ApiResult.success(informationService.getInformation());
    }
}
