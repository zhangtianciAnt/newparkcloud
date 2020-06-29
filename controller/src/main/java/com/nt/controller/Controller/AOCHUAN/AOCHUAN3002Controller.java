package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Applicationrecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Enquiry;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.dao_AOCHUAN.AOCHUAN5000.FinSales;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.TransportGoodService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transportgood")
public class AOCHUAN3002Controller {

    @Autowired
    private TransportGoodService transportGoodService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/get",method={RequestMethod.GET})
    public ApiResult get(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        TransportGood transportGood = new TransportGood();
        transportGood.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(transportGoodService.get(transportGood));
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult getOne(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(transportGoodService.getOne(id));
    }

    @RequestMapping(value = "/getForSupplier",method={RequestMethod.GET})
    public ApiResult getForSupplier(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(transportGoodService.getForSupplier(id));
    }

    @RequestMapping(value = "/getForCustomer",method={RequestMethod.GET})
    public ApiResult getForCustomer(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(transportGoodService.getForCustomer(id));
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody TransportGood transportGood, HttpServletRequest request) throws Exception {
        if(transportGood == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        transportGoodService.update(transportGood,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult insert(@RequestBody TransportGood transportGood, HttpServletRequest request) throws Exception {
        if(transportGood == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        transportGoodService.insert(transportGood,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        transportGoodService.delete(id);
        return ApiResult.success();
    }

//    @RequestMapping(value = "/insertcw",method={RequestMethod.POST})
//    public ApiResult insertcw(@RequestBody TransportGood transportGood, HttpServletRequest request) throws Exception {
//        if (transportGood == null) {
//            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
//        }
//        transportGoodService.insertCW(transportGood, tokenService.getToken(request));
//        return ApiResult.success();
//    }
//
//    @RequestMapping(value = "/inserthk",method={RequestMethod.POST})
//    public ApiResult inserthk(@RequestBody TransportGood transportGood, HttpServletRequest request) throws Exception {
//        if (transportGood == null) {
//            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
//        }
//        transportGoodService.insertHK(transportGood, tokenService.getToken(request));
//        return ApiResult.success();
//    }

    @RequestMapping(value = "/paymentcg",method={RequestMethod.POST})
    public ApiResult paymentCG(@RequestBody List<FinSales> finSales, HttpServletRequest request) throws Exception {
        if (finSales == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        transportGoodService.paymentCG(finSales, tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/paymentxs",method={RequestMethod.POST})
    public ApiResult paymentXS(@RequestBody List<FinPurchase> finPurchases, HttpServletRequest request) throws Exception {
        if (finPurchases == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        transportGoodService.paymentXS(finPurchases, tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/getexport",method={RequestMethod.GET})
    public void getexport(@RequestParam String id, HttpServletResponse response) throws Exception {
        if(!StringUtils.isNotBlank(id)){
//            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

//        transportGoodService.setExport(id , response);

        /* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
     //   response.setContentType(java.net.URLEncoder.encode("aaa.xlsx", "UTF-8"));
        /* 设置文件头：最后一个参数是设置下载文件名 */
        //response.setHeader("Content-Disposition", "attachment;filename=bbab.xlsx");
        response.setHeader("content-Type","application/ms-excel");
        response.setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode("aaa.xlsx", "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        try (
                InputStream ins = new FileInputStream("C:\\Users\\llll\\Desktop\\aaa.xlsx");
                OutputStream os = response.getOutputStream()
        ) {
            byte[] b = new byte[1024];
            int len;
            while ((len = ins.read(b)) > 0) {
                os.write(b, 0, len);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
//        return ApiResult.success();
    }


}
