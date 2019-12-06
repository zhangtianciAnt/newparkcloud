package com.nt.controller.Controller.ASSETS;

import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.Vo.InventoryplanVo;
import com.nt.service_Assets.InventoryplanService;
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
    public ApiResult insert(@RequestBody InventoryplanVo inventoryplanVo, HttpServletRequest request) throws Exception {
        if (inventoryplanVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        inventoryplanService.insert(inventoryplanVo, tokenModel);
        return ApiResult.success();
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/selectAll", method = {RequestMethod.GET})
    public ApiResult selectAll(HttpServletRequest request) throws Exception {
//        TokenModel tokenModel = tokenService.getToken(request);
//        Assets assets = new Assets();
//        assets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(inventoryplanService.selectAll(null));
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody InventoryplanVo inventoryplanVo, HttpServletRequest request) throws Exception {
        if (inventoryplanVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        inventoryplanService.update(inventoryplanVo, tokenModel);
        return ApiResult.success();

    }
}
