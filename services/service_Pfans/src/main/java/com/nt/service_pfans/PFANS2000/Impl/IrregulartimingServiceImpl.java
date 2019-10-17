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

    /**
     *查询列表
     *
     */
    @Override
    public List<Irregulartiming> getAllIrregulartiming() throws Exception {
       
        if(irregulartimingMapper.getAllIrregulartiming().isEmpty()){
            return null;
        }
        return irregulartimingMapper.getAllIrregulartiming();
    }


    /**
     *按id查询
     *
     * @return
     */
    @Override
    public Irregulartiming getIrregulartimingOne(String irregulartimingid) throws Exception {
        if(irregulartimingid.equals(" ")){
            return  null;
        }
       return irregulartimingMapper.selectByPrimaryKey(irregulartimingid);

    }



    /**
     *新建
     */

    @Override
    public void insertIrregulartiming(Irregulartiming irregulartiming, TokenModel tokenModel) throws Exception {
        if (!irregulartiming.equals(null)) {
            irregulartiming.preInsert(tokenModel);
            irregulartiming.setIrregulartiming_id(UUID.randomUUID().toString());
            irregulartimingMapper.insertSelective(irregulartiming);

        }
    }

    /**
     *按id修改
     *
     */
    @Override
    public void updateIrregulartiming(Irregulartiming irregulartiming, TokenModel tokenModel) throws Exception {
        irregulartiming.preUpdate(tokenModel);
        irregulartimingMapper.updateByPrimaryKey(irregulartiming);
    }

}
