package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.BASFImage;
import com.nt.service_BASF.BASFImageService;
import com.nt.service_BASF.mapper.BASFImageMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BASFImageServiceImpl implements BASFImageService {

    @Autowired
    private BASFImageMapper imageMapper;

    @Override
    public List<BASFImage> get(BASFImage image) throws Exception {
        return imageMapper.select(image);
    }

    @Override
    public void insert(TokenModel tokenModel, BASFImage image) throws Exception {
        image.preInsert(tokenModel);
        image.setImageid(UUID.randomUUID().toString());
        imageMapper.insert(image);
    }

    @Override
    public void update(TokenModel tokenModel, BASFImage image) throws Exception {
        image.preUpdate(tokenModel);
        imageMapper.updateByPrimaryKey(image);
    }

    @Override
    public void del(TokenModel tokenModel, BASFImage image) throws Exception {
        image.preUpdate(tokenModel);
        imageMapper.updateByPrimaryKeySelective(image);
    }
}
