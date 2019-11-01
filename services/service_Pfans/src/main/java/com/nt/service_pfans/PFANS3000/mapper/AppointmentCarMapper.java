package com.nt.service_pfans.PFANS3000.mapper;

import com.nt.dao_Pfans.PFANS3000.AppointmentCar;
import com.nt.utils.MyMapper;
import java.util.List;

public interface AppointmentCarMapper extends MyMapper<AppointmentCar> {
    List<AppointmentCar> getAppointmentCar();
}