package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Startprogram;
import com.nt.dao_BASF.VO.PassingRateVo;
import com.nt.dao_BASF.VO.TrainEducationPerVo2;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    List<Startprogram> selectbyuserid(@Param("userid") String userid, @Param("selecttype") String selecttype) throws Exception;

    //获取强制的通过/未通过
    List<PassingRateVo> getMandatoryInfo();

    //获取非强制的通过/未通过
    List<PassingRateVo> getIsMandatoryInfo();

    //根据姓名（或员工号、卡号）和年份查询某人员培训信息（培训教育大屏用）
    List<TrainEducationPerVo2> getTrainEducationPerInfo(@Param("year") String year, @Param("parameter") String parameter) throws Exception;

    //根据姓名（或员工号、卡号）和年份查询某人员培训时长信息（培训教育大屏用）
    Double getTrainThelength(@Param("year") String year, @Param("parameter") String parameter) throws Exception;

    //根据姓名（或员工号、卡号）和年份查询某人员部门信息（培训教育大屏用）
    String getDepartmentname(@Param("year") String year, @Param("parameter") String parameter) throws Exception;

    //大屏培训信息推送列表
    List<Startprogram> getFutureProgram();

}
