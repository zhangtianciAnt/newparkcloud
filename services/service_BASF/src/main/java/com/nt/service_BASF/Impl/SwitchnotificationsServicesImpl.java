package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Application;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.Switchnotifications;
import com.nt.service_BASF.DeviceInformationServices;
import com.nt.service_BASF.MapBox_MapLevelServices;
import com.nt.service_BASF.SwitchnotificationsServices;
import com.nt.service_BASF.mapper.ApplicationMapper;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
import com.nt.service_BASF.mapper.SwitchnotificationsMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class SwitchnotificationsServicesImpl implements SwitchnotificationsServices {

    private static Logger log = LoggerFactory.getLogger(SwitchnotificationsServicesImpl.class);

    @Autowired
    private SwitchnotificationsMapper switchnotificationsMapper;


    @Override
    public List<Switchnotifications> list(Switchnotifications switchnotifications) throws Exception {
//
        List<Switchnotifications> switchnotificationsList = switchnotificationsMapper.select(switchnotifications);
        return switchnotificationsList;
    }

    @Override
    public void create(Switchnotifications switchnotifications) throws Exception {
        switchnotifications.setSwitchnotificationsid(UUID.randomUUID().toString());
        switchnotifications.setCreateon(new Date());
        switchnotifications.setStatus("0");
        switchnotificationsMapper.insert(switchnotifications);
    }


    @Override
    public void delete(Switchnotifications switchnotifications) throws Exception {
        switchnotificationsMapper.delete(switchnotifications);
    }


}
