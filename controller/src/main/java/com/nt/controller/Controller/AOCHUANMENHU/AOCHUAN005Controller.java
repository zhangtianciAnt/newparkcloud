package com.nt.controller.Controller.AOCHUANMENHU;

import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.dao_AOCHUAN.AOCHUANMENHU.Menhuproducts;
import com.nt.service_AOCHUAN.AOCHUANMENHU.MenhuproductsService;
import com.nt.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/productslist")

public class AOCHUAN005Controller {



    @Autowired
    private MenhuproductsService productsService;

    @RequestMapping(value="/getList",method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request)throws  Exception{

        Menhuproducts menhuproducts = new Menhuproducts();

        return ApiResult.success(productsService.get(menhuproducts));

    }





}
