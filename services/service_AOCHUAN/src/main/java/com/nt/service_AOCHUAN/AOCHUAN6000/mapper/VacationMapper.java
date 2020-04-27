package com.nt.service_AOCHUAN.AOCHUAN6000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Vacation;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vo.StatisticsVo;
import com.nt.utils.MyMapper;

import java.util.List;


public interface VacationMapper extends MyMapper<Vacation> {
    public List<StatisticsVo> getVo();

}
