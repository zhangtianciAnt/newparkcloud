package com.nt.service_BASF;

import com.nt.dao_BASF.Highriskarea;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF90921Services
 * @Author: GJ
 * @Description:高风险作业接口
 * @Date: 2020/03/27 10:59
 * @Version: 1.0
 */
public interface HighriskareaServices {
    //获取高风险作业列表
    List<Highriskarea> list() throws Exception;

}
