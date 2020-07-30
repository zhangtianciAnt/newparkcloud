package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.dao_Pfans.PFANS2000.Vo.GivingVo;
import com.nt.service_pfans.PFANS2000.*;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/giving")
public class Pfans2005Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private GivingService givingService;

    @Autowired
    private AppreciationService appreciationService;

    @Autowired
    private OtherTwoService othertwoService;

    @Autowired
    private OtherFourService otherfourService;
    @Autowired
    private AdditionalService additionalService;

    @Autowired
    private OtherFiveService otherfiveService;

    @Autowired
    private WagesService wagesService;


    @RequestMapping(value = "/creategiving", method = {RequestMethod.GET})
    public ApiResult creategiving(String generation, HttpServletRequest request) throws Exception {
        if (generation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        givingService.insert(generation, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/givinglist", method = {RequestMethod.GET})
    public ApiResult get(String giving_id,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(givingService.List(giving_id));
    }


    @RequestMapping(value = "/deleteFive", method = {RequestMethod.POST})
    public ApiResult deleteFive(@RequestBody OtherFive otherfive, HttpServletRequest request) throws Exception {
        if (otherfive == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        otherfiveService.deleteFive(otherfive, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getDataList", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Giving giving=new Giving();
        giving.setActual("0");//預計工作
        return ApiResult.success(givingService.getDataList(giving));
    }

    @RequestMapping(value = "/deleteothertwo", method = {RequestMethod.POST})
    public ApiResult deleteothertwo(@RequestBody OtherTwo othertwo, HttpServletRequest request) throws Exception {
        if (othertwo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        othertwoService.deleteteothertwo(othertwo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/deleteotherfour", method = {RequestMethod.POST})
    public ApiResult deleteotherfour(@RequestBody OtherFour otherFour, HttpServletRequest request) throws Exception {
        if (otherFour == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        otherfourService.deleteotherfour(otherFour, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/deleteadditional", method = {RequestMethod.POST})
    public ApiResult deleteadditional(@RequestBody Additional additional, HttpServletRequest request) throws Exception {
        if (additional == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        additionalService.deleteadditional(additional, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/deleteteappreciation", method = {RequestMethod.POST})
    public ApiResult deleteteappreciation(@RequestBody Appreciation appreciation, HttpServletRequest request) throws Exception {
        if (appreciation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        appreciationService.deleteteappreciation(appreciation, tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/importUserothertwo",method={RequestMethod.POST})
    public ApiResult importUserothertwo( String givingid,HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(othertwoService.importUserothertwo(givingid,request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/importUserotherfive",method={RequestMethod.POST})
    public ApiResult importUserotherfive( String givingid,HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(otherfiveService.importUserotherfive(givingid,request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/importUserappreciation",method={RequestMethod.POST})
    public ApiResult importUserappreciation( String givingid,HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(appreciationService.importUserappreciation(givingid,request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
    //其他4
    @RequestMapping(value = "/importUserotherfour",method={RequestMethod.POST})
    public ApiResult importUserotherfour( String givingid,HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(otherfourService.importUserotherfour(givingid,request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/importUseradditional",method={RequestMethod.POST})
    public ApiResult importUseradditional( String givingid,HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(additionalService.importUseradditional(givingid,request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
    /**
     * 保存
     */
    @RequestMapping(value = "save", method = { RequestMethod.POST })
    public ApiResult insert(@RequestBody GivingVo givingvo, HttpServletRequest request) throws Exception{
        if (givingvo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03,RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        givingService.save(givingvo,tokenModel);
        return ApiResult.success();
    }

    /**
     * 保存
     */
    @RequestMapping(value = "selectWage", method = { RequestMethod.GET })
    public ApiResult selectWage(HttpServletRequest request) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        wagesService.select(tokenModel);
        return ApiResult.success(wagesService.select(tokenModel));
    }

    /**
     * @return com.nt.utils.ApiResult
     * @Method thisMonthOvertimeChange
     * @Author SKAIXX
     * @Description 本月加班数据变更时，重新计算加班费合计
     * @Date 2020/3/18 19:50
     * @Param [givingVo, request]
     **/
    @RequestMapping(value = "/thisMonthOvertimeChange", method = {RequestMethod.POST})
    public ApiResult thisMonthOvertimeChange(@RequestBody GivingVo givingVo, HttpServletRequest request) throws Exception {
        if (givingVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(givingService.thisMonthOvertimeChange(givingVo));
    }

    /**
     * @return com.nt.utils.ApiResult
     * @Method thisMonthOvertimeChange
     * @Author SKAIXX
     * @Description 本月欠勤数据变更时，重新计算欠勤费合计
     * @Date 2020/3/19 10:05
     * @Param [givingVo, request]
     **/
    @RequestMapping(value = "/thisMonthLacktimeChange", method = {RequestMethod.POST})
    public ApiResult thisMonthLacktimeChange(@RequestBody GivingVo givingVo, HttpServletRequest request) throws Exception {
        if (givingVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(givingService.thisMonthLacktimeChange(givingVo));
    }

    @RequestMapping(value = "/updatestate", method = {RequestMethod.GET})
    public ApiResult updatestate(String givingid,String generationdate,HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        givingService.updatestate(givingid,generationdate,tokenModel);
        return ApiResult.success();
    }
}
