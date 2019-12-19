package com.nt.controller.Controller.ASSETS;

import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.Vo.InventoryRangeVo;
import com.nt.service_Assets.InventoryplanService;
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
@RequestMapping("/inventoryplan")
public class InventoryplanController {

    @Autowired
    private InventoryplanService inventoryplanService;
    @Autowired
    private TokenService tokenService;

    /**
     * 列表查看
     */
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Inventoryplan inventoryplan = new Inventoryplan();
        inventoryplan.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(inventoryplanService.get(inventoryplan));
    }

    /**
     * 新建
     */
    @RequestMapping(value = "insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody InventoryRangeVo inventoryRangeVo, HttpServletRequest request) throws Exception {
        if (inventoryRangeVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        inventoryplanService.insert(inventoryRangeVo, tokenModel);
        return ApiResult.success();
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/selectAll", method = {RequestMethod.GET})
    public ApiResult selectAll(HttpServletRequest request) throws Exception {
        Assets assets = new Assets();
        return ApiResult.success(inventoryplanService.selectAll(assets));
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody InventoryRangeVo inventoryRangeVo, HttpServletRequest request) throws Exception {
        if (inventoryRangeVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        inventoryplanService.update(inventoryRangeVo, tokenModel);
        return ApiResult.success();

    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiResult delete(@RequestBody Inventoryplan inventoryplan, HttpServletRequest request) throws Exception {
        if (inventoryplan == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        inventoryplanService.isDelInventory(inventoryplan);
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String inventoryRangeid, HttpServletRequest request) throws Exception {
        if (inventoryRangeid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(inventoryplanService.selectById(inventoryRangeid));
    }

    @RequestMapping(value = "/selectByResult", method = {RequestMethod.GET})
    public ApiResult selectByResult(String inventoryresultsid, HttpServletRequest request) throws Exception {
        if (inventoryresultsid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(inventoryplanService.selectByResult(inventoryresultsid));
    }

}
