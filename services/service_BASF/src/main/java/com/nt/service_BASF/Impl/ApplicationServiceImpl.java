package com.nt.service_BASF.Impl;
/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: ApplicationServiceImpl
 * @Author: LXY
 * @Description: 消防设备申请接口实现类
 * @Date: 2019/11/15
 * @Version: 1.0
 */
import com.nt.dao_BASF.Application;
import com.nt.service_BASF.ApplicationServices;
import com.nt.service_BASF.mapper.ApplicationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nt.dao_BASF.VO.ApplicationVo;

import java.util.List;
import java.util.UUID;

@Service
public class ApplicationServiceImpl implements ApplicationServices {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    public List<Application> get(Application application) throws Exception {
        return applicationMapper.select(application);
    }

    public List<ApplicationVo> getList() throws Exception {
        return applicationMapper.selectApplicationVoList();
    }

    @Override
    public void insert(TokenModel tokenModel, Application application) throws Exception {
        application.preInsert(tokenModel);
        application.setApplicationid(UUID.randomUUID().toString());
        application.setApplicationstatus("BC012005");
        applicationMapper.insert(application);
    }

    @Override
    public void update(TokenModel tokenModel, Application application) throws Exception {
        application.preUpdate(tokenModel);
        applicationMapper.updateByPrimaryKeySelective(application);
    }

    @Override
    public void del(TokenModel tokenModel, Application application) throws Exception {
        application.preUpdate(tokenModel);
        applicationMapper.updateByPrimaryKeySelective(application);
    }

    @Override
    public Application one(String applicationid) throws Exception {
        return applicationMapper.selectByPrimaryKey(applicationid);
    }
}
