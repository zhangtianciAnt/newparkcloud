package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Vo.AssetinformationVo;
import com.nt.dao_Pfans.PFANS1000.Assetinformation;
import com.nt.service_pfans.PFANS1000.AssetinformationService;
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
@RequestMapping("/assetinformation")
public class Pfans1007Controller {
    //查找信息发布
    @Autowired
    private AssetinformationService assetinformationService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception{

            TokenModel tokenModel = tokenService.getToken(request);
            Assetinformation assetinformation = new Assetinformation();
            assetinformation.setOwners(tokenModel.getOwnerList());
            return ApiResult.success(assetinformationService.getAssetinformation(assetinformation));
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String assetinformationid, HttpServletRequest request) throws Exception {
        if (assetinformationid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(assetinformationService.selectById(assetinformationid));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateAssetinformation(@RequestBody AssetinformationVo assetinformationVo, HttpServletRequest request) throws Exception{
        if (assetinformationVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetinformationService.updateAssetinformation(assetinformationVo,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody AssetinformationVo assetinformationVo, HttpServletRequest request) throws Exception {
        if (assetinformationVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetinformationService.insert(assetinformationVo,tokenModel);
        return ApiResult.success();
    }
}
