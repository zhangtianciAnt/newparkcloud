package com.nt.service_pfans.PFANS2000.generateDk;

import cn.hutool.core.date.DateUtil;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.DkDao;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.generateFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class generateThree implements generateFactory {

    @Override
    public DkDao generate(DkDao dkDao) throws Exception {

        //12:00
        Date dateZS = new Date();
        dateZS.setHours(12);
        dateZS.setMinutes(0);
        dateZS.setSeconds(0);

        //12:00 - 出
        dkDao.setLength(DateUtil.toIntSecond (dateZS) - DateUtil.toIntSecond (dkDao.getOut()));
        return dkDao;
    }
}
