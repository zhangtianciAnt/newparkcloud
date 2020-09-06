package com.nt.service_BASF.mapper;


import com.nt.dao_BASF.Electronicfencealarm;
import com.nt.utils.MyMapper;

import java.util.List;

/**
 * 电子围栏报警单 Mapper
 */
public interface ElectronicfencealarmMapper extends MyMapper<Electronicfencealarm> {

    List<Electronicfencealarm> getElectronicfences(Electronicfencealarm electronicfencealarm) throws Exception;
}
