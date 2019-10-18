package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Overtime;
import com.nt.utils.MyMapper;
import java.util.List;

public interface OvertimeMapper extends MyMapper<Overtime> {
    List<Overtime> getOvertime();
}
