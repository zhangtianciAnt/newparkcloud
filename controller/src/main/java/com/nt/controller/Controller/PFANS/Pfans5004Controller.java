package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS5000.CloseApplicat;
import com.nt.dao_Pfans.PFANS5000.ProjectSecore;
import com.nt.dao_Pfans.PFANS5000.Vo.CloseApplicatVo;
import com.nt.service_pfans.PFANS5000.CloseApplicatService;
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
@RequestMapping("/closeapplicat")
public class Pfans5004Controller {
    @Autowired
    private CloseApplicatService closeApplicatService;

    @Autowired
    private TokenService tokenService;

  @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{
      TokenModel tokenModel=tokenService.getToken(request);
      CloseApplicat closeApplicat=new CloseApplicat();
      closeApplicat.setOwners(tokenModel.getOwnerList());
      return ApiResult.success(closeApplicatService.get(closeApplicat));
  }
    @RequestMapping(value = "/selectById",method = {RequestMethod.GET})
    public ApiResult selectById(String closeapplicatid, HttpServletRequest request) throws Exception {
        if (closeapplicatid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenMode1 = tokenService.getToken(request);
        return ApiResult.success(closeApplicatService.selectById(closeapplicatid));
    }
    @RequestMapping(value = "/insert",method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody CloseApplicatVo closeApplicatVo, HttpServletRequest request) throws Exception {
        if (closeApplicatVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        closeApplicatService.insert(closeApplicatVo,tokenModel);
        return ApiResult.success();
    }
    @RequestMapping(value = "/update",method = {RequestMethod.POST})
    public ApiResult update(@RequestBody CloseApplicatVo closeApplicatVo, HttpServletRequest request) throws Exception {
        if (closeApplicatVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        closeApplicatService.update(closeApplicatVo,tokenModel);
        return ApiResult.success();
    }

}
