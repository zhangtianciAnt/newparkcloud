package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.service_AOCHUAN.AOCHUAN5000.FinPurchaseSerivce;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/finpurchase")
public class AOCHUAN5002Controller {

    @Autowired
    private FinPurchaseSerivce finPurchaseSerivce;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取费用表数据
     */
    @RequestMapping(value = "/getFinPurchaseList", method = {RequestMethod.POST})
    public ApiResult getFinPurchaseList(HttpServletRequest request) throws Exception {

        FinPurchase finPurchase = new FinPurchase();
        return ApiResult.success(finPurchaseSerivce.getFinPurchaseList(finPurchase));
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/paymentConfirm", method = {RequestMethod.POST})
    public ApiResult paymentConfirm(@RequestBody FinPurchase finPurchase, HttpServletRequest request) throws Exception {

        if (finPurchase == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //UPDATE:FIN_SALES
        //存在Check
        if (finPurchaseSerivce.existCheck(finPurchase)) {
            //唯一性Check
            if(! finPurchaseSerivce.uniqueCheck(finPurchase)) {
                finPurchaseSerivce.update(finPurchase, tokenService.getToken(request));
            }else{
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        //正常结束
        return ApiResult.success();
    }
}
