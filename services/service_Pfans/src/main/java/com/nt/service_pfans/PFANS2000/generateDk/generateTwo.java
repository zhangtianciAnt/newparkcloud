package com.nt.service_pfans.PFANS2000.generateDk;

import cn.hutool.core.date.DateUtil;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.DkDao;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.generateFactory;
import org.springframework.stereotype.Service;

@Service
public class generateTwo implements generateFactory {

    @Override
    public DkDao generate(DkDao dkDao) throws Exception {
        //入 - 出
        dkDao.setLength(DateUtil.toIntSecond (dkDao.getIn()) - DateUtil.toIntSecond (dkDao.getOut()));
        return dkDao;
    }
}
