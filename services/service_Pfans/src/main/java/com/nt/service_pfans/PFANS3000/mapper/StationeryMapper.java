package com.nt.service_pfans.PFANS3000.mapper;

import com.nt.dao_Pfans.PFANS3000.Stationery;
import com.nt.utils.MyMapper;
import java.util.List;

public interface StationeryMapper extends MyMapper<Stationery> {
    List<Stationery> getStationery();
}