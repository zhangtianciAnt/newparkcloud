package com.nt.service_BASF.Impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nt.dao_BASF.BlackList;
import com.nt.dao_BASF.VO.BlackListApiVo;
import com.nt.dao_BASF.VO.BlackListVo;
import com.nt.service_BASF.BlackListServices;
import com.nt.service_BASF.mapper.BlackListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: DriverInformationServicesImpl
 * @Author: Wxz
 * @Description: DriverInformationServicesImpl
 * @Date: 2019/11/22 15:02
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BlackListServicesImpl implements BlackListServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private BlackListMapper blackListMapper;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @param blackList
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取黑名单列表
     * @Return java.util.List<driverInformation>
     * @Date 2019/11/22 15:05
     */
    @Override
    public List<BlackList> list(BlackList blackList) throws Exception {
        return blackListMapper.select(blackList);
    }

    /**
     * @return void
     * @Method createBlack
     * @Author SKAIXX
     * @Description 添加黑名单信息
     * @Date 2020/9/2 14:43
     * @Param [blackList]
     **/
    @Override
    public void createBlack(BlackList blackList) throws Exception {
        blackListMapper.insert(blackList);
    }

    /**
     * @param driverIdNo
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 查询是否为黑名单
     * @Return java.util.List<driverInformation>
     * @Date 2019/11/28 9:36
     */
    @Override
    public boolean checkblack(String driverIdNo) throws Exception {
        BlackList blackList = new BlackList();
        blackList.setDriveridnumber(driverIdNo);
        blackList.setStatus("0");
        return blackListMapper.select(blackList).size() != 0;
    }


    /**
     * @param blackList
     * @Method Delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除驾驶员黑名单信息
     * @Return void
     * @Date 2019/11/29 10：13
     */
    @Override
    public void delete(BlackList blackList) throws Exception {
        //逻辑删除（status -> "1"）
        int uptCount = blackListMapper.updateByPrimaryKeySelective(blackList);
        //删除成功后执行联动接口，调用门检系统接口
        if (uptCount > 0) {
            sendDelBlack(blackList);
        }
    }

    /**
     * @param blackList
     * @Method sendDelBlack
     * @Author Myt
     * @Version 1.0
     * @Description 调用门检系统接口，同步删除黑名单数据
     * @Return void
     * @Date 2019/11/29 10：13
     */
    public void sendDelBlack(BlackList blackList) {
        log.info("Door Inspection Interface Start!");
        // 道闸系统接口地址
        String urlToken = "http://basf-gatecheck-v3-t-api.dowann.com/api/auth/login?username=bach&password=123456&deviceInfo=pc";
        String urlDelBalck = "http://basf-gatecheck-v3-t-api.dowann.com/api/out/removebacklist";
        // 获取token
        ResponseEntity<String> rst = restTemplate.exchange(urlToken, HttpMethod.GET, null, String.class);
        String value = rst.getBody();
        JSONObject string_to_json = JSONUtil.parseObj(value).getJSONObject("data");
        String token = string_to_json.get("access_token").toString();
        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpHeaders.add("Authorization", String.format("Bearer %s", token));
        // 封装参数和头信息
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", blackList.getDrivername());//驾驶员姓名
        jsonObject.put("idcard", blackList.getDriveridnumber());//驾驶员身份证号码
        jsonArray.put(jsonObject);
        HttpEntity<JSONArray> httpEntity = new HttpEntity<>(jsonArray, httpHeaders);
        rst = restTemplate.exchange(urlDelBalck, HttpMethod.POST, httpEntity, String.class);
        JSONObject result = JSONUtil.parseObj(rst.getBody());
        if (result.get("status_code").toString().equals("200")) {
            log.info("Door Inspection Interface Call Succeeded!");
        } else {
            log.error("Door Inspection Interface Call Error:" + result.get("msg").toString());
        }
        log.info("Door Inspection Interface End!");
    }

    /**
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取黑名单列表
     * @Return java.util.List<driverInformation>
     * @Date 2019/11/22 15:05
     */
    @Override
    public List<BlackListVo> getBlackList() throws Exception {
        return blackListMapper.getBlackList();
    }

    /**
     * @Method getBlackListApi
     * @Author myt
     * @Version 1.0
     * @Description 获取黑名单列表(车牌号&违规类型)
     * @Return java.util.List<BlackListApiVo>
     * @Date 2021/10/20 15:05
     */
    @Override
    public List<BlackListApiVo> getBlackListApi() throws Exception {
        for(BlackListApiVo blackListApiVo : blackListMapper.getBlackListApi()){
            if (blackListApiVo.getViolationtype().equals("1")) {
                blackListApiVo.setViolationtype("超速");
            } else {
                blackListApiVo.setViolationtype("偏离");
            }
        }
        return blackListMapper.getBlackListApi();
    }
}
