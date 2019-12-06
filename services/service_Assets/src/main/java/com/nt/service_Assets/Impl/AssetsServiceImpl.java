package com.nt.service_Assets.Impl;

import com.nt.dao_Assets.Assets;
import com.nt.service_Assets.AssetsService;
import com.nt.service_Assets.mapper.AssetsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class AssetsServiceImpl implements AssetsService {

    @Autowired
    private AssetsMapper assetsMapper;

    @Override
    public List<Assets> list(Assets assets) throws Exception {
        return assetsMapper.select(assets);
    }

    @Override
    public void insert(Assets assets, TokenModel tokenModel) throws Exception {
        assets.preInsert(tokenModel);
        assets.setAssets_id(UUID.randomUUID().toString());
        assetsMapper.insert(assets);
    }

    @Override
    public void update(Assets assets, TokenModel tokenModel) throws Exception {
        assets.preUpdate(tokenModel);
        assetsMapper.updateByPrimaryKey(assets);
    }

    @Override
    public Assets One(String assetsid) throws Exception {
        return assetsMapper.selectByPrimaryKey(assetsid);
    }
}
