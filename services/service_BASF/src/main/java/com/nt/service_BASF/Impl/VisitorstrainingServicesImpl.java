package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Visitorstraining;
import com.nt.service_BASF.VisitorstrainingServices;
import com.nt.service_BASF.mapper.VisitorstrainingMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: VisitorstrainingServicesImpl
 * @Author: 王哲
 * @Description: 访客培训记录接口实现类
 * @Date: 2019/11/25 10:54
 * @Version: 1.0
 */
@Service
public class VisitorstrainingServicesImpl implements VisitorstrainingServices {

    @Autowired
    private VisitorstrainingMapper visitorstrainingMapper;

    @Override
    public List<Visitorstraining> list() throws Exception {
        return visitorstrainingMapper.select(new Visitorstraining());
    }

    @Override
    public void insert(Visitorstraining visitorstraining, TokenModel tokenModel) throws Exception {
        visitorstraining.preInsert(tokenModel);
        visitorstraining.setVisitorsid(UUID.randomUUID().toString());
        visitorstrainingMapper.insert(visitorstraining);
    }


}
