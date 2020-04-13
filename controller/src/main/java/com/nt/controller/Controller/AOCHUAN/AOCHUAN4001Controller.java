package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.dao_Assets.Assets;
import com.nt.dao_Pfans.PFANS1000.Fixedassets;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.service_Assets.AssetsService;
import com.nt.service_pfans.PFANS1000.FixedassetsService;
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
@RequestMapping("/products")

public class AOCHUAN4001Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProductsService productsService;

    @RequestMapping(value="/getList",method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request)throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        Products products = new Products();
//        fixedassets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(productsService.get(products));

    }




}
