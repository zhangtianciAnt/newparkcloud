package com.nt.service_pfans.PFANS2000.generateDk;

import cn.hutool.core.date.DateUtil;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.DkDao;
import com.nt.service_pfans.PFANS2000.generateDk.generateBase.generateFactory;
import org.springframework.stereotype.Service;

@Service
public class generateFive implements generateFactory {

    @Override
    public DkDao generate(DkDao dkDao) throws Exception {

        return dkDao;
    }
}
