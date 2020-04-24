package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Emergencytemplate;
import com.nt.dao_BASF.Trainjoinlist;
import com.nt.dao_BASF.VO.OverduePersonnelListVo;
import com.nt.dao_BASF.VO.StartprogramTrainVo;
import com.nt.dao_BASF.VO.TrainjoinlistVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: TrainjoinlistMapper
 * @Author: 王哲
 * @Description: 培训参加名单Mapper
 * @Date: 2020/1/7 11:10
 * @Version: 1.0
 */
@Component(value = "TrainjoinlistMapper")
public interface TrainjoinlistMapper extends MyMapper<Trainjoinlist> {

    //大屏培训教育即将到期人员列表
    List<OverduePersonnelListVo> OverduePersonnelList() throws Exception;

    //结果发布判断该培训是否存在正常参加人员通过状态为空
    int isNotThroughtype(@Param("startprogramid") String startprogramid) throws Exception;

    //获取参加培训的人员id们
    List<String> joinPersonnelid() throws Exception;

    //获取部门名和通过状态（强制）
    List<TrainjoinlistVo> selectDeptThrough(String year) throws Exception;

    //获取部门名和所有通过状态（强制）
    List<TrainjoinlistVo> selectAllDeptThrough(String year) throws Exception;

    //获取部门名和通过状态（非强制）
    List<TrainjoinlistVo> selectUnDeptThrough(String year) throws Exception;

    //获取部门名和所有通过状态（非强制）
    List<TrainjoinlistVo> selectUnAllDeptThrough(String year) throws Exception;
}
