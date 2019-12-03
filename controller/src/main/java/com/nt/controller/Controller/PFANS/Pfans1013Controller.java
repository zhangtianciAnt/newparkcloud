package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS1000.Evection;
import com.nt.dao_Pfans.PFANS1000.Vo.EvectionVo;
import com.nt.service_pfans.PFANS1000.BusinessService;
import com.nt.service_pfans.PFANS1000.EvectionService;
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

@RestController
@RequestMapping("/evection")
public class Pfans1013Controller {

    @Autowired
    private EvectionService evectionService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private BusinessService businessService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Evection evection = new Evection();
        evection.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(evectionService.get(evection));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String evectionid, HttpServletRequest request) throws Exception {
        if (evectionid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(evectionService.selectById(evectionid));
    }

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody EvectionVo evectionVo, HttpServletRequest request) throws Exception {
        if (evectionVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        evectionService.insertEvectionVo(evectionVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody EvectionVo evectionVo, HttpServletRequest request) throws Exception {
        if (evectionVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        evectionService.updateEvectionVo(evectionVo, tokenModel);
        return ApiResult.success();
    }

    /*
     * 出差申请ID号
     * */

    @RequestMapping(value="/getBusiness" ,method = {RequestMethod.POST})
    public ApiResult getBusiness(@RequestBody Business business, HttpServletRequest request) throws Exception{
        if(business==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel=tokenService.getToken(request);
        return ApiResult.success(businessService.get(business));
    }

   /*
   * 借款金额

   @RequestMapping(value="/getmoney" ,method = {RequestMethod.POST})
   public ApiResult getBusiness(@RequestBody Business business, HttpServletRequest request) throws Exception{
       if(business==null){
           return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
       }
       TokenModel tokenModel=tokenService.getToken(request);
       return ApiResult.success(businessService.get(business));
   }

    */


}
