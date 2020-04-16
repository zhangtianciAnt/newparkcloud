package com.nt.service_pfans.PFANS2000.generateDk;

import cn.hutool.core.date.DateUtil;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.DkDao;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.generateFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class generateOne implements generateFactory {

    @Override
    public DkDao generate(DkDao dkDao) throws Exception {
        Date date = new Date();
        date.setHours(8);
        date.setMinutes(0);
        date.setSeconds(0);


        //入 - 8：00
        dkDao.setLength(DateUtil.toIntSecond (dkDao.getIn()) - DateUtil.toIntSecond (date));
        return dkDao;
    }
}
