package com.nt.service_AOCHUAN.AOCHUAN4000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
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
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    private ProductsMapper productsMapper;


    @Override
    public List<Products> get(Products products) throws Exception {
        return productsMapper.select(products);
    }

    @Override
    public void insert(Products products, TokenModel tokenModel) throws Exception {
        products.preInsert(tokenModel);
        products.setProducts_id(UUID.randomUUID().toString());
        productsMapper.insert(products);
    }

    @Override
    public Products One(String ids) throws Exception {
        return productsMapper.selectByPrimaryKey(ids);
    }

    @Override
    public void update(Products products, TokenModel tokenModel) throws Exception {
        products.preUpdate(tokenModel);
        productsMapper.updateByPrimaryKey(products);

    }

    @Override
    public void delete(String id) throws Exception {
        Products products = new Products();
        products.setProducts_id(id);
        products.setStatus("1");
        productsMapper.updateByPrimaryKey(products);
        productsMapper.deleteByPrimaryKey(id);
    }
}
