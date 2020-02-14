package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Vo.ChipinfoListVo;
import com.nt.service_PHINE.ChipinfoService;
import com.nt.service_PHINE.mapper.ChipinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: ChipinfoServiceImpl
 * @Description: 芯片信息实现类
 * @Author: MYT
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
@Service
public class ChipinfoServiceImpl implements ChipinfoService {

    @Autowired
    private ChipinfoMapper chipinfoMapper;

    /**
     * @Method getDeviceListByCompanyId
     * @Author MYT
     * @Description 根据企业ID获取设备列表信息
     * @Date 2020/1/31 15:27
     * @Param companyid 企业ID
     **/
    @Override
    public List<ChipinfoListVo> getChipTypeList() {
        return chipinfoMapper.selectChipTypeList();
    }
}
