package com.nt.service_pfans.PFANS3000.Impl;
import com.nt.dao_Pfans.PFANS3000.Stationery;
import com.nt.service_pfans.PFANS3000.StationeryService;
import com.nt.service_pfans.PFANS3000.mapper.StationeryMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class StationeryServiceImpl implements StationeryService {

    @Autowired
    private StationeryMapper stationeryMapper;

    @Override
    public List<Stationery> getStationery(Stationery stationery) throws Exception {
        return stationeryMapper.select(stationery);
    }

    @Override
    public List<Stationery> getStationerylist(Stationery stationery) throws Exception {
        return stationeryMapper.select(stationery);
    }

    @Override
    public Stationery One(String stationeryid) throws Exception {

        return stationeryMapper.selectByPrimaryKey(stationeryid);
    }

    @Override
    public void insertStationery(Stationery stationery, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(stationery)){
            stationery.preInsert(tokenModel);
            stationery.setStationeryid(UUID.randomUUID().toString());
            stationeryMapper.insert(stationery);
        }
    }


    @Override
    public void updateStationery(Stationery stationery, TokenModel tokenModel) throws Exception {
        if(!StringUtils.isEmpty(stationery)){
            stationery.preUpdate(tokenModel);
            stationeryMapper.updateByPrimaryKey(stationery);
        }
    }
}
