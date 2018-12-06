package com.nt.service_Org;


import com.nt.dao_Org.Information;

import java.util.List;

public interface InformationService {

    //保存
    void save(Information information) throws Exception;

    //获取
    List<Information> get(Information information) throws Exception;
}
