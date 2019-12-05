package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Confidential;
import com.nt.service_pfans.PFANS1000.ConfidentialService;
import com.nt.service_pfans.PFANS1000.mapper.ConfidentialMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class ConfidentialServiceImpl implements ConfidentialService {

    @Autowired
    private ConfidentialMapper confidentialMapper;

    @Override
    public List<Confidential> getConfidential(Confidential confidential ) {
        return confidentialMapper.select(confidential);
    }

    @Override
    public Confidential One(String confidentialid) throws Exception {

        return confidentialMapper.selectByPrimaryKey(confidentialid);
    }

    @Override
    public void updateConfidential(Confidential confidential, TokenModel tokenModel) throws Exception {
        confidentialMapper.updateByPrimaryKeySelective(confidential);
    }

    @Override
    public void insert(Confidential confidential, TokenModel tokenModel) throws Exception {

        confidential.preInsert(tokenModel);
        confidential.setConfidentialid(UUID.randomUUID().toString());
        confidentialMapper.insert(confidential);
    }

    @Override
    public List<Confidential> getConfidentialList(Confidential confidential, HttpServletRequest request) throws Exception {

        return confidentialMapper.select(confidential) ;
    }
}
