package com.nt.service_AOCHUAN.AOCHUAN2000.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import com.nt.service_AOCHUAN.AOCHUAN2000.CustomerbaseinforService;
import com.nt.service_AOCHUAN.AOCHUAN2000.mapper.CustomerbaseinforMapper;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.*;

@Service
public class CustomerbaseinforServiceImpl implements CustomerbaseinforService {

    @Autowired
    private CustomerbaseinforMapper customerbaseinforMapper;
    @Autowired
    private K3CloundConfig k3CloundConfig;
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
        int number = 10001;
        int seleocount = customerbaseinforMapper.allselectCount();
        String  num = String.valueOf(number + seleocount);
        num = num.substring(1,5);
        customerbaseinfor.setCustnumber("CUST"+num);
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
    public void pushKingdee(List<Customerbaseinfor> list,TokenModel tokenModel) throws Exception {

        String loginParam = BaseUtil.buildLogin("5f4f0eaa667840", "Administrator", "888888", 2052);
        ResultVo login = login(k3CloundConfig.url + k3CloundConfig.login, loginParam);
        if (login.getCode() != ResultEnum.SUCCESS.getCode()) {
//            log.error("【登录金蝶系统失败】：{}", login.getMsg());
            throw new LogicalException("登录金蝶系统失败");
//            return;
        }
        Map<String, Object> map = (Map<String, Object>) login.getData();
        String cookie = map.get("cookie").toString();

        //获取客户数据模板
        File file = null;
        file = ResourceUtils.getFile("classpath:excel/customer.json");
        String jsonData = BaseUtil.jsonRead(file);;
        List<Map<String,Object>> listmap = new ArrayList();
        JSONObject basic = null;
        for(Customerbaseinfor customerbaseinfor : list){
            Customerbaseinfor customerbaseinfor1 = customerbaseinforMapper.selectByPrimaryKey(customerbaseinfor.getCustomerbaseinfor_id());

            basic = JSON.parseObject(jsonData);
            Map<String,Object> model = (Map<String, Object>) basic.get("Model");
            Map<String,Object> fGroup = (Map<String, Object>) ((Map<String, Object>) basic.get("Model")).get("FGroup");
            if(StringUtils.isNotEmpty(customerbaseinfor1.getKisid())){
                model.put("FCUSTID",customerbaseinfor1.getKisid());
            }
            else{
                model.put("FCUSTID",0);
            }
            model.put("FNumber",customerbaseinfor.getCustnumber());
            model.put("FName",customerbaseinfor.getCustomernameen());
            fGroup.put("FNumber",customerbaseinfor.getNation());
            model.put("FGroup",fGroup);
            listmap.add(model);
        }
        basic.put("Model",listmap);

        //构造供应商接口数据
        String customer = BaseUtil.buildMaterial(basic,KDFormIdEnum.CUSTOMER.getFormid());
        ResultVo save = batchSave(k3CloundConfig.url + k3CloundConfig.batchSave, cookie, customer,tokenModel);

        if (save.getCode() != ResultEnum.SUCCESS.getCode()) {
//            log.error("【保存出错】：{}", save.getMsg());
            throw new LogicalException("保存出错" + save.getMsg());
//            return;
        }
    }



    //add_wxl_1021 推送KIS
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
    public ResultVo batchSave(String url,String cookie, String content,TokenModel tokenModel) throws Exception {
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
            updKisid(kisid,number,tokenModel);
        }

        if (isSuccess) {
            return ResultUtil.success();
        } else {
            List<Map<String, Object>> errors = (List<Map<String, Object>>) responseStatus.get("Errors");
            return ResultUtil.error(JSON.toJSONString(errors));
        }
    }

    //返回kisid更新回去
    public void updKisid(Integer kisid, String number, TokenModel tokenModel) throws Exception {
        Customerbaseinfor customerbaseinfor = new Customerbaseinfor();
        customerbaseinfor.setCustnumber(number);
        List<Customerbaseinfor> cust = customerbaseinforMapper.select(customerbaseinfor);
        if (cust.size() > 0) {
            customerbaseinfor.preUpdate(tokenModel);
            cust.get(0).setKisid(kisid.toString());
            customerbaseinforMapper.updateByPrimaryKeySelective(cust.get(0));
        }

    }

}
