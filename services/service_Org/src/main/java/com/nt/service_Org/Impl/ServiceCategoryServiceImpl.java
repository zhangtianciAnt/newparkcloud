package com.nt.service_Org.Impl;
import com.nt.dao_Org.ServiceCategory;
import com.nt.service_Org.ServiceCategoryService;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @方法名：save
     * @描述：服务类目
     * @创建日期：2018/12/15
     * @作者：SUNXU
     * @参数：[servicecategory, request]
     * @返回值：
     */
    @Override
    public void save(ServiceCategory servicecategory) throws Exception {
        mongoTemplate.save(servicecategory);
    }

    /**
     * @方法名：get
     * @描述：获取服务类目
     * @创建日期：2018/12/15
     * @作者：SUNXU
     * @参数：[servicecategory, request]
     * @返回值：ServiceCategory
     */
    @Override
    public List<ServiceCategory> get(ServiceCategory servicecategory) throws Exception {
        //共同带权限查询
        Query query = CustmizeQuery(servicecategory);
        return mongoTemplate.find(query, ServiceCategory.class);
    }
}
