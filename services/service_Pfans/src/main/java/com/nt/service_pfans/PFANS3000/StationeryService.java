package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.Stationery;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface StationeryService {

    List<Stationery> getStationery(Stationery stationery) throws Exception;

    List<Stationery> getStationerylist(Stationery stationery) throws Exception;

    public Stationery One(String stationeryid) throws Exception;

    public void insertStationery(Stationery stationery, TokenModel tokenModel)throws Exception;

    public void updateStationery(Stationery stationery, TokenModel tokenModel)throws Exception;

}