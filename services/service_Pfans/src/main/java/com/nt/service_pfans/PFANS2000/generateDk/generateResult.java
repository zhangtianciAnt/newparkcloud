package com.nt.service_pfans.PFANS2000.generateDk;

import cn.hutool.core.date.DateUtil;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.DkDao;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.generateFactory;

import java.util.Calendar;
import java.util.Date;

public class generateResult {

    public DkDao generate(DkDao dkDao) throws Exception{
        if(dkDao.getStart() == null || dkDao.getEnd() == null){
            dkDao.setLength(0D);
        }else{
            Date date = new Date();
            date.setHours(8);
            date.setMinutes(0);
            date.setSeconds(0);
            //进，出均小于&等于8 不处理
            if(DateUtil.toIntSecond (dkDao.getStart()) <= DateUtil.toIntSecond (date) &&
                    DateUtil.toIntSecond (dkDao.getEnd()) <= DateUtil.toIntSecond (date)){
                dkDao.setLength(0D);
            }

            //出小予等于 8 ， 进大于等于8
            if(DateUtil.toIntSecond (dkDao.getStart()) <= DateUtil.toIntSecond (date) &&
                    DateUtil.toIntSecond (dkDao.getEnd()) >= DateUtil.toIntSecond (date)){
                generateFactory generatefactory = new generateOne();
                generatefactory.generate(dkDao);
            }
        }

        return dkDao;
    }
}
