package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS1000.Vo.BusinessVo;
import com.nt.service_pfans.PFANS1000.BusinessService;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/business")
public class Pfans1002Controller {
    @Autowired
    private BusinessService businessService;

    @Autowired
    private JapanCondominiumService japancondominiumservice;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private OffshoreService offshoreservice;

    @Autowired
    private TicketsService ticketsService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(String businesstype,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Business business = new Business();
        business.setBusinesstype(businesstype);
        business.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(businessService.get(business));
    }
    //add-ws-7/7-禅道153
    @RequestMapping(value = "/selectById2", method = {RequestMethod.GET})
    public ApiResult selectById2(String business_id, HttpServletRequest request) throws Exception {
        if(business_id==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(ticketsService.selectById2(business_id));
    }
    //add-ws-7/10-禅道247
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody BusinessVo businessVo, HttpServletRequest request) throws Exception {
        if (businessVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessService.list(businessVo));
    }
    //add-ws-7/10-禅道247
    @RequestMapping(value = "/selectById3", method = {RequestMethod.GET})
    public ApiResult selectById3(String offshore_id, HttpServletRequest request) throws Exception {
        if(offshore_id==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessService.selectById3(offshore_id));
    }
    @RequestMapping(value = "/selectById4", method = {RequestMethod.GET})
    public ApiResult selectById4(String business_id, HttpServletRequest request) throws Exception {
        if(business_id==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(japancondominiumservice.selectById4(business_id));
    }
    //add-ws-7/7-禅道153
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String businessid, HttpServletRequest request) throws Exception {
        if (businessid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(businessService.selectById(businessid));
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult insertBusinessVo(@RequestBody BusinessVo businessVo, HttpServletRequest request) throws Exception {
        if (businessVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessService.insertBusinessVo(businessVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updateBusinessVo(@RequestBody BusinessVo businessVo, HttpServletRequest request) throws Exception {
        if (businessVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessService.updateBusinessVo(businessVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/change", method = {RequestMethod.POST})
    public ApiResult change(@RequestParam String center_id,
                            @RequestParam String group_id,
                            @RequestParam String team_id,
                            @RequestParam String budgetunit,
                            @RequestParam String change_id,
                            @RequestParam String flag,
                            HttpServletRequest request) throws Exception {
        if (center_id == null || budgetunit == null || change_id == null || flag == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessService.change(center_id, group_id, team_id, budgetunit, change_id, flag, tokenModel);
        return ApiResult.success();
    }

    /**
     * 境内外出差决裁逻辑删除，更改数据状态为1，若事业内，涉及还钱
     */
    @RequestMapping(value = "/busdelete", method = {RequestMethod.POST})
    public ApiResult busdelete(@RequestBody Business business, HttpServletRequest request) throws Exception {
        if (business == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        businessService.busdelete(business, tokenModel);
        return ApiResult.success();

    }

    //region   add  ml  220112  检索  from
    @RequestMapping(value = "/getBusinessSearch", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request,@RequestBody Business business) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        business.setOwners(tokenModel.getOwnerList());
        List<Business> resultList = businessService.getBusinessSearch(business)
                .stream().sorted(Comparator.comparing(Business::getCreateon).reversed()).collect(Collectors.toList());
        return ApiResult.success(resultList);
    }
    //endregion   add  ml  220112  检索  to

}
