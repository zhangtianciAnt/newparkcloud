package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Fireaccidentrecord;
import com.nt.service_BASF.FireaccidentrecordServices;
import com.nt.service_BASF.mapper.FireaccidentrecordMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: BASF10203ServicesImpl
 * @Author: SUN
 * @Description: BASF消防事故记录模块实现类
 * @Date: 2019/11/4 16:30
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FireaccidentrecordServicesImpl implements FireaccidentrecordServices {

    private static Logger log = LoggerFactory.getLogger(FireaccidentrecordServicesImpl.class);

    @Autowired
    private FireaccidentrecordMapper fireaccidentrecordMapper;

    /**
     * @param fireaccidentrecord
     * @Method list
     * @Author SUN
     * @Version 1.0
     * @Description
     * @Return java.util.List<Fireaccidentrecord>
     * @Date 2019/11/4
     */
    @Override
    public List<Fireaccidentrecord> list(Fireaccidentrecord fireaccidentrecord) throws Exception {
        return fireaccidentrecordMapper.select(fireaccidentrecord);
    }

    @Override
    public Fireaccidentrecord selectById(String fireaccidentrecordid) throws Exception {
        return fireaccidentrecordMapper.selectByPrimaryKey(fireaccidentrecordid);
    }


    @Override
    public void insert(TokenModel tokenModel, Fireaccidentrecord fireaccidentrecord) throws Exception {
        fireaccidentrecord.preInsert(tokenModel);
        String id = UUID.randomUUID().toString();
        fireaccidentrecord.setFireaccidentrecordid(id);

        Calendar now = Calendar.getInstance();
        String YY = now.get(Calendar.YEAR)+"";
        String MM = (now.get(Calendar.MONTH)+1)+"";
        String DD = now.get(Calendar.DAY_OF_MONTH)+"";
        String no = YY+MM+DD;




        fireaccidentrecord.setFireaccidentno(no);


        fireaccidentrecordMapper.insert(fireaccidentrecord);
    }


    @Override
    public void update(TokenModel tokenModel, Fireaccidentrecord fireaccidentrecord) throws Exception {
        fireaccidentrecord.preUpdate(tokenModel);
        fireaccidentrecordMapper.updateByPrimaryKeySelective(fireaccidentrecord);
    }
}
