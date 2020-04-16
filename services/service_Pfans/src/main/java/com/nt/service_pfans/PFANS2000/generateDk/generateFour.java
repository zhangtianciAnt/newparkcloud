package com.nt.service_pfans.PFANS2000.generateDk;

import cn.hutool.core.date.DateUtil;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.DkDao;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.generateFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class generateFour implements generateFactory {

    @Override
    public DkDao generate(DkDao dkDao) throws Exception {

        //13:00
        Date dateZE = new Date();
        dateZE.setHours(13);
        dateZE.setMinutes(0);
        dateZE.setSeconds(0);

        //进 - 13：00
        dkDao.setLength(DateUtil.toIntSecond (dkDao.getIn()) - DateUtil.toIntSecond (dateZE));
        return dkDao;
    }
}
