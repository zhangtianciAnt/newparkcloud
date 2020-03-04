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
import org.springframework.web.bind.annotation.*;

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
        return ApiResult.success(assetsService.confirm(code, tokenModel).getBarcode());
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
    public ApiResult list(HttpServletRequest request,@RequestBody Assets assets) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
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

    @RequestMapping(value = "/getDepartment",method={RequestMethod.POST})
    public ApiResult getDepartment(HttpServletRequest request){
        try{
            TokenModel tokenModel = tokenService.getToken(request);
            return ApiResult.success(assetsService.getDepartment(request,tokenModel));
        }catch(LogicalException e){
            return ApiResult.fail(e.getMessage());
        }catch (Exception e) {
            return ApiResult.fail("操作失败！");
        }
    }

    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void download(String type, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        String templateName = null;
        String fileName = null;
        if ( "0".equals(type) ) {
            templateName = "qitazichan.xlsx";
            fileName = "资产";
        } else if ( "1".equals(type) ) {
            templateName = "buwaizichan.xlsx";
            fileName = "簿外";
        } else  if ( "2".equals(type) ) {
            templateName = "gudingzichan.xlsx";
            fileName = "固定";
        }
        if (templateName != null ) {
            ExcelOutPutUtil.OutPut(fileName,templateName,data,response);
        }
    }
}
