package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Trainmail;
import com.nt.dao_BASF.VO.TrainmailVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value="TrainmailMapper")
public interface TrainmailMapper extends MyMapper<Trainmail> {

    List<TrainmailVo> getAllList() throws Exception;

    TrainmailVo getone(@Param("trainmailid") String trainmailid) throws Exception;
}
