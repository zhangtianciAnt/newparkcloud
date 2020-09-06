package com.nt.controller.Controller.BASF.BASFLANController;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.Electronicfencealarm;
import com.nt.dao_BASF.Electronicfencestatus;
import com.nt.service_BASF.ElectronicfenceService;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
import com.nt.service_BASF.mapper.ElectronicfencestatusMapper;
import com.nt.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 电子围栏相关controller
 */
@RestController
@RequestMapping("/electronicfence")
public class ElectronicfenceController {

    @Autowired
    private ElectronicfenceService electronicfenceService;

    @Resource
    private ElectronicfencestatusMapper electronicfencestatusMapper;

    @Resource
    private DeviceinformationMapper deviceinformationMapper;

    /**
     * 更新围栏报警信息状态
     */
    @RequestMapping(value = "/updateElectronicfencestatus", method = {RequestMethod.POST})
    public ApiResult updateElectronicfencestatus(@RequestBody Electronicfencestatus electronicfencestatus) {
        electronicfencestatusMapper.updateByPrimaryKeySelective(electronicfencestatus);
        return ApiResult.success();
    }

    /**
     * 查询电子围栏报警单
     *
     * @param electronicfencealarm
     * @return
     */
    @RequestMapping(value = "getElectronicfences", method = {RequestMethod.POST})
    public ApiResult getElectronicfences(@RequestBody Electronicfencealarm electronicfencealarm) throws Exception {
        List<Electronicfencealarm> electronicfencealarmList = electronicfenceService.getElectronicfences(electronicfencealarm);
        return ApiResult.success(electronicfencealarmList);
    }

    /**
     * 获取所有电子围栏信息
     */
    @RequestMapping(value = "getElectronicfencesinfos", method = {RequestMethod.POST})
    public ApiResult getElectronicfencesinfos() {
        List<Deviceinformation> deviceinformations = deviceinformationMapper.selectElectricShields();
        return ApiResult.success(deviceinformations);
    }
}
