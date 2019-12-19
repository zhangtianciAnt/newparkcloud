package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.DutySimt;
import com.nt.service_BASF.DutySimtServices;
import com.nt.service_BASF.mapper.DutySimtMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: DutySimtServicesImpl
 * @Author: WXZ
 * @Description: DutySimtServicesImpl
 * @Date: 2019/12/19 15:09
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DutySimtServicesImpl implements DutySimtServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private DutySimtMapper dutySimtMapper;

    /**
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取值班人列表
     * @Return void
     * @Date 2019/12/19 15:14
     */
    @Override
    public List<DutySimt> list() throws Exception {
        DutySimt dutySimt = new DutySimt();
        return dutySimtMapper.select(dutySimt);
    }

    /**
     * @param dutySimt
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新值班人
     * @Return void
     * @Date 2019/12/19 15：18
     */
    @Override
    public void update(DutySimt dutySimt, TokenModel tokenModel) throws Exception {
        dutySimt.preUpdate(tokenModel);
        dutySimtMapper.updateByPrimaryKey(dutySimt);
    }

}
