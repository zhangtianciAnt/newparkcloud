package com.nt.service_pfans.PFANS3000.Impl;
import com.nt.dao_Pfans.PFANS3000.AppointmentCar;
import com.nt.service_pfans.PFANS3000.AppointmentCarService;
import com.nt.service_pfans.PFANS3000.mapper.AppointmentCarMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;


@Service
@Transactional(rollbackFor=Exception.class)
public class AppointmentCarServiceImpl implements AppointmentCarService {

    @Autowired
    private AppointmentCarMapper appointmentcarMapper;

    @Override
    public List<AppointmentCar> getAppointmentCar(AppointmentCar appointmentcar) throws Exception {
        return appointmentcarMapper.select(appointmentcar);
    }

    @Override
    public List<AppointmentCar> getAppointmentCarlist(AppointmentCar appointmentcar) throws Exception {
        return appointmentcarMapper.select(appointmentcar);
    }

    @Override
    public AppointmentCar One(String appointmentcarid) throws Exception {

        return appointmentcarMapper.selectByPrimaryKey(appointmentcarid);
    }

    @Override
    public void insertAppointmentCar(AppointmentCar appointmentcar, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(appointmentcar)){
            appointmentcar.preInsert(tokenModel);
            appointmentcar.setAppointmentcarid(UUID.randomUUID().toString());
            appointmentcarMapper.insert(appointmentcar);
        }
    }

    @Override
    public void updateAppointmentCar(AppointmentCar appointmentcar, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(appointmentcar)){
            appointmentcar.preUpdate(tokenModel);
            appointmentcarMapper.updateByPrimaryKey(appointmentcar);
        }
    }
}
