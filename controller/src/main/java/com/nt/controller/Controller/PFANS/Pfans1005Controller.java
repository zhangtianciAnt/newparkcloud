package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Vo.PurchaseApplyVo;
import com.nt.dao_Pfans.PFANS1000.PurchaseApply;
import com.nt.service_pfans.PFANS1000.PurchaseApplyService;
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
@RequestMapping("/purchaseApply")
public class Pfans1005Controller {

    @Autowired
    private PurchaseApplyService purchaseApplyService;
    @Autowired
    private TokenService tokenService;

    /**
     * 列表查看
     */
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        PurchaseApply purchaseApply =new PurchaseApply();
        purchaseApply.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(purchaseApplyService.get(purchaseApply));
    }

    /**
     * 新建
     */
    @RequestMapping(value = "insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody PurchaseApplyVo purchaseApplyVo, HttpServletRequest request) throws Exception {
        if (purchaseApplyVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        purchaseApplyService.insert(purchaseApplyVo, tokenModel);
        return ApiResult.success();
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String purchaseApplyid, HttpServletRequest request) throws Exception {
        if (purchaseApplyid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(purchaseApplyService.selectById(purchaseApplyid));
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody PurchaseApplyVo purchaseApplyVo, HttpServletRequest request) throws Exception {
        if (purchaseApplyVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        purchaseApplyService.update(purchaseApplyVo, tokenModel);
        return ApiResult.success();

    }
}
