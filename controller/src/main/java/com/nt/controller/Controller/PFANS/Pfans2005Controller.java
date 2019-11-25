package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.service_pfans.PFANS2000.DutyfreeService;
import com.nt.service_pfans.PFANS2000.GivingService;
import com.nt.service_pfans.PFANS2000.ContrastService;
import com.nt.service_pfans.PFANS2000.OtherTwoService;
import com.nt.service_pfans.PFANS2000.OtherFourService;
import com.nt.service_pfans.PFANS2000.OtherFiveService;
import com.nt.service_pfans.PFANS2000.AppreciationService;
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
    private OtherFiveService otherfiveService;

    @Autowired
    private ContrastService contrastService;

    @Autowired
    private DutyfreeService dutyfreeService;

    @RequestMapping(value = "/creategiving", method = {RequestMethod.GET})
    public ApiResult creategiving(String generation, HttpServletRequest request) throws Exception {
        if (generation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        givingService.insert(generation, tokenModel);
        return ApiResult.success();
    }

//    /**
//     * 获取基数表列表
//     * FJL
//     * */
//    @RequestMapping(value = "/getListBase", method = {RequestMethod.GET})
//    public ApiResult getListtBase(HttpServletRequest request) throws Exception {
//        return ApiResult.success(givingService.getListtBase(null));
//    }


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


    @RequestMapping(value = "/deleteteappreciation", method = {RequestMethod.POST})
    public ApiResult deleteteappreciation(@RequestBody Appreciation appreciation, HttpServletRequest request) throws Exception {
        if (appreciation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        appreciationService.deleteteappreciation(appreciation, tokenModel);
        return ApiResult.success();
    }


//    @RequestMapping(value = "/getListContrast", method = {RequestMethod.GET})
//    public ApiResult getList(HttpServletRequest request) throws Exception {
////        TokenModel tokenModel = tokenService.getToken(request);
////        Contrast contrast =new Contrast();
////        contrast.setOwners(tokenModel.getOwnerList());
//        return ApiResult.success(contrastService.getList(null));
//    }

    @RequestMapping(value = "/getListdutyfree", method = {RequestMethod.GET})
    public ApiResult getListdutyfree(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(dutyfreeService.getdutyfree(tokenModel));
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
}
