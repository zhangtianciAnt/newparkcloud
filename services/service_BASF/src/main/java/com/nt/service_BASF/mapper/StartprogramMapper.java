package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Emergencytemplate;
import com.nt.dao_BASF.Startprogram;
import com.nt.dao_BASF.VO.PassingRateVo;
import com.nt.dao_BASF.VO.TrainEducationPerVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: StartprogramMapper
 * @Author: 王哲
 * @Description: 申请考核Mapper
 * @Date: 2020/1/7 11:08
 * @Version: 1.0
 */
@Component(value = "StartprogramMapper")
public interface StartprogramMapper extends MyMapper<Startprogram> {

    List<Startprogram> selectbyuserid(@Param("userid") String userid, @Param("selecttype") String selecttype);

    //获取强制的通过/未通过
    List<PassingRateVo> getMandatoryInfo();
    //获取非强制的通过/未通过
    List<PassingRateVo> getIsMandatoryInfo();
    //获取培训教育人员详细
    List<TrainEducationPerVo>getTrainEducationPerInfo();

    //大屏培训信息推送列表
    List<Startprogram> getFutureProgram();

}
