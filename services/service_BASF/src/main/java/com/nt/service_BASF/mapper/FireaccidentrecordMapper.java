package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Fireaccidentrecord;
import com.nt.dao_BASF.VO.FireaccidentrecordVo;
import com.nt.utils.MyMapper;

import java.util.List;

public interface FireaccidentrecordMapper extends MyMapper<Fireaccidentrecord> {

    //获取消防事故记录列表
    List<FireaccidentrecordVo> selectList() throws Exception;
}
