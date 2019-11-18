package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.HotelReservation;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface HotelReservationService {

    List<HotelReservation> getHotelReservation(HotelReservation hotelreservation) throws Exception;

    public HotelReservation One(String hotelreservationid) throws Exception;

    public void insertHotelReservation(HotelReservation hotelreservation, TokenModel tokenModel)throws Exception;

    public void updateHotelReservation(HotelReservation hotelreservation, TokenModel tokenModel)throws Exception;
}