package com.nt.service_pfans.PFANS6000.Impl;


import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationVo;
import com.nt.service_pfans.PFANS6000.DeleginformationService;
import com.nt.service_pfans.PFANS6000.mapper.DelegainformationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class DelegainformationServiceImpl implements DeleginformationService {


    @Autowired
    private DelegainformationMapper delegainformationMapper;

    @Override
    public List<DelegainformationVo> getDelegainformation() throws Exception {
        return delegainformationMapper.getinfo();
    }

    @Override
    public List<DelegainformationVo> getYears(String year) throws Exception {
        return delegainformationMapper.getYears(year);
    }

    @Override
    public void updateDeleginformation(List<Delegainformation> delegainformationList, TokenModel tokenModel) throws Exception {
        for (Delegainformation delegainformation : delegainformationList) {
            delegainformation.preInsert(tokenModel);
            delegainformationMapper.updateByPrimaryKey(delegainformation);
        }
    }
}
