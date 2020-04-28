package com.nt.service_AOCHUAN.AOCHUAN6000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Secrecy;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Vo.All;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SecrecyMapper extends MyMapper<Secrecy> {

    List<Secrecy> selectsecrecy();

}
