package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.BlackList;
import com.nt.dao_BASF.VO.BlackListApiVo;
import com.nt.dao_BASF.VO.BlackListVo;
import com.nt.service_BASF.BlackListServices;
import com.nt.service_BASF.mapper.BlackListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: DriverInformationServicesImpl
 * @Author: Wxz
 * @Description: DriverInformationServicesImpl
 * @Date: 2019/11/22 15:02
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BlackListServicesImpl implements BlackListServices {

    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private BlackListMapper blackListMapper;

    /**
     * @param blackList
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取黑名单列表
     * @Return java.util.List<driverInformation>
     * @Date 2019/11/22 15:05
     */
    @Override
    public List<BlackList> list(BlackList blackList) throws Exception {
        return blackListMapper.select(blackList);
    }

    /**
     * @return void
     * @Method createBlack
     * @Author SKAIXX
     * @Description 添加黑名单信息
     * @Date 2020/9/2 14:43
     * @Param [blackList]
     **/
    @Override
    public void createBlack(BlackList blackList) throws Exception {
        blackListMapper.insert(blackList);
    }

    /**
     * @param driverIdNo
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 查询是否为黑名单
     * @Return java.util.List<driverInformation>
     * @Date 2019/11/28 9:36
     */
    @Override
    public boolean checkblack(String driverIdNo) throws Exception {
        BlackList blackList = new BlackList();
        blackList.setDriveridnumber(driverIdNo);
        blackList.setStatus("0");
        return blackListMapper.select(blackList).size() != 0;
    }


    /**
     * @param blackList
     * @Method Delete
     * @Author Wxz
     * @Version 1.0
     * @Description 删除驾驶员黑名单信息
     * @Return void
     * @Date 2019/11/29 10：13
     */
    @Override
    public void delete(BlackList blackList) throws Exception {
        //逻辑删除（status -> "1"）
        blackListMapper.updateByPrimaryKeySelective(blackList);
    }
    /**
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取黑名单列表
     * @Return java.util.List<driverInformation>
     * @Date 2019/11/22 15:05
     */
    @Override
    public List<BlackListVo> getBlackList() throws Exception {
        return blackListMapper.getBlackList();
    }

    /**
     * @Method getBlackListApi
     * @Author myt
     * @Version 1.0
     * @Description 获取黑名单列表(车牌号&违规类型)
     * @Return java.util.List<BlackListApiVo>
     * @Date 2021/10/20 15:05
     */
    @Override
    public List<BlackListApiVo> getBlackListApi() throws Exception {
        for(BlackListApiVo blackListApiVo : blackListMapper.getBlackListApi()){
            if (blackListApiVo.getViolationtype().equals("1")) {
                blackListApiVo.setViolationtype("超速");
            } else {
                blackListApiVo.setViolationtype("偏离");
            }
        }
        return blackListMapper.getBlackListApi();
    }
}
