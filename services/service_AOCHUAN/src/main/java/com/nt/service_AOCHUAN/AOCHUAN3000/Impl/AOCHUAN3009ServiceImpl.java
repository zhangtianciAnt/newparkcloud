package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.model.AOCHUAN3009;
import com.nt.service_AOCHUAN.AOCHUAN3000.AOCHUAN3009Service;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.AOCHUAN3009Mapper;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.util.List;

@Service
public class AOCHUAN3009ServiceImpl implements AOCHUAN3009Service {

    private AOCHUAN3009Mapper AoChuan3009Mapper;
    private AOCHUAN3009 AoChuan3009;

    //获取projectscheduleList
    @Override
    public List<AOCHUAN3009> getProjectList() throws Exception {
        return AoChuan3009Mapper.select(AoChuan3009);
    }
}
