package com.nt.service_AOCHUAN.AOCHUAN4000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.utils.MyMapper;

import java.util.List;


public interface ProductsMapper extends MyMapper<Products> {

    //获取不在项目表中的数据
    public List<Products>  getProdutsExceptUnique();
    //获取不在注册表中的数据
    public List<Products>  getProdutsExceptUniqueInReg();
}
