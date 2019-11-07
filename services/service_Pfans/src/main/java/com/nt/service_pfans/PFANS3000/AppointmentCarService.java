package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.AppointmentCar;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AppointmentCarService {

    //查看
    List<AppointmentCar> getAppointmentCar(AppointmentCar appointmentcar) throws Exception;

    List<AppointmentCar> getAppointmentCarlist(AppointmentCar appointmentcar) throws Exception;

    public AppointmentCar One(String appointmentcarid) throws Exception;
    //增加
    public void insertAppointmentCar(AppointmentCar appointmentcar, TokenModel tokenModel)throws Exception;
    //修改
    public void updateAppointmentCar(AppointmentCar appointmentcar, TokenModel tokenModel)throws Exception;
}
