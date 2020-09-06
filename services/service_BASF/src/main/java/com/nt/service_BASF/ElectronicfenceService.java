package com.nt.service_BASF;

import com.nt.dao_BASF.Electronicfencealarm;

import java.util.List;

/**
 * 电子围栏相关 Service
 */
public interface ElectronicfenceService {
    // 电子围栏报警单 增删改查 start
    List<Electronicfencealarm> getElectronicfences(Electronicfencealarm electronicfencealarm) throws Exception;

    int createElectronicfences(Electronicfencealarm electronicfencealarm) throws Exception;

    int updateElectronicfences(Electronicfencealarm electronicfencealarm) throws Exception;

    int delElectronicfences(Electronicfencealarm electronicfencealarm) throws Exception;
    // 电子围栏报警单 增删改查 end
}
