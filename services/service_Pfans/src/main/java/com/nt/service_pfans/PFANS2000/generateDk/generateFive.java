package com.nt.service_pfans.PFANS2000.generateDk;

import cn.hutool.core.date.DateUtil;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.DkDao;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.generateFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class generateFive implements generateFactory {

    @Override
    public DkDao generate(DkDao dkDao) throws Exception {
        //18:00
        Date dateE = new Date();
        dateE.setHours(18);
        dateE.setMinutes(0);
        dateE.setSeconds(0);

        //18:00 - 出
        dkDao.setLength(DateUtil.toIntSecond (dateE) - DateUtil.toIntSecond (dkDao.getOut()));
        return dkDao;
    }
}
