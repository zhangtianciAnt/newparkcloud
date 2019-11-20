package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Assetinformation;
import com.nt.service_pfans.PFANS1000.AssetinformationService;
import com.nt.service_pfans.PFANS1000.mapper.AssetinformationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class AssetinformationServiceImpl implements AssetinformationService {

    @Autowired
    private AssetinformationMapper assetinformationMapper;

    @Override
    public List<Assetinformation> getAssetinformation(Assetinformation assetinformation ) {

        return assetinformationMapper.select(assetinformation);
    }

    @Override
    public Assetinformation One(String assetinformationid) throws Exception {

        return assetinformationMapper.selectByPrimaryKey(assetinformationid);
    }

    @Override
    public void updateAssetinformation(Assetinformation assetinformation, TokenModel tokenModel) throws Exception {
        assetinformationMapper.updateByPrimaryKeySelective(assetinformation);
    }

    @Override
    public void insert(Assetinformation assetinformation, TokenModel tokenModel) throws Exception {

        assetinformation.preInsert(tokenModel);
        assetinformation.setAssetinformationid(UUID.randomUUID().toString());
        assetinformationMapper.insert(assetinformation);
    }

    @Override
    public List<Assetinformation> getAssetinformationList(Assetinformation assetinformation, HttpServletRequest request) throws Exception {

        return assetinformationMapper.select(assetinformation) ;
    }
}
