package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.*;
import com.nt.service_pfans.PFANS2000.DutyfreeService;
import com.nt.service_pfans.PFANS2000.GivingService;
import com.nt.service_pfans.PFANS2000.ContrastService;
import com.nt.service_pfans.PFANS2000.OtherTwoService;
import com.nt.service_pfans.PFANS2000.OtherFiveService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.nt.service_pfans.PFANS2000.AppreciationService;
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

    /**
     * 生成基数表
     * FJL
     * */
//    @RequestMapping(value = "insertBase", method = {RequestMethod.GET})
//    public ApiResult insertBase(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        givingService.insertBase(tokenModel);
//        return ApiResult.success();
//    }

    /**
     * 获取基数表列表
     * FJL
     * */
    @RequestMapping(value = "/getListBase", method = {RequestMethod.GET})
    public ApiResult getListtBase(HttpServletRequest request) throws Exception {
        return ApiResult.success(givingService.getListtBase(null));
    }

    @RequestMapping(value = "/listFive", method = {RequestMethod.GET})
    public ApiResult listFive(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        OtherFive otherfive = new OtherFive();
        otherfive.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(otherfiveService.listFive(otherfive));
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
        giving.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(givingService.getDataList(giving));
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        OtherTwo othertwo = new OtherTwo();
        othertwo.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(othertwoService.list(othertwo));
    }

    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody  OtherTwo othertwo, HttpServletRequest request) throws Exception {
        if (othertwo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        othertwoService.insert(othertwo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody OtherTwo othertwo, HttpServletRequest request) throws Exception {
        if (othertwo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        othertwoService.deletete(othertwo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "insertContrast", method = {RequestMethod.GET})
    public ApiResult insert(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        contrastService.insert(tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getListContrast", method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        Contrast contrast =new Contrast();
//        contrast.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(contrastService.getList(null));
    }

    @RequestMapping(value = "/getListdutyfree", method = {RequestMethod.GET})
    public ApiResult getListdutyfree(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(dutyfreeService.getdutyfree(tokenModel));
    }

    @RequestMapping(value = "/importUser",method={RequestMethod.POST})
    public ApiResult importUser(HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(othertwoService.importUser(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/importUser1",method={RequestMethod.POST})
    public ApiResult importUser1(HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(otherfiveService.importUser(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/importUser2",method={RequestMethod.POST})
    public ApiResult importUser2(HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(appreciationService.importUser(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }
}
