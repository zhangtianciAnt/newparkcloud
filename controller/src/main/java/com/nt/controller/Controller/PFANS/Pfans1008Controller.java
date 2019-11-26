package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo;
import com.nt.service_pfans.PFANS1000.SoftwaretransferService;
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
@RequestMapping("/softwaretransfer")
public class Pfans1008Controller {

    @Autowired
    private SoftwaretransferService softwaretransferService;
    @Autowired
    private TokenService tokenService;

    /**
     * 列表查看
     */
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Softwaretransfer softwaretransfer =new Softwaretransfer();
        softwaretransfer.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(softwaretransferService.getSoftwaretransfer(softwaretransfer));
    }

    /**
     * 新建
     */
    @RequestMapping(value = "insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody SoftwaretransferVo softwaretransferVo, HttpServletRequest request) throws Exception {
        if (softwaretransferVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        softwaretransferService.insert(softwaretransferVo, tokenModel);
        return ApiResult.success();
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String softwaretransferid, HttpServletRequest request) throws Exception {
        if (softwaretransferid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(softwaretransferService.selectById(softwaretransferid));
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody SoftwaretransferVo softwaretransferVo, HttpServletRequest request) throws Exception {
        if (softwaretransferVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        softwaretransferService.updateSoftwaretransfer(softwaretransferVo, tokenModel);
        return ApiResult.success();

    }
}
