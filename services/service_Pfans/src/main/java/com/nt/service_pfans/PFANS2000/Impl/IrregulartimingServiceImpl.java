package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Irregulartiming;
import com.nt.service_pfans.PFANS2000.IrregulartimingService;
import com.nt.service_pfans.PFANS2000.mapper.IrregulartimingMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class IrregulartimingServiceImpl implements IrregulartimingService {

    @Autowired
    private IrregulartimingMapper irregulartimingMapper;


    @Override
    public List<Irregulartiming> getAllIrregulartiming(Irregulartiming irregulartiming) throws Exception {
        return irregulartimingMapper.select(irregulartiming);
    }

    @Override
    public Irregulartiming getIrregulartimingOne(String irregulartiming_id) throws Exception {
        if (irregulartiming_id.equals("")) {
            return null;
        }
        return irregulartimingMapper.selectByPrimaryKey(irregulartiming_id);

    }

    @Override
    public void insertIrregulartiming(Irregulartiming irregulartiming, TokenModel tokenModel) throws Exception {
        if (!irregulartiming.equals(null)) {
            irregulartiming.preInsert(tokenModel);
            irregulartiming.setIrregulartiming_id(UUID.randomUUID().toString());
            irregulartimingMapper.insertSelective(irregulartiming);
        }
    }


    @Override
    public void updateIrregulartiming(Irregulartiming irregulartiming, TokenModel tokenModel) throws Exception {
        irregulartimingMapper.updateByPrimaryKeySelective(irregulartiming);
    }

}
