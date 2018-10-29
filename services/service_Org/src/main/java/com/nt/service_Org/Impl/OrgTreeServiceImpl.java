package com.nt.service_Org.Impl;

import com.nt.dao_Org.OrgTree;
import com.nt.service_Org.OrgTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
public class OrgTreeServiceImpl implements OrgTreeService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @方法名：get
     * @描述：获取组织机构树形结构
     * @创建日期：2018/10/23
     * @作者：SKAIXX
     * @参数：[orgTree]
     * @返回值：java.util.List<com.nt.dao_Org.OrgTree>
     */
    @Override
    public OrgTree get(OrgTree orgTree) throws Exception {
        Query query = CustmizeQuery(orgTree);
        orgTree = mongoTemplate.findOne(query, OrgTree.class);
        return orgTree;
    }

    /**
     * @方法名：save
     * @描述：更新或插入组织机构属性结构
     * @创建日期：2018/10/24
     * @作者：SKAIXX
     * @参数：[orgTree]
     * @返回值：void
     */
    @Override
    public void save(OrgTree orgTree) throws Exception {
        // 执行更新操作
        mongoTemplate.save(orgTree);
    }
}
