package com.nt.service_BASF;

import com.nt.dao_BASF.VO.StartprogramTrainVo;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: StartprogramTrainServices
 * @Author: 王哲
 * @Description: 强制/非强制率接口
 * @Date: 2020/4/22 11:06
 * @Version: 1.0
 */
public interface StartprogramTrainServices {

    //获取强制的通过/未通过
    List<StartprogramTrainVo> getDeptThrough(String year) throws Exception;

    //获取非强制的通过/未通过
    List<StartprogramTrainVo> getUnDeptThrough(String year) throws Exception;
}
