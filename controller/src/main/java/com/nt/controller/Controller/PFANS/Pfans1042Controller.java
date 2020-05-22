package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Contracttheme;
import com.nt.dao_Pfans.PFANS1000.CostCarryForward;
import com.nt.service_pfans.PFANS1000.ContractthemeService;
import com.nt.service_pfans.PFANS1000.PltabService;
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
@RequestMapping("/Pltab")
public class Pfans1042Controller {


    @Autowired
    private PltabService pltabService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getPltab",method={RequestMethod.GET})
    public ApiResult one( @RequestParam String groupid, @RequestParam String year, @RequestParam String month,HttpServletRequest request) throws Exception {
        if (groupid == null || year == null || month == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(pltabService.selectPl(groupid,year,month));
    }

    @RequestMapping(value = "/getCostLast", method = {RequestMethod.GET})
    public ApiResult getCostLast(@RequestParam String groupid, @RequestParam String year, @RequestParam String month, HttpServletRequest request) throws Exception {
        if (groupid == null || year == null || month == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(pltabService.getCostLast(groupid, year, month));
    }

    @RequestMapping(value = "/getCostList", method = {RequestMethod.GET})
    public ApiResult getCostList(@RequestParam String groupid, @RequestParam String year, @RequestParam String month, HttpServletRequest request) throws Exception {
        if (groupid == null || year == null || month == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(pltabService.getCostList(groupid, year, month));
    }

    @RequestMapping(value="/insert",method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody List<CostCarryForward> costcarryforward ,HttpServletRequest request) throws Exception{
        if(costcarryforward==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        pltabService.inset(costcarryforward,tokenModel);
        return ApiResult.success();
    }

}
