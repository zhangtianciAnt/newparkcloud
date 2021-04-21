package com.nt.service_Org.Impl;

import cn.hutool.core.util.ImageUtil;
import com.nt.dao_Org.OrgTree;
import com.nt.service_Org.OrgTreeService;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        orgTree.setStatus("0");
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
        // update gbb 20210325 查询组织架构添加【有效】条件 start
        query.addCriteria(Criteria.where("status").is("0"));
        // update gbb 20210325 查询组织架构添加【有效】条件 end
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
        // update gbb 20210325 查询组织架构添加【有效】条件 start
        queryOrg.addCriteria(Criteria.where("status").is("0"));
        // update gbb 20210325 查询组织架构添加【有效】条件 end
        List<OrgTree> orgTrees = mongoTemplate.find(queryOrg, OrgTree.class);
        if (orgTrees.size() == 0) {
            orgTrees = dynamicsQuery(dynamicsQuery, id);
        }
        return orgTrees;
    }

    //update gbb 20210308  禅道任务708  start
    @Override
    public OrgTree getTreeYears(String Years,String Status) throws Exception {
        Query query = new Query();
        //历史组织架构
        query.addCriteria(Criteria.where("years").is(Years));
        OrgTree orgtree = mongoTemplate.findOne(query, OrgTree.class);
        if(Status.equals("1")){
            query.addCriteria(Criteria.where("status").is("1"));
            List<OrgTree> orgTrees = mongoTemplate.find(query, OrgTree.class);
            orgTrees = orgTrees.stream().sorted(Comparator.comparing(OrgTree::getYears).reversed()).collect(Collectors.toList());
            if (orgTrees.size() > 0) {
                orgtree = orgTrees.get(0);
            }
        }
        return orgtree;
    }
    //update gbb 20210308  禅道任务708  end

    @Override
    public void updateStatus(String Years) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("0"));
        List<OrgTree> orgtreeList = mongoTemplate.find(query, OrgTree.class);
        if (orgtreeList.size() > 0) {
            for (OrgTree orgTree0 : orgtreeList) {
                orgTree0.setStatus("1");
                mongoTemplate.save(orgTree0);
            }
        }
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("years").is(Years));
        OrgTree orgTree1 = mongoTemplate.findOne(query1, OrgTree.class);
        orgTree1.setStatus("0");
        mongoTemplate.save(orgTree1);
    }

    /**
     * @方法名：get
     * @描述：获取所有组织机构树形结构
     * @创建日期：2021/04/21
     * @作者：GAOBINGBING
     * @参数：
     * @返回值：java.util.List<com.nt.dao_Org.OrgTree>
     */
    @Override
    public List<OrgTree> getOrgAll() throws Exception {
        List<OrgTree> orgTreeAll = new ArrayList<>();
        List<OrgTree> orgTreeList = mongoTemplate.findAll(OrgTree.class);
        for(OrgTree org :orgTreeList){
            orgTreeAll.add(org);
            if (org.getOrgs() != null) {
                for (OrgTree org1 : org.getOrgs()) {
                    orgTreeAll.add(org1);
                    if (org1.getOrgs() != null) {
                        for (OrgTree org2 : org1.getOrgs()) {
                            orgTreeAll.add(org2);
                            if (org2.getOrgs() != null) {
                                for (OrgTree org3 : org2.getOrgs()) {
                                    orgTreeAll.add(org3);
                                }
                            }
                        }
                    }
                }
            }
        }
        return orgTreeAll;
    }
}
