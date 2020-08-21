package com.nt.service_BASF;

import com.nt.dao_BASF.Pimsdata;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:PimsdataServices
 * @Author: myt
 * @Description:PIMS系统数据服务接口
 * @Date: 2020/08/17 16：30
 * @Version: 1.0
 */
public interface PimsdataServices {

    // 导入PIMS系统数据
    void insert(List<Pimsdata> pimsdata) throws Exception;

    //获取大屏环境相关信息
    List<Pimsdata> getAllPimsInfo(String type) throws Exception;
}
