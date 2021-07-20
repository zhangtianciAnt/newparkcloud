package com.nt.controller.Controller.PFANS;


import com.nt.dao_Pfans.PFANS1000.Departmental;
import com.nt.service_pfans.PFANS1000.DepartmentalService;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
