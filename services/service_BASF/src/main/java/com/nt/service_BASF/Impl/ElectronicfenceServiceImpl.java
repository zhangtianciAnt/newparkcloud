package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Electronicfencealarm;
import com.nt.service_BASF.ElectronicfenceService;
import com.nt.service_BASF.mapper.ElectronicfencealarmMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ElectronicfenceServiceImpl implements ElectronicfenceService {

    @Resource
    private ElectronicfencealarmMapper electronicfencealarmMapper;

    @Override
    public List<Electronicfencealarm> getElectronicfences(Electronicfencealarm electronicfencealarm) throws Exception {
        List<Electronicfencealarm> electronicfencealarms = electronicfencealarmMapper.getElectronicfences(electronicfencealarm);
        return electronicfencealarms;
    }

//    @Override
//    public int createElectronicfences(Electronicfencealarm electronicfencealarm) throws Exception {
//        electronicfencealarm.setId(UUID.randomUUID().toString());
//        electronicfencealarm.setCreateon(new Date());
//        electronicfencealarm.setStatus(0);
//        int i = electronicfencealarmMapper.insert(electronicfencealarm);
//        return i;
//    }

    @Override
    public int updateElectronicfences(Electronicfencealarm electronicfencealarm) throws Exception {
        electronicfencealarm.setModifyon(new Date());
        int i = electronicfencealarmMapper.updateByPrimaryKeySelective(electronicfencealarm);
        return i;
    }

    @Override
    public int delElectronicfences(Electronicfencealarm electronicfencealarm) throws Exception {
        electronicfencealarm.setStatus(1);
        int i = electronicfencealarmMapper.updateByPrimaryKeySelective(electronicfencealarm);
        return i;
    }
}
