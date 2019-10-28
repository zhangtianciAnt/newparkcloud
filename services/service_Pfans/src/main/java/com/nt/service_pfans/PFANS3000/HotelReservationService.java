package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.HotelReservation;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface HotelReservationService {
    //查看
    List<HotelReservation> getHotelReservation(HotelReservation hotelreservation) throws Exception;

    public HotelReservation One(String hotelreservationid) throws Exception;
    //增加
    public void insertHotelReservation(HotelReservation hotelreservation, TokenModel tokenModel)throws Exception;
    //修改
    public void updateHotelReservation(HotelReservation hotelreservation, TokenModel tokenModel)throws Exception;
}