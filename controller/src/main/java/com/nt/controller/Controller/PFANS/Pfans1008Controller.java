package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo;
import com.nt.service_pfans.PFANS1000.SoftwaretransferService;
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
@RequestMapping("/softwaretransfer")
public class Pfans1008Controller {

    @Autowired
    private SoftwaretransferService softwaretransferService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Softwaretransfer softwaretransfer = new Softwaretransfer();
        softwaretransfer.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(softwaretransferService.getSoftwaretransfer(softwaretransfer));
    }

    @RequestMapping(value = "insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody SoftwaretransferVo softwaretransferVo, HttpServletRequest request) throws Exception {
        if (softwaretransferVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        softwaretransferService.insert(softwaretransferVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String softwaretransferid, HttpServletRequest request) throws Exception {
        if (softwaretransferid == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(softwaretransferService.selectById(softwaretransferid));
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody SoftwaretransferVo softwaretransferVo, HttpServletRequest request) throws Exception {
        if (softwaretransferVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        softwaretransferService.updateSoftwaretransfer(softwaretransferVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/downLoad1", method = {RequestMethod.POST})
    public void downLoad1(@RequestBody List<String> softwaretransferList, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        for (int i = 0; i < softwaretransferList.size(); i++) {
            SoftwaretransferVo soft = softwaretransferService.selectById(softwaretransferList.get(i));
            Map<String, Object> data = new HashMap<>();
            data.put("soft", soft);
            data.put("notn", soft.getNotification());
            ExcelOutPutUtil.OutPutPdf("固定资产·软件移转申请", "gudingzichanfq.xls", data, response);
        }
    }
}
