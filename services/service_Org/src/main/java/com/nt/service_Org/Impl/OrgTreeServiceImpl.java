package com.nt.service_Org.Impl;

import cn.hutool.core.util.ImageUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS1000.Vo.OrgTreeVo;
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
        return mongoTemplate.findAll(OrgTree.class);
    }
    /**
     * @方法名：get
     * @描述：根据ID获取组织机构树形结构
     * @创建日期：2021/04/23
     * @作者：GAOBINGBING
     * @参数：
     * @返回值：OrgTree
     */
    @Override
    public OrgTree getCurrentOrg(OrgTree org, String orgId) throws Exception {
        OrgTree returnorg = new OrgTree();
        if (org.get_id().equals(orgId)) {
            return org;
        } else {
            if (org.getOrgs() != null) {
                for (OrgTree item : org.getOrgs()) {
                    returnorg = getCurrentOrg(item, orgId);
                    if(returnorg.get_id() != null){
                        if (returnorg.get_id().equals(orgId)) {
                            return returnorg;
                        }
                    }
                }
            }

        }
        return new OrgTree();
    }

    //根据部门id获取部门组织信息
    @Override
    public OrgTreeVo getDepartmentinfo(String departid) throws Exception {
        OrgTree orgs = get(new OrgTree());
        List<OrgTreeVo> OrgTreeVolist = new ArrayList<>();
        OrgTreeVo orgtreevo = new OrgTreeVo();
        for (OrgTree org : orgs.getOrgs()) {//第一层循环
            orgtreevo = new OrgTreeVo();
            orgtreevo.set_id(org.get_id());//第一层_id
            orgtreevo.setMoney1(org.getCompanyname());//第一层名字
            orgtreevo.setMoney2("");//第二层_id
            orgtreevo.setMoney3("");//第二层名字
            orgtreevo.setMoney4("");//第三层_id
            orgtreevo.setMoney5("");//第三层名字
            orgtreevo.setMoney6(org.getUser());//负责人
            orgtreevo.setMoney7("1");//代表第一层
            orgtreevo.setMoney8(org.get_id());//本部门id
            OrgTreeVolist.add(orgtreevo);
            if(org.getOrgs() != null){
                for (OrgTree org1 : org.getOrgs()) {//第二层循环
                    orgtreevo = new OrgTreeVo();
                    orgtreevo.set_id(org.get_id());//第一层_id
                    orgtreevo.setMoney1(org.getCompanyname());//第一层名字
                    orgtreevo.setMoney2(org1.get_id());//第二层_id
                    orgtreevo.setMoney3(org1.getCompanyname());//第二层名字
                    orgtreevo.setMoney4("");//第三层_id
                    orgtreevo.setMoney5("");//第三层名字
                    orgtreevo.setMoney6(org1.getUser());//负责人
                    orgtreevo.setMoney7("2");//代表第二层
                    orgtreevo.setMoney8(org1.get_id());//本部门id
                    OrgTreeVolist.add(orgtreevo);
                    if(org1.getOrgs() != null){
                        for (OrgTree org2 : org1.getOrgs()) {//第三层循环
                            orgtreevo = new OrgTreeVo();
                            orgtreevo.set_id(org.get_id());//第一层_id
                            orgtreevo.setMoney1(org.getCompanyname());//第一层名字
                            orgtreevo.setMoney2(org1.get_id());//第二层_id
                            orgtreevo.setMoney3(org1.getCompanyname());//第二层名字
                            orgtreevo.setMoney4(org2.get_id());//第三层_id
                            orgtreevo.setMoney5(org2.getCompanyname());//第三层名字
                            orgtreevo.setMoney6(org2.getUser());//负责人
                            orgtreevo.setMoney7("3");//代表第三层
                            orgtreevo.setMoney8(org2.get_id());//本部门id
                            OrgTreeVolist.add(orgtreevo);
                        }
                    }
                }
            }
        }
        OrgTreeVolist = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney8().equals(departid))).collect(Collectors.toList());
        OrgTreeVo orgTreeVoOne = OrgTreeVolist.get(0);
        return orgTreeVoOne;
    }

    //根据部门id获取部门组织信息
    @Override
    public OrgTree getOrgInfo(OrgTree org, String compn) throws Exception {
        OrgTree returnorg = new OrgTree();
        if (org.get_id().equals(compn)) {
            return org;
        } else {
            if (org.getOrgs() != null) {
                for (OrgTree item : org.getOrgs()) {
                    returnorg = getOrgInfo(item, compn);
                    if(returnorg.get_id() != null){
                        if (returnorg.get_id().equals(compn)) {
                            return returnorg;
                        }
                    }
                }
            }

        }
        return new OrgTree();
    }

    //add ccm 20210819 获取所有有效部门的信息 fr
    //获取所有有效部门，从center查找预算编码，存在则作为部门，不存在则获取center下group作为部门
    @Override
    public List<DepartmentVo> getAllDepartment() throws Exception {
        List<DepartmentVo> departmentVoList = new ArrayList<>();
        DepartmentVo departmentVo = new DepartmentVo();
        //获取当前系统中有效的部门，按照预算编码统计
        OrgTree orgs = get(new OrgTree());
        //副总
        for (OrgTree orgfu : orgs.getOrgs()) {
            //Center
            for (OrgTree orgCenter : orgfu.getOrgs()) {
                if(!StringUtils.isNullOrEmpty(orgCenter.getEncoding()))
                {
                    departmentVo = new DepartmentVo();
                    departmentVo.setDepartmentId(orgCenter.get_id());
                    departmentVo.setDepartmentname(orgCenter.getCompanyname());
                    departmentVo.setDepartmentshortname(orgCenter.getCompanyshortname());
                    departmentVo.setDepartmentEncoding(orgCenter.getEncoding());
                    departmentVo.setDepartmentEn(orgCenter.getCompanyen());
                    departmentVo.setDepartmentType(orgCenter.getType());
                    departmentVo.setDepartmentUserid(orgCenter.getUser());
                    departmentVoList.add(departmentVo);
                }
                else
                {
                    for(OrgTree orgGroup : orgCenter.getOrgs())
                    {
                        departmentVo = new DepartmentVo();
                        departmentVo.setDepartmentId(orgGroup.get_id());
                        departmentVo.setDepartmentname(orgGroup.getCompanyname());
                        departmentVo.setDepartmentshortname(orgGroup.getCompanyshortname());
                        departmentVo.setDepartmentEncoding(orgGroup.getEncoding());
                        departmentVo.setDepartmentEn(orgGroup.getCompanyen());
                        departmentVo.setDepartmentType(orgGroup.getType());
                        departmentVo.setDepartmentUserid(orgGroup.getUser());
                        departmentVoList.add(departmentVo);
                    }
                }
            }
        }

        return departmentVoList;
    }
    //add ccm 20210819 获取所有有效部门的信息 to

    // region scc add 21/8/18 根据部门简称查询id from
    @Override
    public String queryIdBycom(String company) {
        String id = "";
        OrgTree returnorg = new OrgTree();
        OrgTree orgTree = null;
        List<OrgTree> byId = null;
        try {
            orgTree = this.get(returnorg);
            byId = this.getById(orgTree);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<OrgTree> orgs = byId.get(0).getOrgs();
        //region 主树下两组织
        jump:for (OrgTree org1 : orgs) {
            List<OrgTree> orgs1 = org1.getOrgs();
            // region 两组织下不同部门
            for (OrgTree org2 : orgs1) {
                // region 如果是経営管理
                if(org2.getCompanyen().equals("OMPC")){
                    List<OrgTree> orgs2 = org2.getOrgs();
                    for(OrgTree org3 : orgs2){
                        if (org3.getCompanyen().equals(company)) {
                            id = org3.get_id();
                            if(!StringUtils.isNullOrEmpty(id)){
                                break jump;
                            }
                        }
                    }
                }
                // endregion 如果是経営管理
                if (org2.getCompanyen().equals(company)) {
                    id = org2.get_id();
                    if(!StringUtils.isNullOrEmpty(id)){
                        break jump;
                    }
                }
            }
            // endregion 两组织下不同部门
        }
        // endregion 主树下两组织
        return id;
    }
    // endregion scc add 21/8/18 根据部门简称查询id to

    //region scc 21/8/17 获取所有有效部门简称 from
    @Override
    public List<String> queryAllComp() {
        ArrayList<String> companyen = new ArrayList<>();
        OrgTree returnorg = new OrgTree();
        OrgTree orgTree = null;
        List<OrgTree> byId = null;
        try {
            orgTree = this.get(returnorg);
            byId = this.getById(orgTree);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<OrgTree> orgs = byId.get(0).getOrgs();
        //region 主树下两组织
        jump:for (OrgTree org1 : orgs) {
            List<OrgTree> orgs1 = org1.getOrgs();
            // region 两组织下不同部门
            for (OrgTree org2 : orgs1) {
                // region 如果是経営管理
                if(org2.getCompanyen().equals("OMPC")){
                    List<OrgTree> orgs2 = org2.getOrgs();
                    for(OrgTree org3 : orgs2){
                        companyen.add(org3.getCompanyen());
                    }
                }
                // endregion 如果是経営管理
                companyen.add(org2.getCompanyen());
            }
            // endregion 两组织下不同部门
        }
        // endregion 主树下两组织
        return companyen;
    }
    //endregion scc 21/8/17 获取所有有效部门简称 to
}
