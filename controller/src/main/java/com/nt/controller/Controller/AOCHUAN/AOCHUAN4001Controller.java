package com.nt.controller.Controller.AOCHUAN;

import cn.hutool.core.date.DateUtil;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.service_AOCHUAN.AOCHUAN4000.mapper.ProductsMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")

public class AOCHUAN4001Controller {
    @Autowired
    private ProductsMapper productsMapper;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProductsService productsService;
    // add-ws-10/13-禅道任务429
    @RequestMapping(value = "/getDataList1", method = {RequestMethod.POST})
    public ApiResult getDataList1(@RequestBody Products products, HttpServletRequest request) throws Exception {
        if (products == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        SimpleDateFormat sf = new SimpleDateFormat("YYYYMM");
        List<Products> list = productsMapper.selectlist(sf.format(products.getCreateon()));
        return ApiResult.success(list);
    }
    // add-ws-10/13-禅道任务429
    @RequestMapping(value="/getList",method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Products products = new Products();
        //products.setOwners(tokenModel.getOwnerList());
//        fixedassets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(productsService.get(products));

    }

    @RequestMapping(value = "/insert",method={RequestMethod.POST})
    public ApiResult create(@RequestBody Products products, HttpServletRequest request) throws Exception {
        if (products == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        productsService.insert(products,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getone",method={RequestMethod.GET})
    public ApiResult one(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(productsService.One(id));
    }

    @RequestMapping(value = "/update",method={RequestMethod.POST})
    public ApiResult update(@RequestBody Products products, HttpServletRequest request) throws Exception {
        if(products == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        productsService.update(products,tokenService.getToken(request));
        return ApiResult.success();
    }

    @RequestMapping(value = "/delete",method={RequestMethod.GET})
    public ApiResult delete(@RequestParam String id, HttpServletRequest request) throws Exception {
        if(!StringUtils.isNotBlank(id)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        productsService.delete(id);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getGYS",method={RequestMethod.GET})
    public ApiResult getGYS(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(productsService.getGYS(id));
    }

    @RequestMapping(value = "/getKH",method={RequestMethod.GET})
    public ApiResult getKH(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(productsService.getKH(id));
    }

    @RequestMapping(value = "/getZH",method={RequestMethod.GET})
    public ApiResult getZH(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(productsService.getZH(id));
    }

    @RequestMapping(value = "/getYP",method={RequestMethod.GET})
    public ApiResult getYP(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(productsService.getYP(id));
    }

    @RequestMapping(value = "/getBJ",method={RequestMethod.GET})
    public ApiResult getBJ(@RequestParam String id, HttpServletRequest request) throws Exception {
        if (id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(productsService.getBJ(id));
    }


    @RequestMapping(value = "/pushKingdee", method = {RequestMethod.POST})
    public ApiResult pushKingdee(@RequestBody List<Products> list, HttpServletRequest request) throws Exception {
        if (list == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        productsService.pushKingdee(list,tokenService.getToken(request));

        return ApiResult.success();
    }




}