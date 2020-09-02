package com.nt.service_BASF.Impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.nt.dao_BASF.Mapbox;
import com.nt.dao_BASF.PersonnelPermissions;
import com.nt.dao_SQL.APBCardHolderVo;
import com.nt.service_BASF.MapboxServices;
import com.nt.service_BASF.PersonnelPermissionsServices;
import com.nt.utils.CowBUtils;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class MapboxServiceImpl implements MapboxServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PersonnelPermissionsServices personnelPermissionsServices;

    @Value("${sqlSerer.ip}")
    private String Url;

    @Override
    public Mapbox get(Mapbox mapbox) throws Exception {
        Query query = CustmizeQuery(mapbox);
        mapbox = mongoTemplate.findOne(query, Mapbox.class);
        return mapbox;
    }

    @Override
    public List<APBCardHolderVo> selectUsersByDeviceid(String deviceid) throws Exception {

        // 获取personnelpermissions下人员类别分组
        List<PersonnelPermissions> personnelPermissions = personnelPermissionsServices.selectByClass();
        // 获取当天门禁系统说有不是“厂外”的所有部门id
        Map<String, Object> map = new HashMap<>();//存放参数
        map.put("deviceid",deviceid);
        List<APBCardHolderVo> apbCardHolderVos = JSONObject.parseArray(
                JSONObject.parseObject(HttpUtil.get(Url + "userInfo/selectUsersByDeviceid", map)).get("data").toString(), APBCardHolderVo.class
        );
        // 获取员工分类信息
        for (APBCardHolderVo item : apbCardHolderVos){
            if(StringUtils.isEmpty(item.getType())){
                continue;
            }
            if(CowBUtils.wordsEqualOf(item.getType(),
                    personnelPermissions.stream().filter(p -> p.getClassname().equals("class1")).collect(Collectors.toList()).get(0).getAllname().split(","))){
                item.setType("员工");

            }
            else if(CowBUtils.wordsEqualOf(item.getType(),
                    personnelPermissions.stream().filter(p -> p.getClassname().equals("class2")).collect(Collectors.toList()).get(0).getAllname().split(","))){
                item.setType("临时访客");
            }
            else if(CowBUtils.wordsEqualOf(item.getType(),
                    personnelPermissions.stream().filter(p -> p.getClassname().equals("class3")).collect(Collectors.toList()).get(0).getAllname().split(","))){
                item.setType("承包商");
            }
        }

        return apbCardHolderVos;
    }

}
