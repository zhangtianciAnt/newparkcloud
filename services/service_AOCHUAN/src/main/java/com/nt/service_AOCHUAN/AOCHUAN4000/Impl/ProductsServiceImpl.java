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
}
