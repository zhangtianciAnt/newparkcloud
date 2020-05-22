package com.nt.service_AOCHUAN.AOCHUANMENHU.Impl;


import com.nt.dao_AOCHUAN.AOCHUANMENHU.Menhuproducts;
import com.nt.service_AOCHUAN.AOCHUANMENHU.MenhuproductsService;
import com.nt.service_AOCHUAN.AOCHUANMENHU.mapper.MenhuproductsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class MenhuproductsServiceImpl implements MenhuproductsService {

    @Autowired
    private MenhuproductsMapper productsMapper;



    @Override
    public List<Menhuproducts> get(Menhuproducts menhuproducts) throws Exception {
        return productsMapper.select(menhuproducts);
    }


}
