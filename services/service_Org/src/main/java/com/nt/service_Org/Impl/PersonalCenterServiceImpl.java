package com.nt.service_Org.Impl;

import Vo.PersonalCenter;
import com.nt.dao_Org.CustomerInfo;
import com.nt.service_Org.PersonalCenterService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;



import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class PersonalCenterServiceImpl implements PersonalCenterService {

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
    public CustomerInfo get(CustomerInfo customerInfo) throws Exception {
            Query query = CustmizeQuery(customerInfo);
            customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            return customerInfo;
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
    public void save(PersonalCenter personalCenter) throws Exception {
        mongoTemplate.save(personalCenter);

    }
}
