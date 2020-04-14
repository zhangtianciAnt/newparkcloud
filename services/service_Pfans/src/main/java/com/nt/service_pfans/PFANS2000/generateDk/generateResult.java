package com.nt.service_pfans.PFANS2000.generateDk;

import cn.hutool.core.date.DateUtil;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.DkDao;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.generateFactory;

import java.util.Date;

public class generateResult {

    public DkDao generate(DkDao dkDao) throws Exception {
        if (dkDao.getOut() == null || dkDao.getIn() == null) {
            dkDao.setLength(0);
        } else {
            //8:00
            Date dateS = new Date();
            dateS.setHours(8);
            dateS.setMinutes(0);
            dateS.setSeconds(0);

            //12:00
            Date dateZS = new Date();
            dateZS.setHours(12);
            dateZS.setMinutes(0);
            dateZS.setSeconds(0);

            //13:00
            Date dateZE = new Date();
            dateZE.setHours(13);
            dateZE.setMinutes(0);
            dateZE.setSeconds(0);

            //18:00
            Date dateE = new Date();
            dateE.setHours(18);
            dateE.setMinutes(0);
            dateE.setSeconds(0);

            //进，出均小于等于8 不处理  或者
            // 进，出均大于等于 18：00 或者
            // 出 大于等12：00 进 小于等于 13：00
            if ((DateUtil.toIntSecond(dkDao.getOut()) <= DateUtil.toIntSecond(dateS) &&
                    DateUtil.toIntSecond(dkDao.getIn()) <= DateUtil.toIntSecond(dateS))
                    ||
                    (DateUtil.toIntSecond(dkDao.getOut()) >= DateUtil.toIntSecond(dateE) &&
                            DateUtil.toIntSecond(dkDao.getIn()) >= DateUtil.toIntSecond(dateE))
                    ||
                    (DateUtil.toIntSecond(dkDao.getOut()) >= DateUtil.toIntSecond(dateZS) &&
                            DateUtil.toIntSecond(dkDao.getIn()) <= DateUtil.toIntSecond(dateZE))) {
                dkDao.setLength(0);
            }

            //出小予等于 8 ， 进大于等于8 或者
            if (DateUtil.toIntSecond(dkDao.getOut()) <= DateUtil.toIntSecond(dateS) &&
                    DateUtil.toIntSecond(dkDao.getIn()) >= DateUtil.toIntSecond(dateS)) {
                generateFactory generatefactory = new generateOne();
                generatefactory.generate(dkDao);
            }

            //进，出 在8：00~12：00 或 13：00~18：00 之间
            if ((DateUtil.toIntSecond(dkDao.getOut()) >= DateUtil.toIntSecond(dateS) &&
                    DateUtil.toIntSecond(dkDao.getIn()) <= DateUtil.toIntSecond(dateZS))
                    ||
                    (DateUtil.toIntSecond(dkDao.getOut()) >= DateUtil.toIntSecond(dateZE) &&
                            DateUtil.toIntSecond(dkDao.getIn()) <= DateUtil.toIntSecond(dateE))) {
                generateFactory generatefactory = new generateTwo();
                generatefactory.generate(dkDao);
            }

            //出在12：00 前 ，进在12：00 后
            if (DateUtil.toIntSecond(dkDao.getOut()) <= DateUtil.toIntSecond(dateZS) &&
                    DateUtil.toIntSecond(dkDao.getIn()) >= DateUtil.toIntSecond(dateZS)) {
                generateFactory generatefactory = new generateThree();
                generatefactory.generate(dkDao);
            }

            //出在13：00 前 ，进在13：00 后
            if (DateUtil.toIntSecond(dkDao.getOut()) <= DateUtil.toIntSecond(dateZE) &&
                    DateUtil.toIntSecond(dkDao.getIn()) >= DateUtil.toIntSecond(dateZE)) {
                generateFactory generatefactory = new generateFour();
                generatefactory.generate(dkDao);
            }

            //出在18：00 前 ，进在18：00 后
            if (DateUtil.toIntSecond(dkDao.getOut()) <= DateUtil.toIntSecond(dateE) &&
                    DateUtil.toIntSecond(dkDao.getIn()) >= DateUtil.toIntSecond(dateE)) {
                generateFactory generatefactory = new generateFive();
                generatefactory.generate(dkDao);
            }
        }

        return dkDao;
    }
}
