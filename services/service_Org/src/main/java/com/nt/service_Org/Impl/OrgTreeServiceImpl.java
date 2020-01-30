package com.nt.service_Org.Impl;

import cn.hutool.core.util.ImageUtil;
import com.nt.dao_Org.OrgTree;
import com.nt.service_Org.OrgTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public List<OrgTree> get(OrgTree orgTree) throws Exception {
        Query query = CustmizeQuery(orgTree);
        return mongoTemplate.find(query, OrgTree.class);
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
        orgTree.preInsert();
        orgTree.setTenantid(UUID.randomUUID().toString());
        mongoTemplate.save(orgTree);
    }

    /**
     * @方法名：get
     * @描述：获取当前组织机构树形结构
     * @创建日期：2018/12/03
     * @作者：ZHANGYING
     * @参数：[orgTree]
     * @返回值：java.util.List<com.nt.dao_Org.OrgTree>
     */
    @Override
    public List<OrgTree> getById(OrgTree orgTree) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(orgTree.get_id()));
        List<OrgTree> orgTrees = mongoTemplate.find(query, OrgTree.class);
        if (orgTrees == null || orgTrees.size() == 0) {
//            Query queryOrg = new Query();
//            queryOrg.addCriteria(Criteria.where("orgs.orgs._id").is(orgTree.get_id()));
//            orgTrees = mongoTemplate.find(queryOrg,OrgTree.class);
            orgTrees = dynamicsQuery("", orgTree.get_id());
            // make result only with org
//            List<OrgTree> result = new ArrayList<>();
//            if ( orgTrees!=null && orgTrees.size() > 0 ) {
//                for ( OrgTree o : orgTrees ) {
//                    List<OrgTree> orgsNode = o.getOrgs();
//                    for ( OrgTree o1 : orgsNode ) {
//                        if ( orgTree.get_id().equals(o1.get_id()) ) {
//                            result.add(o1);
//                        }
//                    }
//                }
//            }
//            return result;
        }
        return orgTrees;
    }

    private List<OrgTree> dynamicsQuery(String query, String id) {
        String dynamicsQuery = query.replace("_id", "") + "orgs._id";
        Query queryOrg = new Query();
        queryOrg.addCriteria(Criteria.where(dynamicsQuery).is(id));
        List<OrgTree> orgTrees = mongoTemplate.find(queryOrg, OrgTree.class);
        if (orgTrees.size() == 0) {
            orgTrees = dynamicsQuery(dynamicsQuery, id);
        }
        return orgTrees;
    }
}
