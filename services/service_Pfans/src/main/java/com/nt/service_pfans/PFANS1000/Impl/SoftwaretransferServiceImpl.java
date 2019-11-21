package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.service_pfans.PFANS1000.SoftwaretransferService;
import com.nt.service_pfans.PFANS1000.mapper.SoftwaretransferMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class SoftwaretransferServiceImpl implements SoftwaretransferService {

    @Autowired
    private SoftwaretransferMapper softwaretransferMapper;

    @Override
    public List<Softwaretransfer> getSoftwaretransfer(Softwaretransfer softwaretransfer ) {

        return softwaretransferMapper.select(softwaretransfer);
    }

    @Override
    public Softwaretransfer One(String softwaretransferid) throws Exception {

        return softwaretransferMapper.selectByPrimaryKey(softwaretransferid);
    }

    @Override
    public void updateSoftwaretransfer(Softwaretransfer softwaretransfer, TokenModel tokenModel) throws Exception {
        softwaretransferMapper.updateByPrimaryKeySelective(softwaretransfer);
    }

    @Override
    public void insert(Softwaretransfer softwaretransfer, TokenModel tokenModel) throws Exception {

        softwaretransfer.preInsert(tokenModel);
        softwaretransfer.setSoftwaretransferid(UUID.randomUUID().toString());
        softwaretransferMapper.insert(softwaretransfer);
    }

    @Override
    public List<Softwaretransfer> getSoftwaretransferList(Softwaretransfer softwaretransfer, HttpServletRequest request) throws Exception {

        return softwaretransferMapper.select(softwaretransfer) ;
    }
}
