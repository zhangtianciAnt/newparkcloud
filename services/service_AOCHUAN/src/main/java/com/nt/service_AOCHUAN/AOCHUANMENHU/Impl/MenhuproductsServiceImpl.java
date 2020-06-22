package com.nt.service_AOCHUAN.AOCHUANMENHU.Impl;


import com.nt.dao_AOCHUAN.AOCHUANMENHU.Menhuproducts;
import com.nt.service_AOCHUAN.AOCHUANMENHU.MenhuproductsService;
import com.nt.service_AOCHUAN.AOCHUANMENHU.mapper.MenhuproductsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class MenhuproductsServiceImpl implements MenhuproductsService {

    @Autowired
    private MenhuproductsMapper productsMapper;



    @Override
    public List<Menhuproducts> get(Menhuproducts menhuproducts) throws Exception {
        return productsMapper.select(menhuproducts);
    }

    @Override
    public void insert(Menhuproducts menhuproducts, TokenModel tokenModel) throws Exception {
        menhuproducts.preInsert(tokenModel);
        menhuproducts.setMenhuproducts_id(UUID.randomUUID().toString());
        productsMapper.insert(menhuproducts);
    }

    @Override
    public Menhuproducts One(String ids) throws Exception {
        return productsMapper.selectByPrimaryKey(ids);
    }

    @Override
    public void delete(String id) throws Exception {
        productsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Menhuproducts menhuproducts, TokenModel tokenModel) throws Exception {
        menhuproducts.preUpdate(tokenModel);
        productsMapper.updateByPrimaryKey(menhuproducts);
    }


}
