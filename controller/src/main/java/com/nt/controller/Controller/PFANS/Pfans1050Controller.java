package com.nt.controller.Controller.PFANS;


import com.nt.dao_Pfans.PFANS1000.Departmental;
import com.nt.service_pfans.PFANS1000.DepartmentalService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/departmental")
public class Pfans1050Controller {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private DepartmentalService departmentalService;

    @PostMapping("/getexpatureList")
    public ApiResult getExpatureList() throws Exception {
        departmentalService.getExpatureList();
        return ApiResult.success();
    }

    @PostMapping("/getDepartmental")
    public ApiResult getDepartmental(String years, String group_id) throws Exception {
        return ApiResult.success(departmentalService.getDepartmental(years,group_id));
    }

    @RequestMapping(value = "/getTable1050infoReport", method = {RequestMethod.GET})
    public ApiResult getTable1050infoReport(String year, String group_id, HttpServletRequest request) throws Exception {
        return ApiResult.success("getTable1050infoReport",departmentalService.getTable1050infoReport(year, group_id));
    }

}
