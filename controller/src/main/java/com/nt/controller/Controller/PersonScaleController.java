package com.nt.controller.Controller;


import com.nt.dao_Org.PersonScale;
import com.nt.dao_Org.PersonScaleMee;
import com.nt.service_Org.PersonScaleService;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/personScale")
public class PersonScaleController {

    @Autowired
    private PersonScaleService personScaleService;

    @RequestMapping(value = "/calMonthScaleInfo", method = {RequestMethod.POST})
    public ApiResult calMonthScaleInfo(HttpServletRequest request) throws Exception{
        personScaleService.calMonthScaleInfo();
        return ApiResult.success();
    }

    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public ApiResult getList(@RequestBody PersonScaleMee personScaleMee, HttpServletRequest request) throws Exception{
        return ApiResult.success(personScaleService.getList(personScaleMee));
    }

    @RequestMapping(value = "/getPeopleInfo", method = {RequestMethod.GET})
    public ApiResult getPeopleInfo(@RequestParam String personScaleMee_id, @RequestParam String yearMonth, HttpServletRequest request) throws Exception{
        return ApiResult.success(personScaleService.getPeopleInfo(personScaleMee_id,yearMonth));
    }
}
