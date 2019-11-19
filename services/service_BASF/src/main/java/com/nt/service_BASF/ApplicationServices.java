package com.nt.service_BASF;

import com.nt.dao_BASF.Application;
import com.nt.utils.dao.TokenModel;
import com.nt.dao_BASF.VO.ApplicationVo;

import java.util.List;


public interface ApplicationServices {

    List<Application> get(Application application) throws Exception;

    List<ApplicationVo> getList() throws Exception;

    void insert(TokenModel tokenModel, Application application) throws Exception;

    void update(TokenModel tokenModel, Application application) throws Exception;

    void del(TokenModel tokenModel, Application application) throws Exception;
}
