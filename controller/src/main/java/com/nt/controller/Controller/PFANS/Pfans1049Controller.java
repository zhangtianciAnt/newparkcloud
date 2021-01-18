package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS1000.IncomeExpenditure;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessVo;
import com.nt.service_pfans.PFANS1000.BusinessService;
import com.nt.service_pfans.PFANS1000.IncomeExpenditureService;
import com.nt.service_pfans.PFANS1000.OffshoreService;
import com.nt.service_pfans.PFANS3000.JapanCondominiumService;
import com.nt.service_pfans.PFANS3000.TicketsService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/incomeexpenditure")
public class Pfans1049Controller {


    @Autowired
    private IncomeExpenditureService incomeexpenditureservice;
    @Autowired
    private TokenService tokenService;

    @GetMapping("list")
    public ApiResult list(HttpServletRequest request) throws Exception {
        return ApiResult.success(incomeexpenditureservice.getdatalist());
    }

    @GetMapping("/selectlist")
    public ApiResult selectlist(String year, String group_id, HttpServletRequest request) throws Exception {
        return ApiResult.success(incomeexpenditureservice.selectlist(year, group_id));
    }

    @PostMapping("/insert")
    public ApiResult insert(@RequestBody List<IncomeExpenditure> incomeexpenditure, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        incomeexpenditureservice.insert(incomeexpenditure, tokenModel);
        return ApiResult.success();
    }
}
