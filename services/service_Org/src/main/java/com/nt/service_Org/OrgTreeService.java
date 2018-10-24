package com.nt.service_Org;

import com.nt.dao_Org.OrgTree;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrgTreeService {

    // 获取组织机构树形结构
    OrgTree get(OrgTree orgTree) throws Exception;

    // 更新或插入组织机构属性结构
    void save(OrgTree orgTree, HttpServletRequest request) throws Exception;
}
