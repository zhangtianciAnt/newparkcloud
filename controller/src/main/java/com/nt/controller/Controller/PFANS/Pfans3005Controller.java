package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.dao_Pfans.PFANS3000.Vo.PurchaseVo;
import com.nt.dao_Pfans.PFANS6000.CoststatisticsVo;
import com.nt.service_pfans.PFANS3000.PurchaseService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/purchase")
public class Pfans3005Controller {

    @Autowired
    private PurchaseService  purchaseService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getPurchase(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Purchase purchase = new Purchase();
        purchase.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(purchaseService.getPurchase(purchase));
    }

    @RequestMapping(value="/getlist",method = {RequestMethod.GET})
    public ApiResult getPurchaselist(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Purchase purchase = new Purchase();
        return ApiResult.success(purchaseService.getPurchaselist(purchase));
    }

    @RequestMapping(value = "/one",method={RequestMethod.POST})
    public ApiResult one(@RequestBody Purchase purchase, HttpServletRequest request) throws Exception {
        if (purchase == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(purchaseService.One(purchase.getPurchase_id()));
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updatePurchase(@RequestBody Purchase purchase, HttpServletRequest request) throws Exception{
        if (purchase == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        purchaseService.updatePurchase(purchase,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/create",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Purchase purchase, HttpServletRequest request) throws Exception {
        if (purchase == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        purchaseService.insert(purchase,tokenModel);
        return ApiResult.success();
    }
    /**
     * 导出Excel
     *
     */
    @RequestMapping(value = "/downLoad1", method = {RequestMethod.POST})
    public void downLoad1(@RequestBody PurchaseVo purchaseVo, HttpServletRequest request, HttpServletResponse response) throws Exception {

        SimpleDateFormat sf1ymd = new SimpleDateFormat("yyyy/MM/dd");
        List<Purchase> list = purchaseVo.getPurchase();
        Map<String, Object> data = new HashMap<>();
        data.put("cgList", list);
        ExcelOutPutUtil.OutPutPdf("领取验收单", "lingquyanshoudan.xlsx", data, response);
        ExcelOutPutUtil.deleteDir("E:\\PFANS\\image");
    }
}
