package com.nt.service_Org;


import com.nt.dao_Org.ServiceCategory;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ServiceCategoryService {

    //保存
    void save(ServiceCategory servicecategory) throws Exception;

    //获取
    List<ServiceCategory> get(ServiceCategory servicecategory) throws Exception;

}
