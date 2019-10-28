package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS3000.HotelReservation;
import com.nt.service_pfans.PFANS3000.HotelReservationService;
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
@RequestMapping("/hotelreservation")
public class Pfans3002Controller {
    //查找
    @Autowired
    private HotelReservationService hotelreservationService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value="/get",method = {RequestMethod.GET})
    public ApiResult getHotelReservation(HttpServletRequest request) throws Exception{
        TokenModel tokenModel = tokenService.getToken(request);
        HotelReservation hotelreservation = new HotelReservation();
        hotelreservation.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(hotelreservationService.getHotelReservation(hotelreservation));
    }






    @RequestMapping(value ="/one",method = { RequestMethod.POST} )
    public ApiResult one(@RequestBody HotelReservation hotelreservation,HttpServletRequest request) throws Exception{
        if (hotelreservation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        return ApiResult.success(hotelreservationService.One(hotelreservation.getHotelreservationid()));
    }

    @RequestMapping(value="/create",method = {RequestMethod.POST})
    public ApiResult create(@RequestBody HotelReservation hotelreservation, HttpServletRequest request) throws Exception{
        if (hotelreservation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        hotelreservationService.insertHotelReservation(hotelreservation,tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value="/update",method = {RequestMethod.POST})
    public ApiResult updateHotelReservation(@RequestBody HotelReservation hotelreservation, HttpServletRequest request) throws Exception{
        if (hotelreservation == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        hotelreservationService.updateHotelReservation(hotelreservation,tokenModel);
        return ApiResult.success();
    }

}

