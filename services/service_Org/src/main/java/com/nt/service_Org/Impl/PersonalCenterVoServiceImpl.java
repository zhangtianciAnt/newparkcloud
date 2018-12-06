package com.nt.service_Org.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Org.Vo.PersonalCenterVo;
import com.nt.service_Org.PersonalCenterVoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class PersonalCenterVoServiceImpl implements PersonalCenterVoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @方法名：get
     * @描述：获取基本信息数据
     * @创建日期：2018/12/05
     * @作者：FEIJIALIANG
     * @参数：[customerInfo]
     * @返回值：java.util.List<com.nt.dao_Org.PersonalCenter>
     */
    @Override
    public PersonalCenterVo get(String userid) throws Exception {
        PersonalCenterVo  sut = new PersonalCenterVo();
        //根据条件检索数据
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userid));
        UserAccount account = mongoTemplate.findOne(query,UserAccount.class);
        sut.setUserAccount(account);
        Query newquery = new Query();
        newquery.addCriteria(Criteria.where("userid").is(userid));
        newquery.addCriteria(Criteria.where("type").is('1'));//个人类型
        CustomerInfo newaccount = mongoTemplate.findOne(newquery,CustomerInfo.class);
        sut.setCustomerInfo(newaccount);
        return sut;
    }

    /**
     * @方法名：save
     * @描述：更新或保存基本信息
     * @创建日期：2018/12/05
     * @作者：FEIJIALIANG
     * @参数：[personalCenter]
     * @返回值：void
     */
    @Override
    public void save(CustomerInfo personalCenter) throws Exception {
        mongoTemplate.save(personalCenter);

    }
}
