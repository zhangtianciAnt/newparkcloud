package com.nt.service_AOCHUAN.AOCHUAN1000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.IOUtils;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.service_AOCHUAN.AOCHUAN1000.SupplierbaseinforService;
import com.nt.service_AOCHUAN.AOCHUAN1000.mapper.SupplierbaseinforMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;
import java.util.*;

@Service
public class SupplierbaseinforServiceImpl implements SupplierbaseinforService {
    @Autowired
    private SupplierbaseinforMapper supplierbaseinforMapper;

    @Autowired
    private K3CloundConfig k3CloundConfig;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public List<Supplierbaseinfor> get() throws Exception {
        return supplierbaseinforMapper.allSelect();
    }

    @Override
    public Supplierbaseinfor getOne(String id) throws Exception {
        Supplierbaseinfor supplierbaseinfor = supplierbaseinforMapper.selectByPrimaryKey(id);
        return supplierbaseinfor;
    }

    @Override
    public void update(Supplierbaseinfor supplierbaseinfor, TokenModel tokenModel) throws Exception {
        supplierbaseinfor.preUpdate(tokenModel);
        supplierbaseinforMapper.updateByPrimaryKeySelective(supplierbaseinfor);
    }

    @Override
    public String insert(Supplierbaseinfor supplierbaseinfor, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        supplierbaseinfor.setSupplierbaseinfor_id(id);
        //add_fjl_1021  添加编码
//        int number = 100001;
//        int seleocount = supplierbaseinforMapper.allselectCount();
//        String  num = String.valueOf(number + seleocount);
//        num = num.substring(1,6);
//        supplierbaseinfor.setSupnumber("VEN"+num);
        //add_fjl_1021  添加编码
        supplierbaseinfor.preInsert(tokenModel);
        supplierbaseinforMapper.insert(supplierbaseinfor);
        return id;
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
    public ResultVo batchSave(String url,String cookie, String content,TokenModel tokenModel,List<Supplierbaseinfor> supplierbaseinforList,Boolean flg) throws Exception {
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
        for(int i = 0; i < ((JSONArray) ob).size();i++){
            Integer kisid = (Integer)((JSONObject) ((JSONArray) ob).get(i)).get("Id");
            String number = (String)((JSONObject) ((JSONArray) ob).get(i)).get("Number");
            updKisid(kisid,number,tokenModel,supplierbaseinforList.get(i),flg);
        }

        if (isSuccess) {
            return ResultUtil.success();
        } else {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.get("Errors");
            return ResultUtil.error(JSON.toJSONString(errors));
        }
    }

    @Override
    public void login1(List<Supplierbaseinfor> supplierbaseinforList,TokenModel tokenModel,Boolean flg) throws Exception {
        //正式dbid：5f1533095ad35e
        //测试dbid：5f4f0eaa667840
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
//        file = ResourceUtils.getFile("classpath:excel/supplier.json");
        ClassPathResource resource  = new ClassPathResource("excel/supplier.json");
        //判断是否存在
        if (resource.exists()) {
            //获取流
            InputStream inputStream = resource.getInputStream();
            Date now = new Date();
            //创建一个json格式的临时文件
            file = File.createTempFile(now.getTime() + "", ".json");
            try {
                //将流写入到你创建的新文件中
                byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
                FileCopyUtils.copy(bdata, file);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
        String jsonData = BaseUtil.jsonRead(file);
        List<Map<String,Object>> listmap = new ArrayList();
        JSONObject basic = null;
        //推送data
        if(supplierbaseinforList.size() > 0){
            for(Supplierbaseinfor su :supplierbaseinforList){
                int kis = 0;
                if(flg){ //true: 系统服务
                    if(StringUtils.isNotEmpty(su.getKisid())){
                        kis = Integer.valueOf(su.getKisid());
                    }
                } else { //false: 手动
                    Supplierbaseinfor supplierbaseinfor = supplierbaseinforMapper.selectByPrimaryKey(su.getSupplierbaseinfor_id());
                    if(supplierbaseinfor != null){
                        if(StringUtils.isNotEmpty(supplierbaseinfor.getKisid())){
                            kis = Integer.valueOf(supplierbaseinfor.getKisid());
                        }
                    }
                }
                basic = JSON.parseObject(jsonData);
                Map<String,Object> model = (Map<String, Object>) basic.get("Model");
                Map<String,Object> fGroup = (Map<String, Object>) ((Map<String, Object>) basic.get("Model")).get("FGroup");
                model.put("FSupplierId",kis);//主键ID
//                model.put("FCreateOrgId","");//创建组织
//                model.put("FUseOrgId",""); //使用组织
                model.put("FNumber",su.getSupnumber());//编码
                model.put("FName",su.getSuppliernamecn());//名称
                fGroup.put("FNumber","");//供应商分组
                model.put("FGroup",fGroup);//名称
                listmap.add(model);
            }
            basic.put("Model",listmap);

        }

        //构造供应商接口数据
        String supplier = BaseUtil.buildMaterial(basic,KDFormIdEnum.SUPPLIER.getFormid());

        ResultVo save = batchSave(k3CloundConfig.url + k3CloundConfig.batchSave, cookie, supplier,tokenModel,supplierbaseinforList,flg);
        if (save.getCode() != ResultEnum.SUCCESS.getCode()) {
//            log.error("【保存出错】：{}", save.getMsg());
            throw new LogicalException("保存出错"+save.getMsg());
//            return;
        }


    }

    //系统服务
//    @Scheduled(cron = "0 20 0 1 * ?")
    public void pullKis() throws Exception {
        TokenModel tokenModel = new TokenModel();
        List<Supplierbaseinfor> supplierbaseinforList = supplierbaseinforMapper.allselectData();
        login1(supplierbaseinforList,tokenModel,true);
    }

    //返回kisid更新回去
    public void updKisid(Integer kisid, String number, TokenModel tokenModel,Supplierbaseinfor su,Boolean flg) throws Exception {
        if(su != null){
            if(flg){  //true:系统服务
                su.preUpdate(tokenModel);
                su.setKisid(kisid.toString());
                su.setSupnumber(number);
                supplierbaseinforMapper.updateByPrimaryKeySelective(su);
            } else {   //false:手动
                Supplierbaseinfor sup = supplierbaseinforMapper.selectByPrimaryKey(su.getSupplierbaseinfor_id());
                if (sup != null) {
                    sup.preUpdate(tokenModel);
                    sup.setKisid(kisid.toString());
                    sup.setSupnumber(number);
                    supplierbaseinforMapper.updateByPrimaryKeySelective(sup);
                }
            }
        }
    }
    //add_fjl_1021 推送KIS
    @Override
    public void delete(String id) throws Exception {
        Supplierbaseinfor supplierbaseinfor = new Supplierbaseinfor();
        supplierbaseinfor.setSupplierbaseinfor_id(id);
        supplierbaseinfor.setStatus("1");
//        supplierbaseinforMapper.deleteByPrimaryKey(id);
        supplierbaseinforMapper.updateByPrimaryKeySelective(supplierbaseinfor);
    }

    @Override
    public List<Supplierbaseinfor> getSuppliersExceptUnique() throws Exception {
        Supplierbaseinfor supplierbaseinfor = new Supplierbaseinfor();
        return supplierbaseinforMapper.select(supplierbaseinfor);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importInfo() throws Exception {
        /*try {
           *//* List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);*//*
            ExcelReader reader = ExcelUtil.getReader(f);
            List<Map<String, Object>> readAll = reader.readAll();
            int accesscount = 0;
            int error = 0;
            for (Map<String, Object> item : readAll) {
                *//*Query query = new Query();
                query.addCriteria(Criteria.where("userid").is(item.get("社員ID")));
                List<CustomerInfo> customerInfos = mongoTemplate.find(query, CustomerInfo.class);
                *//*
                if (customerInfos.size() > 0) {
                    customerInfos.get(0).getUserinfo().setCenterid(item.get("centerid").toString());
                    customerInfos.get(0).getUserinfo().setGroupid(item.get("groupid").toString());
                    customerInfos.get(0).getUserinfo().setTeamid(item.get("teamid").toString());
                    customerInfos.get(0).getUserinfo().setCentername(item.get("所属センター").toString());
                    customerInfos.get(0).getUserinfo().setGroupname(item.get("所属グループ").toString());
                    customerInfos.get(0).getUserinfo().setTeamname(item.get("所属チーム").toString());
                    customerInfos.get(0).getUserinfo().setBudgetunit(item.get("予算単位").toString());
                    customerInfos.get(0).getUserinfo().setPost(item.get("職務").toString());
                    customerInfos.get(0).getUserinfo().setRank(item.get("ランク").toString());
                    customerInfos.get(0).getUserinfo().setLaborcontractday(item.get("労働契約締切日").toString());
                    customerInfos.get(0).getUserinfo().setAnnuallastyear(item.get("去年年休数(残)").toString());
                    customerInfos.get(0).getUserinfo().setAnnualyear(item.get("今年年休数").toString());
                    customerInfos.get(0).getUserinfo().setUpgraded(item.get("昇格昇号年月日").toString());
                    customerInfos.get(0).getUserinfo().setSeatnumber(item.get("口座番号").toString());
                    List<CustomerInfo.Personal> cupList = new ArrayList<CustomerInfo.Personal>();
                    CustomerInfo.Personal personal = new CustomerInfo.Personal();
                    personal.setAfter(item.get("変更前基本工资").toString());
                    personal.setBefore(item.get("変更前职责工资").toString());
                    personal.setBasic(item.get("変更后基本工资").toString());
                    personal.setDuty(item.get("変更后职责工资").toString());
                    personal.setDate(item.get("給料変更日").toString());
                    cupList.add(personal);
                    customerInfos.get(0).getUserinfo().setGridData(cupList);
                    customerInfos.get(0).getUserinfo().setOldageinsurance(item.get("養老保険基数").toString());
                    customerInfos.get(0).getUserinfo().setMedicalinsurance(item.get("医療保険基数").toString());
                    customerInfos.get(0).getUserinfo().setHouseinsurance(item.get("住宅積立金納付基数").toString());
                    mongoTemplate.save(customerInfos.get(0));
                }

                Supplierbaseinfor supplierbaseinfor = new Supplierbaseinfor();
                supplierbaseinfor.setSupplierbaseinfor_id(UUID.randomUUID().toString());
                supplierbaseinfor.setSuppliernamecn(item.get("供应商中文名").toString());
                supplierbaseinfor.setSuppliernameen(item.get("供应商英文名").toString());

                supplierbaseinforMapper.insert(supplierbaseinfor);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }*/
        return null;
    }

}
