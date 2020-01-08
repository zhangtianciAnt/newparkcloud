package com.nt.controller.Controller.ASSETS;

import cn.hutool.core.util.StrUtil;
import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.InventoryResults;
import com.nt.dao_Assets.Vo.AssetsVo;
import com.nt.service_Assets.AssetsService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assets")
public class AssetsController {

    @Autowired
    private AssetsService assetsService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/connection", method = {RequestMethod.POST})
    public ApiResult connection(HttpServletRequest request) throws Exception {
        return ApiResult.success();
    }

    @RequestMapping(value = "/scanOne", method = {RequestMethod.POST})
    public ApiResult scanOne(String code, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(code)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(assetsService.scanOne(code, tokenModel).getBarcode());
    }

    @RequestMapping(value = "/scanList", method = {RequestMethod.POST})
    public ApiResult scanList(String code, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(code)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        int rst = assetsService.scanList(code, tokenModel);
        return ApiResult.success(rst);
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Assets assets = new Assets();
        assets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(assetsService.list(assets));
    }

    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult create(@RequestBody Assets assets, HttpServletRequest request) throws Exception {
        if (assets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetsService.insert(assets, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/insertlots", method = {RequestMethod.POST})
    public ApiResult insertlots(@RequestBody AssetsVo assetsVo, HttpServletRequest request) throws Exception {
        if (assetsVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetsService.insertLosts(assetsVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST})
    public ApiResult updateInformation(@RequestBody Assets assets, HttpServletRequest request) throws Exception {
        if (assets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        assetsService.update(assets, tokenModel);
        return ApiResult.success();
    }


    @RequestMapping(value = "/oneInfo", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody Assets assets, HttpServletRequest request) throws Exception {
        if (assets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(assetsService.One(assets.getAssets_id()));
    }

    @RequestMapping(value = "/import",method={RequestMethod.POST})
    public ApiResult importUser(HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(assetsService.importDate(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/download", method = {RequestMethod.POST})
    public void download(HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        ExcelOutPutUtil.OutPut("资产","zichan.xlsx",data,response);
    }
}
