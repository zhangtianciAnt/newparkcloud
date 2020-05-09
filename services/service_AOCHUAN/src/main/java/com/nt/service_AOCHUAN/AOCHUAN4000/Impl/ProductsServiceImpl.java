package com.nt.service_AOCHUAN.AOCHUAN4000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierproductrelation;
import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Sample;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.SupplierbaseinforMapper;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.SupplierproductrelationMapper;
import com.nt.service_AOCHUAN.AOCHUAN2000.mapper.CustomerbaseinforMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.QuotationsMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.SampleMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.TransportGoodMapper;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.service_AOCHUAN.AOCHUAN4000.mapper.ProductsMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class ProductsServiceImpl implements ProductsService {

    @Autowired
    private ProductsMapper productsMapper;
    @Autowired
    private SupplierproductrelationMapper supplierproductrelationMapper;
    @Autowired
    private SupplierbaseinforMapper supplierbaseinforMapper;
    @Autowired
    private TransportGoodMapper transportGoodMapper;
    @Autowired
    private SampleMapper sampleMapper;
    @Autowired
    private QuotationsMapper quotationsMapper;

    @Autowired
    private CustomerbaseinforMapper customerbaseinforMapper;


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
//获取供应商
    @Override
    public List<Supplierbaseinfor> getGYS(String ids) throws Exception {
        Supplierbaseinfor supplierbaseinfor = new Supplierbaseinfor();
        Supplierproductrelation supplierproductrelation = new Supplierproductrelation();
        supplierproductrelation.setProducts_id(ids);
        List<Supplierproductrelation> list = supplierproductrelationMapper.select(supplierproductrelation);
        List<Supplierbaseinfor> supplierbaseinforList = new ArrayList<>();
        for(Supplierproductrelation supplierproductrelation1 : list){
            supplierbaseinfor = supplierbaseinforMapper.selectByPrimaryKey(supplierproductrelation1.getSupplierbaseinfor_id());
            supplierbaseinforList.add(supplierbaseinfor);
        }

        return supplierbaseinforList;
    }
//获取客户信息
    @Override
    public List<Customerbaseinfor> getKH(String ids) throws Exception {
        Customerbaseinfor customerbaseinfor = new Customerbaseinfor();
        Supplierproductrelation supplierproductrelation = new Supplierproductrelation();
        supplierproductrelation.setProducts_id(ids);
        List<Supplierproductrelation> list = supplierproductrelationMapper.select(supplierproductrelation);
        List<Customerbaseinfor> list1 = new ArrayList();
        for(Supplierproductrelation supp : list){
            customerbaseinfor = customerbaseinforMapper.selectByPrimaryKey(supp.getSupplierbaseinfor_id());
            list1.add(customerbaseinfor);
        }
        return list1;
    }

    @Override
    public List<TransportGood> getZH(String ids) throws Exception {
        TransportGood transportGood = new TransportGood();
        transportGood.setProductsid(ids);
        return transportGoodMapper.select(transportGood);
    }

    @Override
    public List<Sample> getYP(String ids) throws Exception {
        Sample sample = new Sample();
        sample.setProducts_id(ids);
        return sampleMapper.select(sample);
    }

    @Override
    public List<Quotations> getBJ(String ids) throws Exception {
        Quotations quotations = new Quotations();
        quotations.setProductsid(ids);
        return quotationsMapper.select(quotations);
    }

    @Override
    public Products insertForSupplier(Products product, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        product.preInsert(tokenModel);
        product.setProducts_id(id);
        productsMapper.insert(product);
        return product;
    }

    /**
     * 获取不在项目表中的数据
     * @return
     * @throws Exception
     */
    @Override
    public List<Products> getProdutsExceptUnique() throws Exception {
        return productsMapper.getProdutsExceptUnique();
    }
}
