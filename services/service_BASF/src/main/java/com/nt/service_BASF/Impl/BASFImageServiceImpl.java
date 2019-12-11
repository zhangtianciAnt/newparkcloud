package com.nt.service_BASF.Impl;
/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: BASFImageServicesImpl
 * @Author: LXY
 * @Description: 首页超链接接口实现类
 * @Date: 2019/11/22
 * @Version: 1.0
 */
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

    //insert
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
