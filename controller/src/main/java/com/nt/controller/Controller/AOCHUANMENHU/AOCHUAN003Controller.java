package com.nt.controller.Controller.AOCHUANMENHU;

import com.nt.dao_AOCHUAN.AOCHUANMENHU.Menhuproducts;
import com.nt.dao_AOCHUAN.AOCHUANMENHU.Newsinformation;
import com.nt.service_AOCHUAN.AOCHUANMENHU.MenhunewsService;
import com.nt.service_AOCHUAN.AOCHUANMENHU.MenhuproductsService;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/newslist")

public class AOCHUAN003Controller {



    @Autowired
    private MenhunewsService menhunewsService;

    @RequestMapping(value="/getList",method = {RequestMethod.GET})
    public ApiResult getList(HttpServletRequest request)throws  Exception{

        Newsinformation newsinformation = new Newsinformation();

        return ApiResult.success(menhunewsService.get(newsinformation));


    }





}
