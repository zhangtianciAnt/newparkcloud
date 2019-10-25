package com.nt.service_pfans.PFANS3000.Impl;
import com.nt.dao_Pfans.PFANS3000.HotelReservation;
import com.nt.service_pfans.PFANS3000.HotelReservationService;
import com.nt.service_pfans.PFANS3000.mapper.HotelReservationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class HotelReservationServiceImpl implements HotelReservationService {

    @Autowired
    private HotelReservationMapper hotelreservationMapper;

    @Override
    public List<HotelReservation> getHotelReservation(HotelReservation hotelreservation) throws Exception {
        return hotelreservationMapper.select(hotelreservation);
    }

    @Override
    public HotelReservation One(String hotelreservationid) throws Exception {

        return hotelreservationMapper.selectByPrimaryKey(hotelreservationid);
    }

    @Override
    public void insertHotelReservation(HotelReservation hotelreservation, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(hotelreservation)){
            hotelreservation.preInsert(tokenModel);
            hotelreservation.setHotelreservationid(UUID.randomUUID().toString());
            hotelreservationMapper.insert(hotelreservation);
        }
    }


    @Override
    public void updateHotelReservation(HotelReservation hotelreservation, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(hotelreservation)){
            hotelreservation.preUpdate(tokenModel);
            hotelreservationMapper.updateByPrimaryKey(hotelreservation);
        }
    }
}
