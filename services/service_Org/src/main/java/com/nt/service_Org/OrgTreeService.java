package com.nt.service_Org;

import com.nt.dao_Org.OrgTree;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_Org
 * @ClassName: 组织机构的相关Services
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2018/10/25
 * @UpdateUser: SKAIXX
 * @UpdateDate: 2018/10/25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface OrgTreeService {

    // 获取组织机构树形结构
    OrgTree get(OrgTree orgTree) throws Exception;

    // 更新或插入组织机构属性结构
    void save(OrgTree orgTree) throws Exception;

    // 获取当前组织机构树形结构
    List<OrgTree> getById(OrgTree orgTree) throws Exception;

    // 获取组织机构树形结构
    OrgTree getTreeYears(String Years,String Status) throws Exception;
}
