package com.nt.service_AOCHUAN.AOCHUAN2000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.utils.IOUtils;
import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN5000.KisLogin;
import com.nt.service_AOCHUAN.AOCHUAN2000.CustomerbaseinforService;
import com.nt.service_AOCHUAN.AOCHUAN2000.mapper.CustomerbaseinforMapper;
import com.nt.service_AOCHUAN.AOCHUAN5000.mapper.FinCrdlInfoMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;
import java.util.*;

@Service
public class CustomerbaseinforServiceImpl implements CustomerbaseinforService {

    @Autowired
    private CustomerbaseinforMapper customerbaseinforMapper;
    @Autowired
    private K3CloundConfig k3CloundConfig;

    @Autowired
    private FinCrdlInfoMapper finCrdlInfoMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Customerbaseinfor> get() throws Exception {
        return customerbaseinforMapper.selectALLcust();
    }

    @Override
    public Customerbaseinfor getOne(String id) throws Exception {
        return customerbaseinforMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Customerbaseinfor customerbaseinfor, TokenModel tokenModel) throws Exception {
        customerbaseinfor.preUpdate(tokenModel);
        customerbaseinforMapper.updateByPrimaryKeySelective(customerbaseinfor);
    }

    @Override
    public String insert(Customerbaseinfor customerbaseinfor, TokenModel tokenModel) throws Exception {
        String id = UUID.randomUUID().toString();
        //add_fjl_1021  添加编码
//        int number = 10001;
//        int seleocount = customerbaseinforMapper.allselectCount();
//        String  num = String.valueOf(number + seleocount);
//        num = num.substring(1,5);
//        customerbaseinfor.setCustnumber("CUST"+num);
        //add_fjl_1021  添加编码
        customerbaseinfor.setCustomerbaseinfor_id(id);
        customerbaseinfor.preInsert(tokenModel);
        customerbaseinforMapper.insert(customerbaseinfor);
        return id;
    }

    @Override
    public void delete(String id) throws Exception {
        Customerbaseinfor customerbaseinfor = new Customerbaseinfor();
        customerbaseinfor.setCustomerbaseinfor_id(id);
        customerbaseinfor.setStatus("1");
        customerbaseinforMapper.updateByPrimaryKeySelective(customerbaseinfor);
//        customerbaseinforMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void pushKingdee(List<Customerbaseinfor> list,TokenModel tokenModel,Boolean flg) throws Exception {
        //正式dbid：5f1533095ad35e
        //测试dbid：5f4f0eaa667840
//        String loginParam = BaseUtil.buildLogin("5f4f0eaa667840", "Administrator", "888888", 2052);
//        ResultVo login = login(k3CloundConfig.url + k3CloundConfig.login, loginParam);
        KisLogin kisLogin = finCrdlInfoMapper.selectKislogin();
        String loginParam = "";
        if(kisLogin != null){
            loginParam = BaseUtil.buildLogin(kisLogin.getKisloginid(), kisLogin.getUsername(), kisLogin.getUserpassword(), kisLogin.getLcid());
        }

        ResultVo login = BaseUtil.login(k3CloundConfig.url + k3CloundConfig.login, loginParam);
        if (login.getCode() != ResultEnum.SUCCESS.getCode()) {
//            log.error("【登录金蝶系统失败】：{}", login.getMsg());
            throw new LogicalException("登录金蝶系统失败");
//            return;
        }
        Map<String, Object> map = (Map<String, Object>) login.getData();
        String cookie = map.get("cookie").toString();

        /**
         @author Yangshubo
         @see 客户数据模板
         创建一个文件
         读取源文件
         生成流对象
         创建并写入一个临时文件中
         在读取写入的新文件
         */
        File file = null;
        //file = ResourceUtils.getFile("classpath:excel/customer.json");
        ClassPathResource resource  = new ClassPathResource("excel/customer.json");
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
        String jsonData = BaseUtil.jsonRead(file);;
        List<Map<String,Object>> listmap = new ArrayList();
        JSONObject basic = null;
        for(Customerbaseinfor customerbaseinfor : list){
            basic = JSON.parseObject(jsonData);
            Map<String,Object> model = (Map<String, Object>) basic.get("Model");
            Map<String,Object> fGroup = (Map<String, Object>) ((Map<String, Object>) basic.get("Model")).get("FCOUNTRY");
            int kis = 0;
            if(flg){ //true: 系统服务
                if(StringUtils.isNotEmpty(customerbaseinfor.getKisid())){
                    kis = Integer.valueOf(customerbaseinfor.getKisid());
                }
            } else { //false: 手动
                Customerbaseinfor customerbaseinfor1 = customerbaseinforMapper.selectByPrimaryKey(customerbaseinfor.getCustomerbaseinfor_id());
                if(customerbaseinfor1 != null){
                    if(StringUtils.isNotEmpty(customerbaseinfor1.getKisid())){
                        kis = Integer.valueOf(customerbaseinfor1.getKisid());
                    }
                }
            }
            model.put("FCUSTID",kis);
            model.put("FNumber",customerbaseinfor.getCustnumber());
            model.put("FName",customerbaseinfor.getCustomernameen());
            fGroup.put("FNumber",customerbaseinfor.getNation());//国家
            model.put("FCOUNTRY",fGroup);
            listmap.add(model);
        }
        basic.put("Model",listmap);

        //构造客户接口数据
        String customer = BaseUtil.buildMaterial(basic,KDFormIdEnum.CUSTOMER.getFormid());
        ResultVo save = batchSave(k3CloundConfig.url + k3CloundConfig.batchSave, cookie, customer,tokenModel,list,flg);

        if (save.getCode() != ResultEnum.SUCCESS.getCode()) {
            String mes = save.getMsg();
            String[] messageArr = mes.split("},");
            String[] messages = new String[messageArr.length];
            for(int a = 0 ; a < messageArr.length; a++){
                messages[a] = messageArr[a].split("\",\"FieldName\"")[0];
            }
            System.out.println(messages);
            String diamessage = "";
            for(int a = 0 ; a < messages.length; a ++){
                diamessage = diamessage +"第"+ (a+1)+"条"+messages[a];
            }


//            log.error("【保存出错】：{}", save.getMsg());
            throw new LogicalException("共有" +messages.length +"条数据推送出错 ：" + diamessage);
//            return;
        }
    }



    //add_wxl_1021 推送KIS

    //金蝶批量保存
    public ResultVo batchSave(String url,String cookie, String content,TokenModel tokenModel,List<Customerbaseinfor> customerbaseinforList,Boolean flg) throws Exception {
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
            updKisid(kisid,number,tokenModel,customerbaseinforList.get(i),flg);
        }

        if (isSuccess) {
            return ResultUtil.success();
        } else {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.get("Errors");
            return ResultUtil.error(JSON.toJSONString(errors));
        }
    }

    //系统服务
//    @Scheduled(cron = "0 0 0 1 * ?")
    public void pullKis() throws Exception {
        TokenModel tokenModel = new TokenModel();
        List<Customerbaseinfor> customerbaseinforList = customerbaseinforMapper.allselectData();
        pushKingdee(customerbaseinforList,tokenModel,true);
    }
    //返回kisid更新回去
    public void updKisid(Integer kisid, String number, TokenModel tokenModel,Customerbaseinfor customerbaseinfor,Boolean flg) throws Exception {
        if(customerbaseinfor != null){
            if(flg){  //true:系统服务
                customerbaseinfor.preUpdate(tokenModel);
                customerbaseinfor.setKisid(kisid.toString());
                customerbaseinfor.setCustnumber(number);
                customerbaseinforMapper.updateByPrimaryKeySelective(customerbaseinfor);
            } else {  //false:手动
                Customerbaseinfor cust = customerbaseinforMapper.selectByPrimaryKey(customerbaseinfor.getCustomerbaseinfor_id());
                if (cust != null) {
                    cust.preUpdate(tokenModel);
                    cust.setKisid(kisid.toString());
                    cust.setCustnumber(number);
                    customerbaseinforMapper.updateByPrimaryKeySelective(cust);
                }
            }
        }
    }
}
