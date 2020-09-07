package com.nt.controller.Controller.PFANS;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.service_pfans.PFANS2000.AbNormalService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/abNormal")
public class Pfans2016Controller {

    @Autowired
    private AbNormalService abNormalService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ApiResult list(HttpServletRequest request,@RequestParam String dates) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        AbNormal abNormal = new AbNormal();
        abNormal.setOwners(tokenModel.getOwnerList());
        List<AbNormal> AbNormalList = abNormalService.list(abNormal);
        //add-gbb-6/28-禅道166 添加申请日期筛选
        if(!dates.equals("Invalid date")){
            AbNormalList = AbNormalList.stream().filter(item -> DateUtil.format(item.getApplicationdate(),"yyyy-MM").equals(dates)).collect(Collectors.toList());
        }
        //add-gbb-6/28-禅道166 添加申请日期筛选
        return ApiResult.success(AbNormalList);
    }
    //add-ws-6/8-禅道035
    @RequestMapping(value = "/list2", method = {RequestMethod.POST})
    public ApiResult list2(@RequestBody AbNormal abNormal,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        List<AbNormal> abNormalList = abNormalService.list2(abNormal);
//        abNormalList = abNormalList.stream().filter(item -> (item.getUser_id().equals(tokenModel.getUserId()))).collect(Collectors.toList());
        return ApiResult.success(abNormalList);
    }
    //add-ws-6/8-禅道035
    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        try{
            if (abNormal == null) {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
            TokenModel tokenModel = tokenService.getToken(request);
            //未承认
            abNormal.setRecognitionstate(AuthConstants.RECOGNITION_FLAG_NO);
            abNormalService.insert(abNormal, tokenModel);
            return ApiResult.success();
        }catch(Exception e){
            return ApiResult.fail(e.getMessage());
        }
    }

    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        abNormalService.upd(abNormal, tokenModel);
        return ApiResult.success();
    }

    //ADD_FJL_0904  添加删除data
    @RequestMapping(value = "/deleteInfo", method = {RequestMethod.POST})
    public ApiResult deleteInformation(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        abNormalService.delete(abNormal, tokenModel);
        return ApiResult.success();
    }
    //ADD_FJL_0904  添加删除data

    @RequestMapping(value = "/oneInfo", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(abNormalService.One(abNormal.getAbnormalid()));
    }

    @RequestMapping(value = "/cklength", method = {RequestMethod.POST})
    public ApiResult cklength(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(abNormalService.cklength(abNormal));
    }

    @RequestMapping(value = "/updateOvertime", method = {RequestMethod.POST})
    public ApiResult updateOvertime(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        abNormalService.updateOvertime(abNormal);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getSickleave", method = {RequestMethod.GET})
    public ApiResult getSickleave(String userid, HttpServletRequest request) throws Exception {
        if (userid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(abNormalService.getSickleave(userid));
    }


    @RequestMapping(value = "/selectAbNormalParent", method = {RequestMethod.GET})
    public ApiResult selectAbNormalParent(String userid, HttpServletRequest request) throws Exception {
        if (userid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(abNormalService.selectAbNormalParent(userid));
    }

    //add_fjl_05/26 --添加代休剩余
    @RequestMapping(value = "/getRestday", method = {RequestMethod.GET})
    public ApiResult getRestday(String userid, HttpServletRequest request) throws Exception {
        if (userid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(abNormalService.getRestday(userid));
    }
    //add_fjl_05/26 --添加代休剩余

    //    add_fjl_06/16  -- 添加异常申请每天累计不超过8小时check  start
    @RequestMapping(value = "/getLeaveNumber", method = {RequestMethod.POST})
    public ApiResult getLeaveNumber(@RequestBody AbNormal abNormal, HttpServletRequest request) throws Exception {
        if (abNormal == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(abNormalService.getLeaveNumber(abNormal));
    }
//    add_fjl_06/16  -- 添加异常申请每天累计不超过8小时check  end

    //add ccm 0806 查询申请人的剩余年休
    @RequestMapping(value = "/getremainingByuserid", method = {RequestMethod.GET})
    public ApiResult getremainingByuserid(String userid, HttpServletRequest request) throws Exception {
        if (userid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(abNormalService.getremainingByuserid(userid));
    }
    //add ccm 0806 查询申请人的剩余年休
}
