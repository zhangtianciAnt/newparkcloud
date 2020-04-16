package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Returngoods;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.service_AOCHUAN.AOCHUAN3000.ReturngoodsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ReturngoodsMapper;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.service_AOCHUAN.AOCHUAN4000.mapper.ProductsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class ReturngoodsServiceImpl implements ReturngoodsService {

    @Autowired
    private ReturngoodsMapper returngoodsMapper;


    @Override
    public List<Returngoods> get(Returngoods returngoods) throws Exception {
        return returngoodsMapper.select(returngoods);
    }

    @Override
    public void insert(Returngoods returngoods, TokenModel tokenModel) throws Exception {
        returngoods.preInsert(tokenModel);
        returngoods.setReturngoods_id(UUID.randomUUID().toString());
        returngoodsMapper.insert(returngoods);
    }

    @Override
    public Returngoods One(String ids) throws Exception {
        return returngoodsMapper.selectByPrimaryKey(ids);
    }

    @Override
    public void update(Returngoods returngoods, TokenModel tokenModel) throws Exception {
        returngoods.preUpdate(tokenModel);
        returngoodsMapper.updateByPrimaryKey(returngoods);

    }

    @Override
    public void delete(String id) throws Exception {
        Returngoods returngoods = new Returngoods();
        returngoods.setReturngoods_id(id);
        returngoods.setStatus("1");
        returngoodsMapper.updateByPrimaryKey(returngoods);

    }
}
