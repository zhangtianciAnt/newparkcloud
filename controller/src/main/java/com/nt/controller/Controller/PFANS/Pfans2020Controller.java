package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS2000.Irregulartiming;
import com.nt.service_pfans.PFANS2000.IrregulartimingService;
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
@RequestMapping("/irregulartiming")

public class Pfans2020Controller {
    @Autowired
    private IrregulartimingService irregulartimingService;

    @Autowired
    private TokenService tokenService;

    /**
     *
     * 查看列表
     */
   @RequestMapping(value ="/getAllIrregulartiming",method = { RequestMethod.GET})
    public ApiResult getAllIrregulartiming(HttpServletRequest request) throws Exception{
       TokenModel tokenModel= tokenService.getToken(request);
      return ApiResult.success(  irregulartimingService.getAllIrregulartiming());
   }

    /**
     *
     * 查看一个人
     */
     @RequestMapping(value =" getIrregulartimingOne",method = { RequestMethod.POST} )
     public ApiResult getIrregulartimingOne( String irregulartimingid,HttpServletRequest request) throws Exception{
         //Irregulartiming irregulartiming1=new Irregulartiming();
         TokenModel tokenMode1= tokenService.getToken(request);
         return ApiResult.success(irregulartimingService.getIrregulartimingOne(irregulartimingid));
     }


    /**
     * 新建
     *
     */
   @RequestMapping(value = "/insertIrregulartiming",method = {RequestMethod.POST})
    public ApiResult insertIrregulartiming(@RequestBody Irregulartiming irregulartiming, HttpServletRequest request) throws Exception{
     TokenModel tokenModel = tokenService.getToken(request);
     irregulartimingService.insertIrregulartiming(irregulartiming,tokenModel);
     return ApiResult.success();
   }
    /**
     *修改
     *
     */
   @RequestMapping(value ="/updateIrregulartiming",method={RequestMethod.POST })
    public ApiResult updateIrregulartiming(@RequestBody Irregulartiming irregulartiming, HttpServletRequest request)throws  Exception{
    if(irregulartiming==null){
        return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
    }
       TokenModel tokenModel = tokenService.getToken(request);
     irregulartimingService.updateIrregulartiming(irregulartiming,tokenModel);

       return ApiResult.success();
   }


}
