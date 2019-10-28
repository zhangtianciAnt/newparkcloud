package com.nt.service_pfans.PFANS3000.mapper;

import com.nt.dao_Pfans.PFANS3000.HotelReservation;
import com.nt.utils.MyMapper;

import java.util.List;

public interface HotelReservationMapper extends MyMapper<HotelReservation> {
    List<HotelReservation> getHotelReservation();
}