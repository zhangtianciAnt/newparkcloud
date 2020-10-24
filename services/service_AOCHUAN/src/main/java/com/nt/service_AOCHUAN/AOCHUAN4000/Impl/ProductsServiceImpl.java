package com.nt.service_AOCHUAN.AOCHUAN4000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierproductrelation;
import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Sample;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Marketproducts;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.SupplierbaseinforMapper;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.SupplierproductrelationMapper;
import com.nt.service_AOCHUAN.AOCHUAN2000.mapper.CustomerbaseinforMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.QuotationsMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.SampleMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.TransportGoodMapper;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.service_AOCHUAN.AOCHUAN4000.mapper.MarketproductsMapper;
import com.nt.service_AOCHUAN.AOCHUAN4000.mapper.ProductsMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.*;

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

    @Autowired
    private MarketproductsMapper marketproductsMapper;

    @Autowired
    private K3CloundConfig k3CloundConfig;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public List<Products> get(Products products) throws Exception {
        return productsMapper.select(products);
    }

    @Override
    public void insert(Products products, TokenModel tokenModel) throws Exception {
        products.preInsert(tokenModel);
        Products pro = new Products();
        pro.setChinaname(products.getChinaname());

        List<Products> list =productsMapper.select(pro);
        if(list.size()>0){
            throw new LogicalException("产品中文名已经注册");
        }
        else {
            products.setProducts_id(UUID.randomUUID().toString());
            //add_fjl_1021  添加编码
            int number = 100001;
            int seleocount = productsMapper.allselectCount();
            String  num = String.valueOf(number + seleocount);
            num = num.substring(1,6);
            products.setPronumber("CH"+num);
            //add_fjl_1021  添加编码
            productsMapper.insert(products);
            List<Marketproducts> marketproductsList = products.getScTable();
            for(Marketproducts m : marketproductsList){
                m.preInsert(tokenModel);
                m.setMarketproducts_id(UUID.randomUUID().toString());
                m.setProducts_id(products.getProducts_id());
                marketproductsMapper.insert(m);
            }
        }


    }

    @Override
    public Products One(String ids) throws Exception {

        Products products  = productsMapper.selectByPrimaryKey(ids);
        if(products != null){
            Marketproducts m = new Marketproducts();
            m.setProducts_id(ids);
            List<Marketproducts> list = marketproductsMapper.select(m);
            products.setScTable(list);
        }
        return products;
    }

    @Override
    public void update(Products products, TokenModel tokenModel) throws Exception {
        products.preUpdate(tokenModel);
        Marketproducts marketproducts = new Marketproducts();
        productsMapper.updateByPrimaryKey(products);

        List<Marketproducts> list = products.getScTable();
        marketproducts.setProducts_id(products.getProducts_id());
        marketproductsMapper.delete(marketproducts);

        for(Marketproducts m : list){
            m.setMarketproducts_id(UUID.randomUUID().toString());
            m.setProducts_id(products.getProducts_id());
            marketproductsMapper.insert(m);
        }

    }

    @Override
    public void delete(String id) throws Exception {
        Products products = new Products();
        products.setProducts_id(id);
        products.setStatus("1");
        productsMapper.updateByPrimaryKeySelective(products);
//        productsMapper.deleteByPrimaryKey(id);
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
    public void insertForSupplier(String baseinfoId, Products[] products, TokenModel tokenModel) throws Exception {
        for(int i = 0 ;i < products.length;i++){
            String id = UUID.randomUUID().toString();
            products[i].preInsert(tokenModel);
            products[i].setProducts_id(id);
            productsMapper.insert(products[i]);

            Supplierproductrelation supplierproductrelation = new Supplierproductrelation();
            String supplierproductrelation_id = UUID.randomUUID().toString();
            supplierproductrelation.setProducts_id(id);
            supplierproductrelation.setSupplierproductrelation_id(supplierproductrelation_id);
            supplierproductrelation.setSupplierbaseinfor_id(baseinfoId);
            supplierproductrelationMapper.insert(supplierproductrelation);
        }
    }

    /*@Override
    public Products insertForSupplier(Products product, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        product.preInsert(tokenModel);
        product.setProducts_id(id);
        productsMapper.insert(product);
        return product;
    }*/

    /**
     * 获取不在项目表中的数据
     * @return
     * @throws Exception
     */
    @Override
    public List<Products> getProdutsExceptUnique() throws Exception {
        return productsMapper.getProdutsExceptUnique();
    }
    /**
     * 获取不在注册表中的数据
     * @return
     * @throws Exception
     */
    @Override
    public List<Products> getProdutsExceptUniqueInReg() throws Exception {
        return productsMapper.getProdutsExceptUniqueInReg();
    }

    @Override
    public void pushKingdee(List<Products> list, TokenModel tokenModel,Boolean flg) throws Exception {
        String loginParam = BaseUtil.buildLogin("5f4f0eaa667840", "Administrator", "888888", 2052);
        ResultVo login = login(k3CloundConfig.url + k3CloundConfig.login, loginParam);
        if (login.getCode() != ResultEnum.SUCCESS.getCode()) {
//            log.error("【登录金蝶系统失败】：{}", login.getMsg());
            throw new LogicalException("登录金蝶系统失败");
//            return;
        }
        Map<String, Object> map = (Map<String, Object>) login.getData();
        String cookie = map.get("cookie").toString();

        //获取供应商数据模板
        File file = null;
        file = ResourceUtils.getFile("classpath:excel/products.json");
        String jsonData = BaseUtil.jsonRead(file);
        List<Map<String,Object>> listmap = new ArrayList();
        JSONObject basic = null;
        //推送data
        if(list.size() > 0){
            for(Products su :list){
                int kis = 0;
                if(flg){ //true: 系统服务
                    if(StringUtils.isNotEmpty(su.getKisid())){
                        kis = Integer.valueOf(su.getKisid());
                    }
                } else { //false: 手动
                    Products products = productsMapper.selectByPrimaryKey(su.getProducts_id());
                    if(products != null){
                        if(StringUtils.isNotEmpty(products.getKisid())){
                            kis = Integer.valueOf(products.getKisid());
                        }
                    }
                }
                basic = JSON.parseObject(jsonData);
                Map<String,Object> model = (Map<String, Object>) basic.get("Model");

                model.put("FMATERIALID",kis);//主键ID
                model.put("FNumber",su.getPronumber());//编码
                model.put("FName",su.getChinaname());//名称
                listmap.add(model);
            }
            basic.put("Model",listmap);

        }

        //构造供应商接口数据
        String producter = BaseUtil.buildMaterial(basic,KDFormIdEnum.MATERIAL.getFormid());

        ResultVo save = batchSave(k3CloundConfig.url + k3CloundConfig.batchSave, cookie, producter,tokenModel,list,flg);
        if (save.getCode() != ResultEnum.SUCCESS.getCode()) {
//            log.error("【保存出错】：{}", save.getMsg());
            throw new LogicalException("保存出错"+save.getMsg());
//            return;
        }

    }



    //add_fjl_1021 推送KIS
    //金蝶login
    public ResultVo login(String url,String content) {

        ResponseEntity<String> responseEntity = HttpUtil.httpPost(url, content);
        //获取登录cookie
        if(responseEntity.getStatusCode()== HttpStatus.OK){
            String login_cookie = "";
            Set<String> keys = responseEntity.getHeaders().keySet();
            for(String key:keys){
                if (key.equalsIgnoreCase("Set-Cookie")) {
                    List<String> cookies = responseEntity.getHeaders().get(key);
                    for(String cookie:cookies){
                        if(cookie.startsWith("kdservice-sessionid")){
                            login_cookie=cookie;
                            break;
                        }
                    }
                }
            }
            Map<String,Object> map = new HashMap<>();
            map.put("cookie",login_cookie);
            return ResultUtil.success(map);
        }

        Map<String,Object> result = JSON.parseObject(responseEntity.getBody());
        return ResultUtil.error(result.get("Message").toString());
    }

    //金蝶批量保存
    public ResultVo batchSave(String url,String cookie, String content,TokenModel tokenModel,List<Products> productsList,Boolean flg) throws Exception {
        //保存
        Map<String, Object> header = new HashMap<>();
        header.put("Cookie", cookie);
        String result = HttpUtil.httpPost(url, header, content);
        JSONObject jsonObject = JSON.parseObject(result);
        Map<String, Object> map = (Map<String, Object>) jsonObject.get("Result");
        Map<String, Object> responseStatus = (Map<String, Object>) map.get("ResponseStatus");
        Boolean isSuccess = (Boolean) responseStatus.get("IsSuccess");

        //获取返回值   kisid
        Object ob = responseStatus.get("SuccessEntitys");
        for(int i = 0; i < ((JSONArray) ob).size(); i++){
            Integer kisid = (Integer)((JSONObject) ((JSONArray) ob).get(i)).get("Id");
            String number = (String)((JSONObject) ((JSONArray) ob).get(i)).get("Number");
            updKisid(kisid,number,tokenModel,productsList.get(i),flg);
        }

        if (isSuccess) {
            return ResultUtil.success();
        } else {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.get("Errors");
            return ResultUtil.error(JSON.toJSONString(errors));
        }
    }

    //系统服务
    @Scheduled(cron = "0 0 0 1 * ?")
    public void pullKis() throws Exception {
        TokenModel tokenModel = new TokenModel();
        List<Products> productsList = productsMapper.allselectData();
        pushKingdee(productsList,tokenModel,true);
    }

    //返回kisid更新回去
    public void updKisid(Integer kisid, String number, TokenModel tokenModel,Products products,Boolean flg) throws Exception {
        if(products != null){
            if(flg){
                products.preUpdate(tokenModel);
                products.setKisid(kisid.toString());
                products.setPronumber(number);
                productsMapper.updateByPrimaryKeySelective(products);
            } else {
                Products pro = productsMapper.selectByPrimaryKey(products.getProducts_id());
                if (pro != null) {
                    pro.preUpdate(tokenModel);
                    pro.setKisid(kisid.toString());
                    pro.setPronumber(number);
                    productsMapper.updateByPrimaryKeySelective(pro);
                }
            }
        }
    }

    @Override
    public void zhengtishuju(){
        //添加加产品编码数据
//        List<Products> list = productsMapper.selectProlist();
//        int a = 100000;
//        for(Products products : list){
//            a ++;
//            String ss = String.valueOf(a).substring(1,String.valueOf(a).length());
////            Products prod = new Products();
////            prod.setProducts_id(products.getProducts_id());
//            products.setPronumber("CH" + ss);
//            productsMapper.updateByPrimaryKeySelective(products);
//        }

        //添加客户编码数据
//        List<Customerbaseinfor> cu = customerbaseinforMapper.selectCustomerlist();
//
//        int b = 10000;
//        for(Customerbaseinfor u : cu){
//            b ++;
//            String ss = String.valueOf(b).substring(1,String.valueOf(b).length());
//            u.setCustnumber("CUST" + ss);
//            customerbaseinforMapper.updateByPrimaryKeySelective(u);
//        }


        //添加供应商编码数据
        List<Supplierbaseinfor> cu = supplierbaseinforMapper.selectSupplierlist();

        int b = 100000;
        for(Supplierbaseinfor u : cu){
            b ++;
            String ss = String.valueOf(b).substring(1,String.valueOf(b).length());
            u.setSupnumber("VEN" + ss);
            supplierbaseinforMapper.updateByPrimaryKeySelective(u);
        }
    }


}
