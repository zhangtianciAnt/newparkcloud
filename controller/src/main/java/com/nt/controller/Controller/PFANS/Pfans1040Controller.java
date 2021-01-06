package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.PersonnelPlan;
import com.nt.dao_Pfans.PFANS1000.ThemePlan;
import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemePlanDetailVo;
import com.nt.dao_Pfans.PFANS1000.Vo.ThemePlanVo;
import com.nt.service_pfans.PFANS1000.ThemePlanService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/themeplan")
public class Pfans1040Controller {

    //收支データ
    @Autowired
    private ThemePlanService themePlanService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getdataList", method = {RequestMethod.GET})
    public ApiResult getdataList(String groupid,String year,HttpServletRequest request) throws Exception {

        return ApiResult.success(themePlanService.getAll(groupid,year));
    }
    @RequestMapping(value = "/getthemename", method = {RequestMethod.GET})
    public ApiResult getthemename(String themename, HttpServletRequest request) throws Exception {
        return ApiResult.success(themePlanService.getthemename(themename));
    }
    //add-ws-01/06-禅道任务710
    @RequestMapping(value = "/themenametype", method = {RequestMethod.GET})
    public ApiResult themenametype(String type, HttpServletRequest request) throws Exception {
        return ApiResult.success(themePlanService.themenametype(type));
    }
    //add-ws-01/06-禅道任务710
    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public ApiResult list(@RequestBody ThemePlan themePlan, HttpServletRequest request) throws Exception {
        if (themePlan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        themePlan.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(themePlanService.getList(themePlan));
    }

    @RequestMapping(value = "/getdetilList", method = {RequestMethod.POST})
    public ApiResult detilList(@RequestBody ThemePlanDetail themePlanDetail, HttpServletRequest request) throws Exception {
        if (themePlanDetail == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        themePlanDetail.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(themePlanService.detilList(themePlanDetail));
    }


    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody ThemePlanVo themePlan, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        themePlanService.update(themePlan, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/inserttheme", method = {RequestMethod.POST})
    public ApiResult inserttheme(@RequestBody List<ThemePlanDetailVo> themePlanDetailVo, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        themePlanService.inserttheme(themePlanDetailVo, tokenModel);
        return ApiResult.success();
    }

}
