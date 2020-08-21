package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Pimsdata;
import com.nt.service_BASF.PimsdataServices;
import com.nt.service_BASF.mapper.PimsdataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:PimadataServicesImpl
 * @Author: WXL
 * @Description: BASF环保数据PIMS系统数据模块实现类
 * @Date: 2020/08/17 16:30
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PimadataServicesImpl implements PimsdataServices {

    private static Logger log = LoggerFactory.getLogger(PimadataServicesImpl.class);

    @Autowired
    private PimsdataMapper pimsdataMapper;

    /**
     * @param pimsdata
     * @Method insert
     * @Author myt
     * @Version 1.0
     * @Description 导入PIMS系统数据
     * @Return void
     * @Date 2020/08/17 16:30
     */
    @Override
    public void insert(List<Pimsdata> pimsdata) throws Exception {
        Date date = new Date();
        for(Pimsdata temp : pimsdata){
            temp.setPimsid(UUID.randomUUID().toString());
            temp.setCreateby("PimsSystem");
            temp.setCreateon(date);
            pimsdataMapper.insert(temp);
        }
    }

    /**
     * @Description:获取大屏环境相关信息
     * @Param: []
     * @return: java.util.List<com.nt.dao_BASF.Pimsdata>
     * @Author: Mr.LXX
     * @Date: 2020/8/19
     */
    @Override
    public List<Pimsdata> getAllPimsInfo(String type) throws Exception {
        return pimsdataMapper.getAllPimsInfo(type);
    }
}
