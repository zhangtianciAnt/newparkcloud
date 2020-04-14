package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.PricesetGroup;
import com.nt.dao_Pfans.PFANS6000.Variousfunds;
import com.nt.dao_Pfans.PFANS6000.Vo.PricesetVo;
import com.nt.service_pfans.PFANS6000.PricesetService;
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
import java.util.List;

@RestController
@RequestMapping("/priceset")
public class Pfans6005Controller {

    @Autowired
    private PricesetService pricesetService;

    @Autowired
    private TokenService tokenService;


    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult getpriceset(@RequestBody PricesetGroup pricesetGroup, HttpServletRequest request) throws Exception {
        if (pricesetGroup == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        pricesetGroup.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(pricesetService.gettlist(pricesetGroup));
    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    public ApiResult getPricesetList(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Priceset priceset = new Priceset();
        priceset.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(pricesetService.getPricesetList(priceset));
    }


    /**
     * 单价设定修改
     *
     * @param
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult updatepriceset(@RequestBody PricesetVo pricesetVo, HttpServletRequest request) throws Exception {
        if (pricesetVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        pricesetService.updatepriceset(pricesetVo,tokenModel);
        return ApiResult.success();
    }
}
